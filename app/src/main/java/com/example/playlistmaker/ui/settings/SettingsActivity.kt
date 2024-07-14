package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.presentation.viewmodel.SettingsViewModel
import kotlin.properties.Delegates


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel
    private var nightModeOn by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val settingsToolbar = binding.tbSettings
        val themeSwitcher = binding.swtch
        val shareButton = binding.ivOption2
        val callSupportButton = binding.ivOption3
        val readAgreementButton = binding.ivOption4

        viewModel = SettingsViewModel()

        settingsToolbar.setOnClickListener {
            finish()
        }
        shareButton.setOnClickListener {
            viewModel.shareApp()
        }

        callSupportButton.setOnClickListener {
            viewModel.openSupport()
        }

        readAgreementButton.setOnClickListener {
            viewModel.openTerms()
        }

        themeSwitcher.setOnCheckedChangeListener() { _, checked ->
            nightModeOn = checked
            viewModel.updateThemeSetting(nightModeOn)
        }

    }
}