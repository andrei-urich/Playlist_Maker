package com.example.playlistmaker.ui.library

import android.content.Context
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
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel.Companion.GO
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel.Companion.GO_OR_STAY
import com.example.playlistmaker.utils.EMPTY_STRING
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.markodevcic.peko.PermissionResult
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPlaylistFragment : Fragment() {

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddPlaylistViewModel by viewModel()
    private lateinit var toolbar: Toolbar
    private var nameText = EMPTY_STRING
    private var editedName = EMPTY_STRING
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
    lateinit var onExitDialog: MaterialAlertDialogBuilder
    private val inputMethodManager by lazy { -> requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager }

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

        onExitDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(context?.getString(R.string.on_exit_add_playlist_screen_dialog_title))
            .setNeutralButton(context?.getString(R.string.on_exit_add_playlist_screen_dialog_neutralButton)) { dialog, which ->

            }
            .setPositiveButton(context?.getString(R.string.on_exit_add_playlist_screen_dialog_positiveButton)) { dialog, which ->
                closeScreen()
            }

        toolbar.setOnClickListener {
            viewModel.goOrStay()
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goOrStay()
            }
        })

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
                        context?.getString(R.string.permission_WRITE_EXTRNAL_STORAGE_rationale),
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
                editedName = s.toString()
                viewModel.setEditedName(editedName)

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
        playlistCover.setImageURI(null)
        val coverUri = string.toUri()
        Glide.with(playlistCover.context)
            .load(coverUri)
            .fitCenter()
            .transform(RoundedCorners(8))
            .dontAnimate()
            .into(playlistCover)

    }

    private fun renderState(string: String) {
        when (string) {
            GO_OR_STAY -> {
                onExitDialog.show()
            }

            GO -> {
                closeScreen()
            }
        }
    }

    private fun closeScreen() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun toggleButton(flag: Boolean) {
        if (flag) {
            addButton.isClickable = true
            addButton.setBackgroundColor(requireActivity().getColor(R.color.active_item_color))
        } else {
            addButton.isClickable = false
            addButton.setBackgroundColor(requireActivity().getColor(R.color.add_playlist_button))
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}