package com.mukiva.current_weather.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.mukiva.current_weather.R
import com.mukiva.current_weather.databinding.FragmentCurrentWeatherBinding
import com.mukiva.current_weather.di.CurrentWeatherComponent
import com.mukiva.current_weather.presentation.CurrentWeatherState
import com.mukiva.current_weather.presentation.CurrentWeatherViewModel
import kotlinx.coroutines.launch

class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather) {

    private val mViewModel: CurrentWeatherViewModel by viewModels {
        CurrentWeatherComponent.get().factory
    }

    private lateinit var mBinding: FragmentCurrentWeatherBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinds(view)
        initButtons()
        observeViewModel()
    }

    private fun initButtons() {
        mBinding.button.setOnClickListener {
            mViewModel.fetchWeather()
        }
    }

    private fun initBinds(view: View) {
        mBinding = FragmentCurrentWeatherBinding.bind(view)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mViewModel.state
                .flowWithLifecycle(lifecycle)
                .collect { updateFragmentState(it) }
        }
    }

    private fun updateFragmentState(state: CurrentWeatherState) {
        mBinding.textView.text = state.title
    }

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }
}