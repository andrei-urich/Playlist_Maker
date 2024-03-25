package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.btn_Search)
        val buttonLibrary = findViewById<Button>(R.id.btn_Library)
        val buttonSetup = findViewById<Button>(R.id.btn_Setup)


        val buttonClickListener: View.OnClickListener = View.OnClickListener { v ->
            when (v) {
                buttonSearch -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                }

                buttonLibrary -> {
                    val intent = Intent(this, LibraryActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        buttonSearch.setOnClickListener(buttonClickListener)
        buttonLibrary.setOnClickListener(buttonClickListener)
        buttonSetup.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }
}