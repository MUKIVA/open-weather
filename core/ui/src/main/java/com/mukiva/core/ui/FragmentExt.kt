package com.mukiva.core.ui

import android.util.TypedValue
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mukiva.openweather.ui.getComaptSerializable
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

const val KEY_ARGS = "KEY_ARGS"


fun <T : Serializable> Fragment.getArgs(clazz: Class<T>): T {
    return this.requireArguments().getComaptSerializable(KEY_ARGS, clazz) as T
}

fun Fragment.getAttrColor(res: Int): Int {
    val typedValue = TypedValue()
    val typedArray = requireContext().obtainStyledAttributes(typedValue.data, intArrayOf(res))
    val color = typedArray.getColor(0, 0)
    typedArray.recycle()
    return color
}

class FragmentViewBindingDelegate<T : ViewBinding>(
    val fragment: Fragment,
    val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerLiveDataObserver =
                Observer<LifecycleOwner?> {
                    val viewLifecycleOwner = it ?: return@Observer

                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            binding = null
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

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = binding
        if (binding != null) {
            return binding
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }

        return viewBindingFactory(thisRef.requireView()).also { this.binding = it }
    }
}

fun <T : ViewBinding> Fragment.viewBindings(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

fun <T : ViewBinding> BottomSheetDialogFragment.viewBindings(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

@MainThread
inline fun <reified T> uiLazy(
    noinline objProvider: () -> T
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) { objProvider() }
}

