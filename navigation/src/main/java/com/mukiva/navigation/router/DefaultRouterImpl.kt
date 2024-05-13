package com.mukiva.navigation.router

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.mukiva.core.ui.KEY_ARGS
import com.mukiva.navigation.domain.IRouter
import com.mukiva.navigation.ui.IOnCreateHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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

    override fun launch(destination: Int, args: Serializable?) {
        requireNavController().apply {
            if (args == null) {
                navigate(destination)
            } else {
                navigate(
                    resId = destination,
                    args = Bundle().apply {
                        putSerializable(KEY_ARGS, args)
                    }
                )
            }
        }
    }

    override fun onCreated(activity: FragmentActivity) {
        setupNavigationGraph()
        setupActionBarConfiguration()
    }

    override fun navigateUp(): Boolean {
        activity.onBackPressedDispatcher.onBackPressed()
        globalRouter.onCreated(activity)
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
