package com.mukiva.core.navigation

import androidx.fragment.app.Fragment
import java.io.Serializable

interface IBaseScreen : Serializable {
    val fragment: Fragment
}