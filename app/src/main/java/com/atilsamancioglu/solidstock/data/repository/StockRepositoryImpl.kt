package com.atilsamancioglu.solidstock.data.repository

import com.atilsamancioglu.solidstock.data.local.StockDatabase
import com.atilsamancioglu.solidstock.data.mapper.toCompanyListing
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
    val db : StockDatabase
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
                
            } catch (e: IOException) {
                emit(Resource.Error(message = "IO Exception"))
            } catch (e: HttpException) {
                emit(Resource.Error(message = "HTTP Exception"))

            }
        }
    }

}