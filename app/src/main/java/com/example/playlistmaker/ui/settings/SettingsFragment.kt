package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import kotlin.properties.Delegates
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()
    private var nightModeOn by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val themeSwitcher = binding.swtch
        val shareButton = binding.ivOption2
        val callSupportButton = binding.ivOption3
        val readAgreementButton = binding.ivOption4

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
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}