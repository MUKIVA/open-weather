package com.github.mukiva.feature.splash.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mukiva.core.ui.getAttrColor
import com.github.mukiva.core.ui.lazyAdapter
import com.github.mukiva.core.ui.viewBindings
import com.github.mukiva.feature.splash.R
import com.github.mukiva.feature.splash.databinding.FragmentOnboardingBinding
import com.github.mukiva.feature.splash.presentation.OnboardingScreen
import com.github.mukiva.feature.splash.presentation.OnboardingScreenState
import com.github.mukiva.feature.splash.presentation.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.github.mukiva.core.ui.R as CoreUiRes
import com.google.android.material.R as MaterialRes

@AndroidEntryPoint
class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val mViewModel by viewModels<OnboardingViewModel>()
    private val mBindings by viewBindings(FragmentOnboardingBinding::bind)
    private val mBackPressedDispatcherCallback = object : OnBackPressedCallback(enabled = true) {
        override fun handleOnBackPressed() {
            mViewModel.previousStep()
        }
    }

    private val mOnboardingAdapter by lazyAdapter {
        OnboardingAdapter(childFragmentManager, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initViewPager()
        subscribeOnViewModel()

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                mBackPressedDispatcherCallback.remove()
            }
        })
    }

    private fun initToolbar() = with(mBindings) {
        toolbar.setNavigationIconTint(getAttrColor(MaterialRes.attr.colorPrimary))
        toolbar.setNavigationOnClickListener {
            mViewModel.previousStep()
        }
    }

    private fun initViewPager() = with(mBindings) {
        onboarding.isUserInputEnabled = false
        onboarding.adapter = mOnboardingAdapter
    }

    private fun subscribeOnViewModel() {
        mViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach(::updateState)
            .onEach(::updateToolbarState)
            .launchIn(viewLifecycleOwner.lifecycleScope)
        mViewModel.screenState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach(::updateScreenState)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun updateState(state: OnboardingScreen) = with(mBindings) {
        onboarding.setCurrentItem(state.ordinal, true)
    }

    private fun updateScreenState(state: OnboardingScreenState) = with(mBindings) {
        toolbar.isEnabled = state is OnboardingScreenState.Content
    }

    private fun updateToolbarState(state: OnboardingScreen) = with(mBindings) {
        when (state.ordinal) {
            0 -> {
                toolbar.navigationIcon = null
                mBackPressedDispatcherCallback.remove()
            }
            else -> {
                toolbar.setNavigationIcon(CoreUiRes.drawable.ic_arrow_back)
                requireActivity().onBackPressedDispatcher
                    .addCallback(mBackPressedDispatcherCallback)
            }
        }
    }
}
