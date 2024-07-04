package com.github.mukiva.core.ui.component

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class Component {
    abstract fun init()

    interface IStateObserver<VM : ViewModel> {
        fun subscribeOnViewModel(viewModel: VM, lifecycleOwner: LifecycleOwner)
    }
}

class ComponentDelegate<C : Component> internal constructor(
    private val fragment: Fragment,
    private val componentFactory: () -> C
) : ReadOnlyProperty<Fragment, C> {
    private var mComponent: C? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerLiveDataObserver =
                Observer<LifecycleOwner?> {
                    val viewLifecycleOwner = it ?: return@Observer

                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            mComponent = null
                        }
                    })
                }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerLiveDataObserver)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerLiveDataObserver)
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): C {
        val instance = mComponent
        if (instance != null) {
            return instance
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            error("Should not attempt to get bindings when Fragment views are destroyed.")
        }

        return componentFactory().also { mComponent = it }
    }
}

fun <C : Component> Fragment.component(
    factory: () -> C
) = ComponentDelegate(this, factory)
