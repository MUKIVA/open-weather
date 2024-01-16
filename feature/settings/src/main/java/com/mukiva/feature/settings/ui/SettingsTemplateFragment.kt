package com.mukiva.feature.settings.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.appbar.CollapsingToolbarLayout.TITLE_COLLAPSE_MODE_SCALE
import com.mukiva.feature.settings.R
import com.mukiva.feature.settings.databinding.FragmentSettingsTemplateBinding
import com.mukiva.feature.settings.domain.SettingVariantList
import com.mukiva.feature.settings.domain.SettingVariant
import com.mukiva.feature.settings.presentation.SettingsState
import com.mukiva.feature.settings.presentation.SettingsViewModel
import com.mukiva.feature.settings.ui.adapter.SettingStringResolver
import com.mukiva.feature.settings.ui.adapter.SettingsAdapter
import com.mukiva.feature.settings.ui.variantSelector.VariantSelectorBottomSheet
import com.mukiva.core.ui.uiLazy
import com.mukiva.core.ui.viewBindings
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

        initActionBar()
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

    private fun initActionBar() = with(mBinding) {
        collapsingToolbarLayout.title = getString(R.string.fragment_settings_title)
        collapsingToolbarLayout.titleCollapseMode = TITLE_COLLAPSE_MODE_SCALE
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
        }
    }

    private fun initList() = with(mBinding) {
        settingsList.adapter = mAdapter
    }

}