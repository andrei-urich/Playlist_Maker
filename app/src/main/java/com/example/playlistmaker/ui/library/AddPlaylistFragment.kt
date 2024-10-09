package com.example.playlistmaker.ui.library

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel.Companion.ADD_OK
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel.Companion.GO
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel.Companion.GO_OR_STAY
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel.Companion.STAY
import com.example.playlistmaker.utils.EMPTY_STRING
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddPlaylistFragment : Fragment() {

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddPlaylistViewModel by viewModels()
    private lateinit var toolbar: Toolbar
    private var nameText = EMPTY_STRING
    private var descriptionText = EMPTY_STRING
    private lateinit var nameInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var playlistCover: ImageView
    private lateinit var addButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<BottomNavigationView>(R.id.bnView).visibility =
            View.GONE
        toolbar = binding.tbCreatePlaylist
        nameInput = binding.etNamePlaylist
        descriptionInput = binding.etDescriptionPlaylist
        playlistCover = binding.playlistImage
        addButton = binding.btnCreatePlaylist


        toolbar.setOnClickListener {
            viewModel.goOrStay()
        }

        viewModel.getToggleButtonLiveData().observe(viewLifecycleOwner) { flag ->
            toggleButton(flag)
        }
        viewModel.getStateLiveData().observe(viewLifecycleOwner) { it ->
            renderState(it)
        }

        val nameInputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameText = s.toString()
                viewModel.setName(nameText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        val descriptionInputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                descriptionText = s.toString()
                viewModel.setDescription(descriptionText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        nameInput.addTextChangedListener(nameInputTextWatcher)
        descriptionInput.addTextChangedListener(descriptionInputTextWatcher)


    }

    private fun renderState(string: String) {
        when (string) {
            GO_OR_STAY -> {}
            STAY -> {}
            GO -> {
                closeScreen()
            }

            ADD_OK -> {}
        }
    }

    private fun closeScreen() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun toggleButton(flag: Boolean) {
        if (flag) {
            addButton.isClickable = true
            addButton.setBackgroundColor(requireActivity().getColor(R.color.active_item_color))
        }
    }

    private fun showMessageOK() {

    }

    private fun showDialogGoOrStay() {

    }
}