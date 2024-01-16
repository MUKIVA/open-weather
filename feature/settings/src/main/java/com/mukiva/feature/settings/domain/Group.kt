package com.mukiva.feature.settings.domain

sealed interface Group {
    open class General : Group {
        data object Theme : General()
        data object UnitsType : General()
    
    }
    
}


