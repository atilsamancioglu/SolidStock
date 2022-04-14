package com.atilsamancioglu.solidstock.data.mapper

import com.atilsamancioglu.solidstock.data.remote.dto.IntradayInfoDto
import com.atilsamancioglu.solidstock.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun IntradayInfoDto.toIntradayInfo() : IntradayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp,formatter)
    return IntradayInfo(
        localDateTime,
        close
    )
}