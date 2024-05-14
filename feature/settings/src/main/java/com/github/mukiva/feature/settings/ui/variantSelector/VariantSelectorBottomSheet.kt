package com.github.mukiva.feature.settings.ui.variantSelector

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.uiLazy
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.feature.settings.R
import com.github.mukiva.feature.settings.databinding.BottomsheetVariantSelectorBinding
import com.github.mukiva.feature.settings.presentation.BottomSheetState
import com.github.mukiva.feature.settings.presentation.SettingsState
import com.github.mukiva.feature.settings.presentation.SettingsViewModel
import com.github.mukiva.feature.settings.ui.adapter.asLocalTitle
import com.github.mukiva.feature.settings.ui.adapter.asLocalVariants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class VariantSelectorBottomSheet : BottomSheetDialogFragment(R.layout.bottomsheet_variant_selector) {

    private val mBinding by viewBindings(BottomsheetVariantSelectorBinding::bind)
    private val mViewModel by viewModels<SettingsViewModel>(
        ownerProducer = { requireParentFragment() }
    )
    private val mAdapter by uiLazy {
        VariantAdapter(
            selectedPosition = { mSelectedPosition },
            onVariantSelect = mViewModel::commitSelection
        )
    }
    private var mSelectedPosition: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        subscribeOnViewModel()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        mViewModel.closeBottomSheet()
    }

    private fun initList() {
        mBinding.variantList.adapter = mAdapter
    }
    private fun subscribeOnViewModel() {
        mViewModel.state
            .flowWithLifecycle(lifecycle)
            .filterIsInstance<SettingsState.Content>()
            .map { state -> state.bottomSheetState }
            .onEach(::updateState)
            .launchIn(lifecycleScope)
    }

    private fun updateState(state: BottomSheetState) {
        when (state) {
            BottomSheetState.Hide -> { dismiss() }
            is BottomSheetState.Show -> {
                mBinding.header.text = asLocalTitle(state.key)
                mSelectedPosition = state.selectedPosition
                mAdapter.submitList(asLocalVariants(state.key))
            }
        }
    }

    companion object {
        const val TAG = "VARIANT_SELECTOR"
        fun newInstance() = VariantSelectorBottomSheet()
    }
}
