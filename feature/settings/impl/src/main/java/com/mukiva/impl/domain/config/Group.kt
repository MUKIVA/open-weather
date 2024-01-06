package com.mukiva.impl.domain.config

sealed interface Group {
    open class General : Group {
        data object Theme : General()
        data object TempUnitsType : General()
        data object SpeedUnitsType : General()
    
    }
    
}


