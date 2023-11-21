package com.mukiva.impl.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mukiva.feature.settings_impl.R
import com.mukiva.feature.settings_impl.databinding.FragmentSettingsTemplateBinding
import com.mukiva.impl.di.ISettingsComponent
import com.mukiva.impl.presentation.SettingsViewModel
import com.mukiva.impl.ui.adapter.SettingsAdapter
import com.mukiva.openweather.ui.uiLazy
import com.mukiva.openweather.ui.viewBindings

class SettingsTemplateFragment : Fragment(R.layout.fragment_settings_template) {

    private val mBinding by viewBindings(FragmentSettingsTemplateBinding::bind)
    private val mAdapter by uiLazy { SettingsAdapter() }
    private val mViewModel : SettingsViewModel by viewModels {
        ISettingsComponent.get().factory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActionBar(getString(requireArguments().getInt(ARG_TITLE_KEY)))
        initList()
    }

    private fun initActionBar(title: String) = with(mBinding) {
        toolbar.title = title
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
        }
    }

    private fun initList() = with(mBinding) {
        settingsList.adapter = mAdapter
    }

    companion object {

        private const val ARG_TITLE_KEY = "ARG_TITLE_KEY"

        fun newInstance(
            @StringRes titleId: Int = R.string.fragment_settings_title
        ) = SettingsTemplateFragment().apply {
            arguments = bundleOf(
                ARG_TITLE_KEY to titleId
            )

        }
    }

}