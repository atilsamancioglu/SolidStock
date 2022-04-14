package com.atilsamancioglu.solidstock.data.csv

import com.atilsamancioglu.solidstock.data.mapper.toIntradayInfo
import com.atilsamancioglu.solidstock.data.remote.dto.IntradayInfoDto
import com.atilsamancioglu.solidstock.domain.model.CompanyListing
import com.atilsamancioglu.solidstock.domain.model.IntradayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntradayParser @Inject constructor(): CsvParser<IntradayInfo> {

    override suspend fun parse(stream: InputStream): List<IntradayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader.readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null
                    val intradayInfoDto = IntradayInfoDto(timestamp = timestamp,
                    close = close.toDouble())
                    intradayInfoDto.toIntradayInfo()
                }.filter {
                    //only get yesterdays
                 it.date.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
                }.sortedBy {
                    it.date.hour
                }.also {
                    csvReader.close()
                }
        }
    }


}