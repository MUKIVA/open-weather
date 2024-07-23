package com.github.mukiva.feature.splash.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.KEY_ARGS
import com.github.mukiva.core.ui.getArgs
import com.github.mukiva.core.ui.gone
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.core.ui.visible
import com.github.mukiva.feature.splash.R
import com.github.mukiva.feature.splash.databinding.FragmentGetAccessTemplateBinding
import com.github.mukiva.feature.splash.presentation.OnboardingScreenState
import com.github.mukiva.feature.splash.presentation.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
internal class GetAccessTemplateFragment : Fragment(R.layout.fragment_get_access_template) {

    @Parcelize
    data class Args(
        @DrawableRes val imageRes: Int,
        @StringRes val description: Int,
        val permission: String
    ) : Parcelable

    private val mBinding by viewBindings(FragmentGetAccessTemplateBinding::bind)
    private val mViewModel by viewModels<OnboardingViewModel>(
        ownerProducer = { requireParentFragment() }
    )
    private val mRequestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        mViewModel.handlePermissionGranted(getArgs(Args::class.java).permission, isGranted)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initImage(getArgs(Args::class.java).imageRes)
        initDescriptionText(getString(getArgs(Args::class.java).description))
        initActions(getArgs(Args::class.java).permission)
        subscribeOnViewModel()
    }

    private fun subscribeOnViewModel() {
        mViewModel.screenState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach(::updateState)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initImage(@DrawableRes imageRes: Int) = with(mBinding) {
        image.setImageResource(imageRes)
    }

    private fun initDescriptionText(text: String) = with(mBinding) {
        description.text = text
    }

    private fun initActions(permission: String) = with(mBinding) {
        getPermissionButton.setOnClickListener {
            requestPermission(permission)
        }
        skipButton.setOnClickListener {
            mViewModel.nextStep()
        }
    }

    private fun requestPermission(permission: String) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission,
            ) == PackageManager.PERMISSION_GRANTED -> {
                mViewModel.nextStep()
            }
            else -> mRequestPermissionLauncher.launch(permission)
        }
    }

    private fun updateState(state: OnboardingScreenState) = with(mBinding) {
        when (state) {
            OnboardingScreenState.Content -> {
                skipButton.isEnabled = true
                getPermissionButton.isEnabled = true
                progressIndicator.gone()
            }
            OnboardingScreenState.Loading -> {
                skipButton.isEnabled = false
                getPermissionButton.isEnabled = false
                progressIndicator.visible()
            }
        }
    }

    companion object {
        fun newInstance(args: Args) = GetAccessTemplateFragment().apply {
            arguments = bundleOf(KEY_ARGS to args)
        }
    }
}
