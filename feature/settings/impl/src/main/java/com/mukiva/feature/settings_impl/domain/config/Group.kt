package com.mukiva.feature.settings_impl.domain.config

sealed interface Group {
    open class General : Group {
        data object Theme : General()
        data object UnitsType : General()
    
    }
    
}


