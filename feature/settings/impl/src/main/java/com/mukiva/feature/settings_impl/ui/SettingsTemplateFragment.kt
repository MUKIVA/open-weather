package com.mukiva.feature.settings_impl.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.appbar.CollapsingToolbarLayout.TITLE_COLLAPSE_MODE_SCALE
import com.mukiva.feature.settings_impl.R
import com.mukiva.feature.settings_impl.databinding.FragmentSettingsTemplateBinding
import com.mukiva.feature.settings_impl.domain.SettingVariantList
import com.mukiva.feature.settings_impl.domain.SettingVariant
import com.mukiva.feature.settings_impl.presentation.SettingsState
import com.mukiva.feature.settings_impl.presentation.SettingsViewModel
import com.mukiva.feature.settings_impl.ui.adapter.SettingStringResolver
import com.mukiva.feature.settings_impl.ui.adapter.SettingsAdapter
import com.mukiva.feature.settings_impl.ui.variantSelector.VariantSelectorBottomSheet
import com.mukiva.openweather.ui.uiLazy
import com.mukiva.openweather.ui.viewBindings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsTemplateFragment : Fragment(R.layout.fragment_settings_template) {

    private val mViewModel by viewModels<SettingsViewModel>()
    private val mBinding by viewBindings(FragmentSettingsTemplateBinding::bind)
    private val mAdapter by uiLazy {
        SettingsAdapter(
            onSelectVariant = {
                val bottomSheet = VariantSelectorBottomSheet.newInstance(
                    state = SettingVariantList(
                        title = with(SettingStringResolver) {
                            requireContext().resolveName(it.group)
                                                            },
                        list = it.variants
                    )
                )
                bottomSheet.addSelectorCallbacks(object : VariantSelectorBottomSheet.VariantSelectorCallbacks {

                    override fun onItemSelect(variant: SettingVariant) {
                        mViewModel.onSelectOptionVariant(it, variant)
                    }

                    override fun onCancel() {}

                })
                bottomSheet.show(
                    requireActivity().supportFragmentManager,
                    VariantSelectorBottomSheet.TAG
                )
            },
            onToggleOption = {
                mViewModel.onToggleOption()
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActionBar(getString(requireArguments().getInt(ARG_TITLE_KEY)))
        initList()
        subscribeOnViewModel()
    }

    private fun subscribeOnViewModel() {
        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.state
                    .collect { updateState(it) }
            }

        }
    }

    private fun updateState(state: SettingsState) {
        mAdapter.submitList(state.settingsList)
    }

    private fun initActionBar(title: String) = with(mBinding) {
        toolbar.title = title
        collapsingToolbarLayout.titleCollapseMode = TITLE_COLLAPSE_MODE_SCALE
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