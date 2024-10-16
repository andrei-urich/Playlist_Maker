package com.example.playlistmaker.ui.library

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel.Companion.EXIT
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel.Companion.EXIT_OR_STAY
import com.example.playlistmaker.presentation.library.AddPlaylistViewModel.Companion.EXIT_TRACK_ADDED
import com.example.playlistmaker.utils.EMPTY_STRING
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.markodevcic.peko.PermissionResult
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddPlaylistFragment : Fragment() {

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!
    private var track: Track? = null
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
    private lateinit var onExitDialog: MaterialAlertDialogBuilder
    private lateinit var callback: OnBackPressedCallback
    private val args: AddPlaylistFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireActivity().findViewById<BottomNavigationView>(R.id.bnView) != null) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bnView).visibility = View.GONE
        }
        toolbar = binding.tbCreatePlaylist
        nameInput = binding.etNamePlaylist
        descriptionInput = binding.etDescriptionPlaylist
        playlistCover = binding.playlistImage
        addButton = binding.btnCreatePlaylist
        track = args.track

        onExitDialog =
            MaterialAlertDialogBuilder(requireActivity()).setTitle(context?.getString(R.string.on_exit_add_playlist_screen_dialog_title))
                .setMessage(context?.getString(R.string.on_exit_add_playlist_screen_dialog_message))
                .setNeutralButton(context?.getString(R.string.on_exit_add_playlist_screen_dialog_neutralButton)) { dialog, which ->

                }
                .setPositiveButton(context?.getString(R.string.on_exit_add_playlist_screen_dialog_positiveButton)) { dialog, which ->
                    closeScreen()
                }

        toolbar.setOnClickListener {
            viewModel.exitOrStay()
        }

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.exitOrStay()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        addButton.setOnClickListener {
            viewModel.addPlaylist(track)
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
        Glide.with(playlistCover.context).load(coverUri).fitCenter().transform(RoundedCorners(8))
            .dontAnimate().into(playlistCover)

    }

    private fun renderState(string: String) {
        when (string) {
            EXIT_OR_STAY -> {
                onExitDialog.show()
            }

            EXIT -> {
                showMessagePlaylistAdded()
                closeScreen()
            }

            EXIT_TRACK_ADDED -> {
                showMessageTrackAddedInPlaylist()
                closeScreen()
            }
        }
    }

    private fun closeScreen() {
        callback.isEnabled = false
        requireActivity().onBackPressedDispatcher.onBackPressed()
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

    private fun showMessagePlaylistAdded() {
        val text =
            context?.getString(R.string.toast_playlist_created_message_part1) + " " + editedName + " " + context?.getString(
                R.string.toast_playlist_created_message_part2
            )
        val snackbar = Snackbar.make(
            binding.root, text,
            Snackbar.LENGTH_LONG
        )
        val snackbarView = snackbar.view
        context?.getColor(R.color.text_primary)?.let { snackbarView.setBackgroundColor(it) }
        val textView: TextView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text)
        context?.getColor(R.color.primary)?.let { textView.setTextColor(it) }
        textView.setTextAppearance(R.style.snake_text_style)
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
        textView.setGravity(Gravity.CENTER)
        snackbar.show()
    }

    private fun showMessageTrackAddedInPlaylist() {
        val text =
            context?.getString(R.string.add_to_created_playlist_message) + " " + editedName
        val snackbar = Snackbar.make(
            binding.root, text,
            Snackbar.LENGTH_LONG
        )
        val snackbarView = snackbar.view
        context?.getColor(R.color.text_primary)?.let { snackbarView.setBackgroundColor(it) }
        val textView: TextView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text)
        context?.getColor(R.color.primary)?.let { textView.setTextColor(it) }
        textView.setTextAppearance(R.style.snake_text_style)
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER)
        textView.setGravity(Gravity.CENTER)
        snackbar.show()
    }
}

