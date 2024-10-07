package com.example.playlistmaker.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.utils.EMPTY_STRING
import com.example.playlistmaker.presentation.search.TrackSearchState
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.main.BottomNavigationListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class SearchFragment : Fragment() {

    private var bottomNavigationListener: BottomNavigationListener? = null

    private var searchText = EMPTY_STRING
    private var _viewBinding: FragmentSearchBinding? = null
    private val viewBinding get() = _viewBinding!!

    private var tracks = mutableListOf<Track>()
    private var historyTracks = mutableListOf<Track>()

    private val viewModel: SearchViewModel by viewModel()
    lateinit var searchAdapter: SearchAdapter
    lateinit var searchHistoryAdapter: SearchHistoryAdapter

    private val inputMethodManager by lazy { -> requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager }

    private lateinit var searchToolbar: Toolbar
    private lateinit var searchBar: EditText
    private lateinit var clearButton: ImageView
    private lateinit var searchScreen: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyClearButton: Button
    private lateinit var historyHeader: TextView
    private lateinit var placeholderSearchError: LinearLayout
    private lateinit var placeholderServerErrors: LinearLayout
    private lateinit var refreshButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var action: NavDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        KeyboardVisibilityEvent.setEventListener(
            activity = requireActivity(),
            lifecycleOwner = viewLifecycleOwner
        ) { isVisible ->
            if (isVisible) {
                onKeyboardVisibilityChanged(true)
            } else {
                onKeyboardVisibilityChanged(false)
            }
        }





        searchToolbar = viewBinding.tbSearch
        searchBar = viewBinding.searchBar
        clearButton = viewBinding.ivClearButton
        searchScreen = viewBinding.searchScreen
        recyclerView = viewBinding.recyclerViewSearch
        placeholderSearchError = viewBinding.placeholderSearchError
        placeholderServerErrors = viewBinding.placeholderServerErrors
        refreshButton = viewBinding.btnRefresh
        historyClearButton = viewBinding.historyClearButton
        historyHeader = viewBinding.searchHistoryHeader
        progressBar = viewBinding.progressBar

        searchBar.setText(searchText)

        searchAdapter = SearchAdapter(tracks, viewModel::playTrack)
        searchHistoryAdapter = SearchHistoryAdapter(historyTracks, viewModel::playTrackFromHistory)

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.getSearchStateLiveData().observe(viewLifecycleOwner) { searchState ->
            when (searchState) {
                is TrackSearchState.Error -> {
                    changeContentVisibility(showCase = ERROR)
                }

                is TrackSearchState.Content -> {
                    tracks.clear()
                    tracks.addAll(searchState.trackList.toMutableList())
                    changeContentVisibility(showCase = SHOW_RESULT)
                }

                is TrackSearchState.Loading -> {
                    changeContentVisibility(showCase = LOADING)
                }
            }
        }

        viewModel.getPlayTrackTrigger().observe(viewLifecycleOwner) { track ->
            playTrack(track)
        }

        viewModel.getSearchHistoryState().observe(viewLifecycleOwner) { state ->
            if (state) {
                changeHistoryVisibility(flag = true)
            } else {
                changeHistoryVisibility(flag = false)
            }
        }

        //обработчик нажатия на кнопку очистки строки поиска
        clearButton.setOnClickListener {
            searchBar.setText(EMPTY_STRING)
            clearPlaceholders()
            inputMethodManager?.hideSoftInputFromWindow(searchScreen.windowToken, 0)
            viewModel.onClearButtonChangeListener(false)
            searchBar.clearFocus()
        }

        //очистка истории при нажатии на кнопку "удалить историю"
        historyClearButton.setOnClickListener {
            viewModel.clearHistory()
            historyTracks.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            viewModel.historyClearButtonChangeListener(false)
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                if (searchBar.hasFocus() && s?.isEmpty() == true) viewModel.onSearchTextChanged(true) else viewModel.onSearchTextChanged(
                    false
                )
                viewModel.getSearchText(searchText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        //Условие показа истории если строка поиска в фокусе
        searchBar.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchBar.text.isEmpty()) viewModel.onSearchBarFocusChangeListener(true) else viewModel.onSearchBarFocusChangeListener(
                false
            )
        }

        searchBar.addTextChangedListener(searchTextWatcher)

        //обработка нажатия "ввод" на виртуальной клавиатуре
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.request(searchText)
                true
            }
            false
        }

        //кнопка "Обновить" для отправки повторного запроса в случае ошибки соединения
        refreshButton.setOnClickListener {
            clearPlaceholders()
            viewModel.request(searchText)
        }

    }

    private fun playTrack(track: Track) {
        action = SearchFragmentDirections.actionSearchFragmentToAudioplayerActivity(track)
        findNavController().navigate(
            action
        )
    }

    private fun changeContentVisibility(showCase: String) {
        when (showCase) {
            ERROR -> {
                progressBar.visibility = View.GONE
                inputMethodManager?.hideSoftInputFromWindow(searchScreen.windowToken, 0)
                showSearchError(CONNECTION_ERROR)
            }

            LOADING -> {
                clearPlaceholders()
                progressBar.visibility = View.VISIBLE
            }

            SHOW_RESULT -> {
                clearPlaceholders()
                progressBar.visibility = View.GONE
                recyclerView.adapter = searchAdapter
                recyclerView.visibility = View.VISIBLE

                if (tracks.isNotEmpty() == true) {
                    searchAdapter.notifyDataSetChanged()

                } else {
                    progressBar.visibility = View.GONE
                    showSearchError(SEARCH_ERROR)
                }
            }
        }
    }

    // метод очистки поисковой строки
    fun clearButtonVisibility(s: CharSequence?): Int {
        if (s.isNullOrBlank()) {
            return View.GONE
        }
        clearPlaceholders()
        return View.VISIBLE
    }

    // очистка экрана от плейсхолдеров
    fun clearPlaceholders() {
        placeholderSearchError.visibility = View.GONE
        placeholderServerErrors.visibility = View.GONE
    }

    // метод вывода плейсхолдеров при ошибках поиска
    private fun showSearchError(codeError: String) {
        viewModel.showSearchErrorChangeChangeListener(false)
        if (codeError.equals(SEARCH_ERROR)) {
            placeholderSearchError.visibility = View.VISIBLE
            tracks.clear()
            searchAdapter.notifyDataSetChanged()

        } else {
            placeholderServerErrors.visibility = View.VISIBLE
            tracks.clear()
            searchAdapter.notifyDataSetChanged()

        }
    }

    // метод показа/скрытия истории поиска
    fun changeHistoryVisibility(flag: Boolean) {
        if (flag) {
            historyTracks.clear()
            historyTracks.addAll(viewModel.getHistoryList())
            recyclerView.adapter = searchHistoryAdapter
            searchHistoryAdapter.notifyDataSetChanged()


            if (historyTracks.isNotEmpty()) {
                historyClearButton.visibility = View.VISIBLE
                historyHeader.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
            }

        } else {
            historyClearButton.visibility = View.GONE
            historyHeader.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) {
            bottomNavigationListener = context
        }
    }
    override fun onDetach() {
        super.onDetach()
        bottomNavigationListener = null
    }

    private fun onKeyboardVisibilityChanged(isVisible: Boolean) {
        if (isVisible) {
            bottomNavigationListener?.changeBottomNavBarVisibility(false)
        } else {
            bottomNavigationListener?.changeBottomNavBarVisibility(true)
        }
    }


    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    companion object {
        private const val ERROR = "ERROR"
        private const val SEARCH_ERROR = "SEARCH_ERROR"
        private const val CONNECTION_ERROR = "CONNECTION_ERROR"
        private const val LOADING = "LOADING"
        private const val SHOW_RESULT = "SHOW_RESULT"
    }
}

