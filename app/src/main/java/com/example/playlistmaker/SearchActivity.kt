package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchActivity : AppCompatActivity() {

    private var searchText = SEARCH_TEXT_BLANK
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create<PlaylistAPI>()
    var tracks = mutableListOf<Track>()
    var historyTracks = mutableListOf<Track>()
    val searchAdapter = SearchAdapter(tracks)
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
    private lateinit var searchHistory: SearchHistory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchToolbar = findViewById(R.id.tbSearch)
        searchBar = findViewById(R.id.search_bar)
        clearButton = findViewById(R.id.iv_clearButton)
        searchScreen = findViewById(R.id.search_main)
        recyclerView = findViewById(R.id.recyclerView_search)
        placeholderSearchError = findViewById(R.id.placeholderSearchError)
        placeholderServerErrors = findViewById(R.id.placeholderServerErrors)
        refreshButton = findViewById(R.id.btnRefresh)
        historyClearButton = findViewById(R.id.historyClearButton)
        historyHeader = findViewById(R.id.searchHistoryHeader)

        searchBar.setText(searchText)

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)

        //вывод результатов истории
        recyclerView.layoutManager = LinearLayoutManager(this)

        //обработчик нажатия на кнопку Done в всплывающей клавиатуре
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        //возврат через кнопку тулбара на предыдущий экран
        searchToolbar.setOnClickListener {
            finish()
        }

        //обработчик нажатия на кнопку очистки строки поиска
        clearButton.setOnClickListener {
            searchBar.setText("")
            clearPlaceholders()
            inputMethodManager?.hideSoftInputFromWindow(searchScreen.windowToken, 0)
            historyVisibility(false)
            searchBar.clearFocus()
        }

        //очистка истории при нажатии на кнопку "удалить историю"
        historyClearButton.setOnClickListener {
            searchHistory.clearHistory()
            var s = historyTracks.size
            historyTracks.clear()
            searchHistoryAdapter.notifyItemRangeRemoved(0, s)
            historyVisibility(true)
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

        //добавление в историю результатов поиска
        searchAdapter.onItemClick = {
            searchHistory.saveTrackToHistory(it)
        }

    }

    // метод отправки вызова "поиска" на сервер
    fun request() {
        clearPlaceholders()
        recyclerView.adapter = searchAdapter
        recyclerView.visibility = View.VISIBLE

        iTunesService.search(searchText).enqueue(object : Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                if (response.code() == 200) {
                    tracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.addAll(response.body()?.results!!)
                        searchAdapter.notifyDataSetChanged()

                    } else {
                        showSearchError(1)
                    }
                } else {
                    showSearchError(2)
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                showSearchError(3)
            }
        })
    }

    // метод очистки поисковой строки
    fun clearButtonVisibility(s: CharSequence?): Int {
        if (s.isNullOrBlank()) {
            return View.GONE
        }
        clearPlaceholders()
        return View.VISIBLE
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
        searchText = savedInstanceState?.getString(SEARCH_TEXT) ?: SEARCH_TEXT_BLANK
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_TEXT_BLANK = ""
    }


    // метод вывода плейсхолдеров при ошибках поиска
    private fun showSearchError(codeError: Int) {
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

    // очистка экрана от плейсхолдеров
    fun clearPlaceholders() {
        placeholderSearchError.visibility = View.GONE
        placeholderServerErrors.visibility = View.GONE
    }


    fun historyVisibility(flag: Boolean) {
        if (flag) {
            historyTracks = searchHistory.getTrackFromHistory()
            searchHistoryAdapter = SearchHistoryAdapter(historyTracks)
            recyclerView.adapter = searchHistoryAdapter

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


}
