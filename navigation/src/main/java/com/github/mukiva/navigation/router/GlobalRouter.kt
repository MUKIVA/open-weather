package com.github.mukiva.navigation.router

import androidx.fragment.app.FragmentActivity
import com.github.mukiva.navigation.domain.IRouter
import com.github.mukiva.navigation.domain.IRouterHolder
import com.github.mukiva.navigation.ui.IOnCreateHandler
import com.github.mukiva.navigation.ui.IOnDestroyHandler
import java.io.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalRouter @Inject constructor() :
    IRouter,
    IOnCreateHandler,
    IOnDestroyHandler {

    private var mActivity: FragmentActivity? = null
    private var mRouterHolder: IRouterHolder? = null

    override fun onCreated(activity: FragmentActivity) {
        require(activity is IRouterHolder) { "Activity must implement IRouterHolder" }

        mActivity = activity
        mRouterHolder = activity
    }

    override fun onDestroy() {
        mActivity = null
    }

    override fun launch(destination: Int, args: Serializable?) {
        val router = mRouterHolder?.requireRouter() ?: return
        router.launch(destination, args)
    }

    override fun navigateUp(): Boolean {
        val router = mRouterHolder?.requireRouter() ?: return false
        return router.navigateUp()
    }
}