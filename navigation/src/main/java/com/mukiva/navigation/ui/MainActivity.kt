package com.mukiva.navigation.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.mukiva.navigation.router.DefaultRouterImpl
import com.mukiva.navigation.domain.IRouter
import com.mukiva.navigation.domain.IRouterHolder
import com.mukiva.navigation.R
import com.mukiva.navigation.router.INavigationResourcesProvider
import com.mukiva.core.ui.uiLazy
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IRouterHolder {

    @Inject
    lateinit var routerFactory: DefaultRouterImpl.Factory

    @Inject
    lateinit var destinationResourcesProvider: INavigationResourcesProvider

    @Inject
    lateinit var lifecycleHandlerSet: Set<@JvmSuppressWildcards ILifecycleHandler>

    private val mRouter by uiLazy { routerFactory.create(R.id.navHost) }
    override fun requireRouter(): IRouter {
        return mRouter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_main)
        mRouter.onCreated(this)

        lifecycleHandlerSet.forEach {
            it.onCreated(this)
        }
    }

    override fun onPause() {
        super.onPause()
        mRouter.onPause()
    }

    override fun onStart() {
        super.onStart()
        mRouter.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRouter.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mRouter.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        mRouter.onRestoreInstanceState()
    }

    override fun onSupportNavigateUp(): Boolean {
        return mRouter.navigateUp() || super.onSupportNavigateUp()
    }

}