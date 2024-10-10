package com.example.playlistmaker.ui.library

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
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
import com.markodevcic.peko.PermissionResult
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPlaylistFragment : Fragment() {

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddPlaylistViewModel by viewModel()
    private lateinit var toolbar: Toolbar
    private var nameText = EMPTY_STRING
    private var descriptionText = EMPTY_STRING
    private lateinit var nameInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var playlistCover: ImageView
    private lateinit var addButton: MaterialButton
    private val pickImage: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.setCoverImage(uri.toString())
            }
        }

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
        addButton.setOnClickListener {
            viewModel.addPlaylist()
        }

        playlistCover.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        viewModel.getToggleButtonLiveData().observe(viewLifecycleOwner) { flag ->
            toggleButton(flag)
        }
        viewModel.getStateLiveData().observe(viewLifecycleOwner) { it ->
            renderState(it)
        }

        viewModel.getCoverLiveData().observe(viewLifecycleOwner) { it ->
            setCover(it)
        }

        viewModel.getPermissionLiveData().observe(viewLifecycleOwner) { it ->
            when (it) {
                is PermissionResult.Granted -> {
                    return@observe
                }

                is PermissionResult.Denied.NeedsRationale -> {
                    Toast.makeText(
                        requireContext(),
                        "Разрешение на доступ к внутренней памяти телефона необходимо для хранения обложек ваших плейлистов",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is PermissionResult.Denied.DeniedPermanently -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.data = Uri.fromParts("package", context?.packageName, null)
                    context?.startActivity(intent)
                }

                PermissionResult.Cancelled -> return@observe
            }
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

    private fun setCover(string: String) {
        playlistCover.setImageURI(string.toUri())
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}