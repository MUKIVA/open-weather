package com.mukiva.navigation.router

import androidx.fragment.app.FragmentActivity
import com.mukiva.navigation.domain.IRouter
import com.mukiva.navigation.domain.IRouterHolder
import com.mukiva.navigation.ui.AbstractLifecycleHandler
import java.io.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalRouter @Inject constructor() : AbstractLifecycleHandler(), IRouter {

    private var mActivity: FragmentActivity? = null
    private var mRouterHolder: IRouterHolder? = null

    override fun onCreated(activity: FragmentActivity) {
        super.onCreated(activity)

        if (activity !is IRouterHolder)
            throw IllegalArgumentException("Activity must implement IRouterHolder")

        mActivity = activity
        mRouterHolder = activity
    }


    override fun onDestroy() {
        super.onDestroy()
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