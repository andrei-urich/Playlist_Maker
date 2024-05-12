package com.example.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val settingsToolbar = findViewById<Toolbar>(R.id.tbSettings)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.swtch)
        val shareButton = findViewById<ImageView>(R.id.iv_option2)
        val callSupportButton = findViewById<ImageView>(R.id.iv_option3)
        val readAgreementButton = findViewById<ImageView>(R.id.iv_option4)

        settingsToolbar.setOnClickListener {
            finish()
        }
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_link))
            startActivity(Intent.createChooser(shareIntent, getString(R.string.app_share_header)))
        }

        callSupportButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.supportEmail)))
            emailIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.callSupportDefaultSubject)
            )
            emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.callSupportDefaultMessage))
            startActivity(emailIntent)
        }

        readAgreementButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(getString(R.string.agreement_link))
            startActivity(browserIntent)
        }

        themeSwitcher.setOnCheckedChangeListener() { _, checked ->
            (applicationContext as MyApp).switchTheme(checked)
            sharedPrefs.edit().putBoolean(NIGHT_MODE, checked).apply()
        }

    }
}