package com.github.mukiva.feature.settings.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.uiLazy
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.core.ui.visible
import com.github.mukiva.feature.settings.R
import com.github.mukiva.feature.settings.databinding.FragmentSettingsTemplateBinding
import com.github.mukiva.feature.settings.presentation.BottomSheetState
import com.github.mukiva.feature.settings.presentation.SettingsState
import com.github.mukiva.feature.settings.presentation.SettingsViewModel
import com.github.mukiva.feature.settings.ui.adapter.SettingsAdapter
import com.github.mukiva.feature.settings.ui.variantSelector.VariantSelectorBottomSheet
import com.google.android.material.appbar.CollapsingToolbarLayout.TITLE_COLLAPSE_MODE_SCALE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
internal class SettingsTemplateFragment : Fragment(R.layout.fragment_settings_template) {

    private val mViewModel by viewModels<SettingsViewModel>()
    private val mBinding by viewBindings(FragmentSettingsTemplateBinding::bind)
    private val mAdapter by uiLazy {
        SettingsAdapter(
            onSelectVariant = { mViewModel.selectVariant(it.key, it.variants, it.selectedVariant) },
            onToggleOption = { key, isEnabled -> mViewModel.toggle(key, isEnabled) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActionBar()
        initList()
        subscribeOnViewModel()
    }

    private fun subscribeOnViewModel() {
        mViewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach(::updateState)
            .launchIn(lifecycleScope)
    }
    private fun updateState(state: SettingsState) {
        when (state) {
            is SettingsState.Content -> {
                mBinding.settingsList.visible()
                mAdapter.submitList(state.list)
                updateBottomSheetState(state.bottomSheetState)
            }
            SettingsState.Init -> { mViewModel.loadConfiguration() }
        }
    }

    private fun updateBottomSheetState(
        bottomSheetState: BottomSheetState
    ) {
        val oldBottomSheet = childFragmentManager.findFragmentByTag(VariantSelectorBottomSheet.TAG)
        when (bottomSheetState) {
            BottomSheetState.Hide -> {
                (oldBottomSheet as? VariantSelectorBottomSheet)?.dismiss()
            }
            is BottomSheetState.Show -> {
                if (oldBottomSheet != null) return
                val bottomSheet = VariantSelectorBottomSheet.newInstance()
                bottomSheet.show(childFragmentManager, VariantSelectorBottomSheet.TAG)
            }
        }
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
