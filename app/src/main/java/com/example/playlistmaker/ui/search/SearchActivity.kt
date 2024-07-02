package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.EMPTY_STRING
import com.example.playlistmaker.SEARCH_DEBOUNCE_DELAY
import com.example.playlistmaker.SEARCH_HISTORY_PREFERENCES
import com.example.playlistmaker.TRACK_INFO
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.ResourceResponseResult
import com.example.playlistmaker.domain.ResourceResponseResult.SUCCESS
import com.example.playlistmaker.domain.SearchConsumer
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.player.AudioplayerActivity

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private var searchText = EMPTY_STRING

    private lateinit var viewBinding: ActivitySearchBinding
    private val trackSearch = Creator.provideTracksSearch()
    private val trackTransfer = Creator.provideTrackTransfer()

    var tracks = mutableListOf<Track>()
    var historyTracks = mutableListOf<Track>()
    val searchAdapter = SearchAdapter(tracks)
    val searchHistoryAdapter = SearchHistoryAdapter(historyTracks)
    private lateinit var searchToolbar: Toolbar
    private lateinit var searchBar: EditText
    private lateinit var clearButton: ImageView
    private lateinit var searchScreen: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyClearButton: Button
    private lateinit var historyHeader: TextView
    private lateinit var placeholderSearchError: LinearLayout
    private lateinit var placeholderServerErrors: LinearLayout
    private lateinit var refreshButton: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var handler: Handler
    private lateinit var progressBar: ProgressBar
    private var isClickAllowed = true

    private val searchRunnable = Runnable { request() }
    private var tracksRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        searchToolbar = viewBinding.tbSearch
        searchBar = viewBinding.searchBar
        clearButton = viewBinding.ivClearButton
        searchScreen = viewBinding.searchScreen
        recyclerView = viewBinding.recyclerViewSearch
        placeholderSearchError = viewBinding.placeholderSearchError
        placeholderServerErrors = viewBinding.placeholderServerErrors
        refreshButton = viewBinding.btnRefresh
        historyClearButton = viewBinding.historyClearButton
        historyHeader = viewBinding.searchHistoryHeader
        progressBar = viewBinding.progressBar

        handler = Handler(Looper.getMainLooper())

        searchBar.setText(searchText)

        val sharedPrefs = getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //обработчик всплывающей клавиатуры
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        //возврат через кнопку тулбара на предыдущий экран
        searchToolbar.setOnClickListener {
            finish()
        }

        //обработчик нажатия на кнопку очистки строки поиска
        clearButton.setOnClickListener {
            searchBar.setText(EMPTY_STRING)
            clearPlaceholders()
            inputMethodManager?.hideSoftInputFromWindow(searchScreen.windowToken, 0)
            historyVisibility(false)
            searchBar.clearFocus()
        }

        //очистка истории при нажатии на кнопку "удалить историю"
        historyClearButton.setOnClickListener {
            searchHistory.clearHistory()
            historyTracks.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            historyVisibility(false)
        }

        //текствотчер для поисковой строки
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                if (searchBar.hasFocus() && s?.isEmpty() == true) historyVisibility(true) else historyVisibility(
                    false
                )
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        //Условие показа истории если строка поиска в фокусе
        searchBar.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchBar.text.isEmpty()) historyVisibility(true) else historyVisibility(
                false
            )
        }

        searchBar.addTextChangedListener(searchTextWatcher)

        //обработка нажатия "ввод" на виртуальной клавиатуре
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                request()
                true
            }
            false
        }

        //кнопка "Обновить" для отправки повторного запроса в случае ошибки соединения
        refreshButton.setOnClickListener {
            clearPlaceholders()
            request()
        }

        //переход в плеер и сохранение в историю результатов поиска
        searchAdapter.onItemClick = {
            searchHistory.saveTrackToHistory(it)
            val playerIntent = Intent(this, AudioplayerActivity::class.java)
            val trackToPlay = trackTransfer.sendTrack(it)
            playerIntent.putExtra(TRACK_INFO, trackToPlay)
            startActivity(playerIntent)
        }


        //переход в плеер из истории
        searchHistoryAdapter.onItemClick = {
            if (clickDebounce()) {
                val playerIntent = Intent(this, AudioplayerActivity::class.java)
                val trackToPlay = trackTransfer.sendTrack(it)
                playerIntent.putExtra(TRACK_INFO, trackToPlay)
                startActivity(playerIntent)
            }
        }
    }

    fun request() {
        if (searchText.isNotEmpty()) {
            clearPlaceholders()
            progressBar.visibility = View.VISIBLE

            trackSearch.search(
                searchText,
                consumer = object : SearchConsumer {
                    override fun consume(tracks: List<Track>) {
                        val currentRunnable = tracksRunnable
                        if (currentRunnable != null) {
                            handler.removeCallbacks(currentRunnable)
                        }

                        val newTracksRunnable = Runnable {


                            if (ResourceResponseResult.resourceResponseResult == SUCCESS) {
                                this@SearchActivity.tracks.clear()
                                progressBar.visibility = View.GONE
                                recyclerView.adapter = searchAdapter
                                recyclerView.visibility = View.VISIBLE

                                if (tracks.isNotEmpty() == true) {
                                    this@SearchActivity.tracks.addAll(tracks)
                                    searchAdapter.notifyDataSetChanged()

                                } else {
                                    progressBar.visibility = View.GONE
                                    showSearchError(1)
                                }
                            } else {
                                progressBar.visibility = View.GONE
                                showSearchError(2)
                            }
                        }

                        tracksRunnable = newTracksRunnable
                        handler.post(newTracksRunnable)
                    }

                })
        }
    }

    // метод очистки поисковой строки
    fun clearButtonVisibility(s: CharSequence?): Int {
        if (s.isNullOrBlank()) {
            return View.GONE
        }
        clearPlaceholders()
        return View.VISIBLE
    }

    // очистка экрана от плейсхолдеров
    fun clearPlaceholders() {
        placeholderSearchError.visibility = View.GONE
        placeholderServerErrors.visibility = View.GONE
    }

    // метод вывода плейсхолдеров при ошибках поиска
    private fun showSearchError(codeError: Int) {
        historyVisibility(false)
        if (codeError == 1) {
            placeholderSearchError.visibility = View.VISIBLE
            tracks.clear()
            searchAdapter.notifyDataSetChanged()

        } else {
            placeholderServerErrors.visibility = View.VISIBLE
            tracks.clear()
            searchAdapter.notifyDataSetChanged()

        }
    }

    // метод показа/скрытия истории поиска
    fun historyVisibility(flag: Boolean) {
        if (flag) {
            historyTracks.clear()
            historyTracks.addAll(searchHistory.getTrackFromHistory())
            recyclerView.adapter = searchHistoryAdapter
            searchHistoryAdapter.notifyDataSetChanged()

            if (historyTracks.isNotEmpty()) {
                historyClearButton.visibility = View.VISIBLE
                historyHeader.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
            }

        } else {
            historyClearButton.visibility = View.GONE
            historyHeader.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }
    }

    // метод дебаунса ввода в строке поиска
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    // метод дебаунса клика на результатах поиска (клик на треке для вызова плеера)
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


    // методы для сохранения введеного значения в поисковой строке
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        searchText = savedInstanceState?.getString(SEARCH_TEXT) ?: EMPTY_STRING
    }

    override fun onDestroy() {
        val currentRunnable = tracksRunnable
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }
        super.onDestroy()
    }
}
