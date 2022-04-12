package com.atilsamancioglu.solidstock.data.repository

import com.atilsamancioglu.solidstock.data.csv.CsvParser
import com.atilsamancioglu.solidstock.data.local.StockDatabase
import com.atilsamancioglu.solidstock.data.mapper.toCompanyListing
import com.atilsamancioglu.solidstock.data.mapper.toCompanyListingEntity
import com.atilsamancioglu.solidstock.data.remote.StockAPI
import com.atilsamancioglu.solidstock.domain.model.CompanyListing
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
    val api : StockAPI,
    val db : StockDatabase,
    val companyListingParser : CsvParser<CompanyListing>
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
                // commented out bc of single source of data. data coming from api->room->ui
                emit(Resource.Success(data=dao.searchCompanyListing("").map { it.toCompanyListing() }))
                emit(Resource.Loading(false))
            }
        }
    }

}