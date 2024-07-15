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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.utils.EMPTY_STRING
import com.example.playlistmaker.utils.TRACK_INFO
import com.example.playlistmaker.presentation.search.TrackSearchState
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.search.SearchViewModel.Companion.factory
import com.example.playlistmaker.ui.player.AudioplayerActivity

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val ERROR = "ERROR"
        private const val LOADING = "LOADING"
        private const val SHOW_RESULT = "SHOW_RESULT"
    }

    private var searchText = EMPTY_STRING
    private lateinit var viewBinding: ActivitySearchBinding
    private val trackTransfer = Creator.provideTrackTransfer()

    private var tracks = mutableListOf<Track>()
    private var historyTracks = mutableListOf<Track>()

    private lateinit var viewModel: SearchViewModel

    lateinit var searchAdapter: SearchAdapter
    lateinit var searchHistoryAdapter: SearchHistoryAdapter

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
    private lateinit var handler: Handler
    private lateinit var progressBar: ProgressBar
    private var isClickAllowed = true

    private lateinit var searchRunnable: Runnable
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

        viewModel = ViewModelProvider(
            this,
            factory()
        )[SearchViewModel::class.java]

        searchRunnable = Runnable { viewModel.request(searchText) }
        searchBar.setText(searchText)

        searchAdapter = SearchAdapter(tracks, viewModel::playTrack)
        searchHistoryAdapter = SearchHistoryAdapter(historyTracks, viewModel::playTrackFromHistory)

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
            viewModel.clearHistory()
            historyTracks.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            historyVisibility(false)
        }

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
                viewModel.request(searchText)
                true
            }
            false
        }

        //кнопка "Обновить" для отправки повторного запроса в случае ошибки соединения
        refreshButton.setOnClickListener {
            clearPlaceholders()
            viewModel.request(searchText)
        }

        //переход в плеер из истории
//        searchHistoryAdapter.onItemClick = {
//            if (clickDebounce()) {
//                val playerIntent = Intent(this, AudioplayerActivity::class.java)
//                val trackToPlay = trackTransfer.sendTrack(it)
//                playerIntent.putExtra(TRACK_INFO, trackToPlay)
//                startActivity(playerIntent)
//            }
//        }

        viewModel.getSearchStateLiveData().observe(this) { searchState ->
            when (searchState) {
                is TrackSearchState.Error -> {
                    changeContentVisibility(showCase = ERROR)
                }

                is TrackSearchState.Content -> {
                    tracks.clear()
                    tracks.addAll(searchState.trackList.toMutableList())
                    changeContentVisibility(showCase = SHOW_RESULT)
                }

                is TrackSearchState.Loading -> {
                    changeContentVisibility(showCase = LOADING)
                }
            }
        }

        viewModel.getPlayTrackTrigger().observe(this) { track ->
            playTrack(track)
        }
    }

    private fun playTrack(track: Track) {
        val playerIntent = Intent(this, AudioplayerActivity::class.java)
        val trackToPlay = trackTransfer.sendTrack(track)
        playerIntent.putExtra(TRACK_INFO, trackToPlay)
        startActivity(playerIntent)
    }


    private fun changeContentVisibility(showCase: String) {
        when (showCase) {
            ERROR -> {
                progressBar.visibility = View.GONE
                showSearchError(2)
            }

            LOADING -> {
                clearPlaceholders()
                progressBar.visibility = View.VISIBLE
            }

            SHOW_RESULT -> {
                clearPlaceholders()
                progressBar.visibility = View.GONE
                recyclerView.adapter = searchAdapter
                recyclerView.visibility = View.VISIBLE

                if (tracks.isNotEmpty() == true) {
                    searchAdapter.notifyDataSetChanged()

                } else {
                    progressBar.visibility = View.GONE
                    showSearchError(1)
                }
            }
        }
    }

    // метод дебаунса ввода в строке поиска
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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
            historyTracks.addAll(viewModel.getHistoryList())
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
        savedInstanceState: Bundle?, persistentState: PersistableBundle?
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
