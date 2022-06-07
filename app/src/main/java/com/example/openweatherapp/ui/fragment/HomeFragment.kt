package com.example.openweatherapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.openweatherapp.R
import com.example.openweatherapp.databinding.FragmentHomeBinding
import com.example.openweatherapp.ui.adapter.BookmarkedLocationAdapter
import com.example.openweatherapp.ui.adapter.EmptyBookMarkRecycler
import com.example.openweatherapp.ui.viewmodel.WeatherViewModel
import com.example.openweatherapp.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), BookmarkedLocationAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var bookmarkAdapter: BookmarkedLocationAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpUI()
        setObserver()

        weatherViewModel.getAllBookMarkedLocation()
        return root
    }

    override fun onResume() {
        super.onResume()
        binding.searchEditText.text.clear()
    }

    /**
     * method to set up the UI
     */
    private fun setUpUI() {
        binding.searchEditText.setOnEditorActionListener { v, _, _ ->
            v.hideKeyboard()
            weatherViewModel.getCityWeather(binding.searchEditText.text?.toString() ?: "")
            true
        }
        bookmarkAdapter = BookmarkedLocationAdapter(this)
    }

    /**
     * method to set livedata observer
     */
    private fun setObserver() {
        weatherViewModel.cityWeatherResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                handleCityWeatherResult(result)
            }
        }

        weatherViewModel.allBookMarkResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                handleBookMarkResult(result)
            }
        }
    }

    private fun handleBookMarkResult(result: BookMarkState) {
        if (result != BookMarkState.LOADING) binding.progressCircular.hide()
        when (result) {
            BookMarkState.LOADING -> binding.progressCircular.show()
            BookMarkState.BM_PRESENT -> {
                bookmarkAdapter.setItem(weatherViewModel.bookMarkedLocations)
                binding.bookmarkList.adapter = bookmarkAdapter
            }
            BookMarkState.BM_EMPTY -> {
                val bookmarkAdapter = EmptyBookMarkRecycler()
                binding.bookmarkList.adapter = bookmarkAdapter
            }
        }
    }

    private fun handleCityWeatherResult(result: ResponseState) {
        if (result != ResponseState.LOADING) binding.progressCircular.hide()
        when (result) {
            ResponseState.LOADING -> binding.progressCircular.show()
            ResponseState.SUCCESS -> {
                if (safeNavigate()) {
                    findNavController()
                        .navigate(R.id.action_nav_home_to_nav_detail)
                }
            }
            ResponseState.NOT_FOUND -> {
                binding.root.snackbar(getString(R.string.city_not_found))
            }
            ResponseState.API_ERROR -> {
                binding.root.snackbar(getString(R.string.generic_error_msg))
            }
            ResponseState.NO_INTERNET -> {
                binding.root.snackbar(getString(R.string.no_internet))
            }
        }
    }

    private fun safeNavigate(): Boolean {
        return findNavController().currentDestination == findNavController().findDestination(R.id.nav_home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(s: String) {
        binding.root.hideKeyboard()
        weatherViewModel.getCityWeather(s)
    }
}