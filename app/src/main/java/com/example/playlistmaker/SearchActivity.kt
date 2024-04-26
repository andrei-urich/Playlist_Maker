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

    private val tracks = ArrayList<Track>()

    private val searchAdapter = SearchAdapter(tracks)

    private lateinit var searchToolbar: Toolbar
    private lateinit var searchBar: EditText
    private lateinit var clearButton: ImageView
    private lateinit var searchScreen: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholderSearchError: LinearLayout
    private lateinit var placeholderServerErrors: LinearLayout
    private lateinit var refreshButton: Button


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

        searchBar.setText(searchText)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = searchAdapter

        //обработчик нажатия на кнопку Done в всплывающей клавиатуре

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        searchToolbar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        clearButton.setOnClickListener {
            searchBar.setText("")
            clearPlaceholders()
            inputMethodManager?.hideSoftInputFromWindow(searchScreen.windowToken, 0)
            tracks.clear()
            searchAdapter.notifyDataSetChanged()
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        searchBar.addTextChangedListener(searchTextWatcher)

        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                request()
                true
            }
            false
        }

        refreshButton.setOnClickListener {
            clearPlaceholders()
            request()
        }
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

    // метод отправки вызова "поиска" на сервер
    fun request() {
        clearPlaceholders()
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

    // очистка экрана от плейсхолдеров
    fun clearPlaceholders() {
        placeholderSearchError.visibility = View.GONE
        placeholderServerErrors.visibility = View.GONE
    }


}
