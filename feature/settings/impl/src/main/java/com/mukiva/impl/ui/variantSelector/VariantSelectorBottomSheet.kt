package com.mukiva.impl.ui.variantSelector

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mukiva.feature.settings_impl.R
import com.mukiva.feature.settings_impl.databinding.BottomsheetVariantSelectorBinding
import com.mukiva.impl.domain.SettingVariant
import com.mukiva.impl.domain.SettingVariantList
import com.mukiva.openweather.ui.getComaptSerializable
import com.mukiva.openweather.ui.uiLazy
import com.mukiva.openweather.ui.viewBindings

class VariantSelectorBottomSheet : BottomSheetDialogFragment(R.layout.bottomsheet_variant_selector) {

    private var mCallbacks: VariantSelectorCallbacks? = null
    private val mBinding by viewBindings(BottomsheetVariantSelectorBinding::bind)
    private val mAdapter by uiLazy {
        VariantListAdapter(
            onVariantSelect = { mCallbacks?.onItemSelect(it) }
        )
    }

    private val mVariantList by uiLazy {
        requireArguments().getComaptSerializable(ARG_VARIANT_LIST, SettingVariantList::class.java)
    }
    interface VariantSelectorCallbacks {
        fun onItemSelect(item: SettingVariant)
        fun onCancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()

    }

    override fun onCancel(dialog: DialogInterface) {
        mCallbacks?.onCancel()
        super.onCancel(dialog)
    }

    fun addSelectorCallbacks(callbacks: VariantSelectorCallbacks) {
        mCallbacks = callbacks
    }

    private fun initList() = with(mBinding) {
        variantList.adapter = mAdapter
        mAdapter.submitList(mVariantList?.list)
    }

    companion object {
        const val TAG = "VARIANT_SELECTOR"

        private const val ARG_VARIANT_LIST = "ARG_VARIANT_LIST"

        fun newInstance(variants: SettingVariantList) = VariantSelectorBottomSheet().apply {
            arguments?.putSerializable(ARG_VARIANT_LIST, variants)
        }

    }

}