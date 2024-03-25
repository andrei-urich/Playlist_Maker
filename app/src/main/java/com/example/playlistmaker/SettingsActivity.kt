package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.iv_Back)
        val themeChangeButton = findViewById<Switch>(R.id.swtch)
        val shareButton = findViewById<ImageView>(R.id.iv_option2)
        val callSupportButton = findViewById<ImageView>(R.id.iv_option3)
        val readAgreementButton = findViewById<ImageView>(R.id.iv_option4)


        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        themeChangeButton.setOnClickListener {}
        shareButton.setOnClickListener {}
        callSupportButton.setOnClickListener {}
        readAgreementButton.setOnClickListener {}
    }
}