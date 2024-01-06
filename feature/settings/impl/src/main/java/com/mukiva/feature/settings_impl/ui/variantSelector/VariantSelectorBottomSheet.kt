package com.mukiva.feature.settings_impl.ui.variantSelector

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mukiva.feature.settings_impl.R
import com.mukiva.feature.settings_impl.databinding.BottomsheetVariantSelectorBinding
import com.mukiva.feature.settings_impl.domain.SettingVariantList
import com.mukiva.feature.settings_impl.domain.SettingVariant
import com.mukiva.openweather.ui.getComaptSerializable
import com.mukiva.openweather.ui.uiLazy
import com.mukiva.openweather.ui.viewBindings


class VariantSelectorBottomSheet : BottomSheetDialogFragment(R.layout.bottomsheet_variant_selector) {

    private var mCallbacks: VariantSelectorCallbacks? = null
    private val mBinding by viewBindings(BottomsheetVariantSelectorBinding::bind)
    private val mAdapter by uiLazy {
        VariantListAdapter(
            onVariantSelect = {
                mCallbacks?.onItemSelect(it)
                dismiss()
            }
        )
    }

    private val mState by uiLazy {
        requireArguments().getComaptSerializable(
            ARG_SELECT_VARIANT_STATE,
            SettingVariantList::class.java
        )!!
    }
    interface VariantSelectorCallbacks {
        fun onItemSelect(variant: SettingVariant)
        fun onCancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initTitle()
        initList()

    }

    override fun onCancel(dialog: DialogInterface) {
        mCallbacks?.onCancel()
        super.onCancel(dialog)
    }

    fun addSelectorCallbacks(callbacks: VariantSelectorCallbacks) {
        mCallbacks = callbacks
    }

    private fun initTitle() = with(mBinding) {
        toolbar.title = mState.title
    }

    private fun initList() = with(mBinding) {
        variantList.adapter = mAdapter
        mAdapter.submitList(mState.list)
    }

    companion object {
        const val TAG = "VARIANT_SELECTOR"

        private const val ARG_SELECT_VARIANT_STATE = "ARG_SELECT_VARIANT_STATE"

        fun newInstance(state: SettingVariantList) = VariantSelectorBottomSheet().apply {
            arguments = bundleOf(
                ARG_SELECT_VARIANT_STATE to state
            )
        }

    }

}