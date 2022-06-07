package com.example.openweatherapp.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.openweatherapp.R
import com.example.openweatherapp.data.local.Location
import com.example.openweatherapp.databinding.FragmentDetailBinding
import com.example.openweatherapp.ui.viewmodel.WeatherViewModel
import com.example.openweatherapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherDetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var toolBar: Toolbar
    private var isLocationBookmarked = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpUI()
        setObserver()

        return root
    }

    /**
     * method to set up the UI
     */
    private fun setUpUI() {
        toolBar = requireActivity().findViewById(R.id.toolbar)
        toolBar.title = weatherViewModel.cityWeather.name

        val main = weatherViewModel.cityWeather.main
        val wind = weatherViewModel.cityWeather.wind
        val weather = weatherViewModel.cityWeather.weather[0]

        binding.temperature.text = String.format("%s%s", main.temp.roundToInt(), CELSIUS)
        binding.weatherMain.text = weather.description.replaceFirstChar { it.uppercase() }

        binding.humidity.text = String.format(
            STRING_FORMAT, HUMIDITY,
            main.humidity.toString(), PERCENTAGE
        )
        binding.wind.text = String.format(
            STRING_FORMAT, WIND,
            wind.speed.toString(), MS
        )
        binding.pressure.text = String.format(
            STRING_FORMAT, PRESSURE,
            main.pressure.toString(), HPA
        )
        weatherViewModel.isLocationBookmarked(weatherViewModel.cityWeather.id)
    }

    /**
     * method to set livedata observer
     */
    private fun setObserver() {
        weatherViewModel.addRemoveBookmarkResult.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                handleBookMarkActionResult(result)
            }
        }
        weatherViewModel.isLocationBookmarked.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                if (result != BookMarkState.LOADING) binding.progressCircular.hide()
                when (result) {
                    BookMarkState.LOADING -> binding.progressCircular.show()
                    else -> {
                        isLocationBookmarked = result == BookMarkState.BM_PRESENT
                        updateIcon(
                            if (isLocationBookmarked) BookMarkState.BM_ADDED
                            else BookMarkState.BM_REMOVED
                        )
                    }
                }
            }
        }
    }

    private fun handleBookMarkActionResult(result: BookMarkState) {
        if (result != BookMarkState.LOADING) binding.progressCircular.hide()
        when (result) {
            BookMarkState.LOADING -> binding.progressCircular.show()
            BookMarkState.BM_ADDED -> {
                updateIcon(BookMarkState.BM_ADDED)
                binding.root.snackbar(getString(R.string.added_bookmark_msg))
            }
            BookMarkState.BM_REMOVED -> {
                updateIcon(BookMarkState.BM_REMOVED)
                binding.root.snackbar(getString(R.string.removed_bookmark_msg))
            }
            BookMarkState.SQL_ERROR -> binding.root.snackbar(getString(R.string.generic_error_msg))
            else -> binding.root.snackbar(getString(R.string.generic_error_msg))
        }
    }

    /**
     * method to update the bookmark icon in toolbar
     */
    private fun updateIcon(state: BookMarkState) {
        if (::toolBar.isInitialized) {
            toolBar.also {
                it.menu?.findItem(R.id.action_bookmark)
                    ?.setIcon(
                        when (state) {
                            BookMarkState.BM_ADDED -> R.drawable.ic_bookmark_filled
                            BookMarkState.BM_REMOVED -> R.drawable.ic_bookmark_empty_with_plus
                            else -> R.drawable.ic_bookmark_empty_with_plus
                        }
                    )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_bookmark) {
            val location = Location(
                weatherViewModel.cityWeather.id,
                weatherViewModel.cityWeather.name,
                weatherViewModel.cityWeather.toString()
            )
            weatherViewModel.handleBookmark(location, isLocationBookmarked)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}