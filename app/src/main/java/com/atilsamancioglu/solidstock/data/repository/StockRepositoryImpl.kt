package com.atilsamancioglu.solidstock.data.repository

import com.atilsamancioglu.solidstock.data.csv.CsvParser
import com.atilsamancioglu.solidstock.data.csv.IntradayParser
import com.atilsamancioglu.solidstock.data.local.StockDatabase
import com.atilsamancioglu.solidstock.data.mapper.toCompanyInfo
import com.atilsamancioglu.solidstock.data.mapper.toCompanyListing
import com.atilsamancioglu.solidstock.data.mapper.toCompanyListingEntity
import com.atilsamancioglu.solidstock.data.remote.StockAPI
import com.atilsamancioglu.solidstock.domain.model.CompanyInfo
import com.atilsamancioglu.solidstock.domain.model.CompanyListing
import com.atilsamancioglu.solidstock.domain.model.IntradayInfo
import com.atilsamancioglu.solidstock.domain.repository.StockRepository
import com.atilsamancioglu.solidstock.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api : StockAPI,
    private val db : StockDatabase,
    private val companyListingParser : CsvParser<CompanyListing>,
    private val intradayParser : CsvParser<IntradayInfo>

) : StockRepository  {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow{
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if(shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            } catch (e: IOException) {
                emit(Resource.Error(message = "IO Exception"))
                null
            } catch (e: HttpException) {
                emit(Resource.Error(message = "HTTP Exception"))
                null
            }

            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map { it.toCompanyListingEntity() }
                )
                //emit(Resource.Success(listings)) ->
                // commented out bc of single source of data principle. data coming from api->room->ui
                emit(Resource.Success(data=dao.searchCompanyListing("").map { it.toCompanyListing() }))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol = symbol)
            val results = intradayParser.parse(response.byteStream())
            Resource.Success(results)
        } catch (e : IOException) {
            Resource.Error("IO Exception")
        } catch (e : HttpException) {
            Resource.Error("HTTP Exception")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol = symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e : IOException) {
            Resource.Error("IO Exception")
        } catch (e : HttpException) {
            Resource.Error("HTTP Exception")
        }
    }

}