package com.atilsamancioglu.solidstock.domain.model

import java.time.LocalDateTime

data class IntradayInfo(
    val date : LocalDateTime,
    val close : Double
)
