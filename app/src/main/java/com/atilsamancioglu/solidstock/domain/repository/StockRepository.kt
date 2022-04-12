package com.atilsamancioglu.solidstock.domain.repository

import com.atilsamancioglu.solidstock.domain.model.CompanyListing
import com.atilsamancioglu.solidstock.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote : Boolean,
        query: String
    ) : Flow<Resource<List<CompanyListing>>>
}