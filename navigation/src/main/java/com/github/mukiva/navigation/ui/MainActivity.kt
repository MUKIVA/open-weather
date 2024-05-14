package com.github.mukiva.navigation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.github.mukiva.core.ui.uiLazy
import com.github.mukiva.navigation.R
import com.github.mukiva.navigation.domain.IRouter
import com.github.mukiva.navigation.domain.IRouterHolder
import com.github.mukiva.navigation.router.DefaultRouterImpl
import com.github.mukiva.navigation.router.INavigationResourcesProvider
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

        lifecycleHandlerSet.forEach { handler ->
            if (handler is IOnCreateHandler) {
                handler.onCreated(this)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return mRouter.navigateUp() || super.onSupportNavigateUp()
    }
}
