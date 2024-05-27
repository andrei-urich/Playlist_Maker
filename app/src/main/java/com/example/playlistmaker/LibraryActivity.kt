package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.tbLib)
       val trackImage = findViewById<ImageView>(R.id.trackImageLib)
       trackImage.setImageDrawable(getDrawable(R.drawable.placeholder_big))

        toolbar.setOnClickListener {
            finish()
        }

    }
}