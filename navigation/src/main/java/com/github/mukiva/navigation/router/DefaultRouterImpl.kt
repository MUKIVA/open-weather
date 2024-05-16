package com.github.mukiva.navigation.router

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.github.mukiva.core.ui.KEY_ARGS
import com.github.mukiva.navigation.R
import com.github.mukiva.navigation.domain.IRouter
import com.github.mukiva.navigation.ui.IOnCreateHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.Serializable

class DefaultRouterImpl @AssistedInject constructor(
    @Assisted @IdRes private val fragmentContainerId: Int,
    private val destinationResourcesProvider: INavigationResourcesProvider,
    private val globalRouter: GlobalRouter,
    private val activity: FragmentActivity
) : IOnCreateHandler, IRouter {

    private val mFragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
            super.onFragmentStarted(fm, f)

            if (activity !is AppCompatActivity) return

            val currentNavController = f.findNavController()
            val appBarConfiguration = AppBarConfiguration(currentNavController.graph)

            if (activity.supportActionBar == null) return

            NavigationUI.setupActionBarWithNavController(activity, currentNavController, appBarConfiguration)
        }
    }

    override fun launch(destination: Int, args: Serializable?, setMainPage: Boolean) {
        requireNavController().apply {
            navigate(
                resId = destination,
                args = Bundle().apply {
                    args?.let { putSerializable(KEY_ARGS, args) }
                },
                navOptions = navOptions {
                    anim {
                        exit = android.R.anim.fade_out
                        enter = android.R.anim.fade_in
                        popExit = android.R.anim.fade_out
                        popEnter = android.R.anim.fade_in

                    }
                    launchSingleTop = true
                    if (setMainPage)
                        popUpTo(this@apply.graph.id) {
                            inclusive = true
                        }
                }
            )
        }
    }

    override fun onCreated(activity: FragmentActivity) {
        setupNavigationGraph()
        setupActionBarConfiguration()
    }

    override fun navigateUp(): Boolean {
        requireNavController().popBackStack()
        return true
    }

    private fun setupActionBarConfiguration() {
        if (activity !is AppCompatActivity) return
        activity.supportFragmentManager.apply {
            registerFragmentLifecycleCallbacks(mFragmentCallbacks, true)
        }
    }

    private fun setupNavigationGraph() {
        requireNavController().setGraph(destinationResourcesProvider.provideNavigationGraph())
    }

    private fun requireNavController(): NavController {
        val fragmentManager = activity.supportFragmentManager
        val navHost = fragmentManager.findFragmentById(fragmentContainerId) as NavHostFragment
        return navHost.navController
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @IdRes fragmentContainerId: Int
        ): DefaultRouterImpl
    }
}
