package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class SearchActivity : AppCompatActivity() {

    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageView>(R.id.iv_Back)
        val searchBar = findViewById<EditText>(R.id.search_bar)
        val clearButton = findViewById<ImageView>(R.id.iv_clearButton)
        val searchScreen = findViewById<View>(R.id.search_main)

        searchBar.setText(searchText)

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        clearButton.setOnClickListener {
            searchBar.setText("")
            inputMethodManager?.hideSoftInputFromWindow(searchScreen.windowToken, 0)
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

    }


    fun clearButtonVisibility(s: CharSequence?): Int {
        if (s.isNullOrBlank()) {
            return View.GONE
        }
        return View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        searchText = savedInstanceState?.getString(SEARCH_TEXT)
    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

}
