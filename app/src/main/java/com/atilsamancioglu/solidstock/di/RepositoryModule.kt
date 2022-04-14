package com.atilsamancioglu.solidstock.di

import com.atilsamancioglu.solidstock.data.csv.CompanyListingParser
import com.atilsamancioglu.solidstock.data.csv.CsvParser
import com.atilsamancioglu.solidstock.data.csv.IntradayParser
import com.atilsamancioglu.solidstock.data.repository.StockRepositoryImpl
import com.atilsamancioglu.solidstock.domain.model.CompanyListing
import com.atilsamancioglu.solidstock.domain.model.IntradayInfo
import com.atilsamancioglu.solidstock.domain.repository.StockRepository
import com.opencsv.CSVParser
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    //We are injecting interfaces in stockrepositoryimplementation and also companylistingviewmodel
    //Hilt won't know which implementations to use so we should state specific implementations
    //When we inject abstract structures, we can use binds rather than provides, it would be less boilerplate code


    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingParser : CompanyListingParser
    ) : CsvParser<CompanyListing>


    @Binds
    @Singleton
    abstract fun intradayParser(
        intradayParser: IntradayParser
    ) : CsvParser<IntradayInfo>


    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ) : StockRepository

}