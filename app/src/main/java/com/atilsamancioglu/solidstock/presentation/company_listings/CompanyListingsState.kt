package com.atilsamancioglu.solidstock.presentation.company_listings

import com.atilsamancioglu.solidstock.domain.model.CompanyListing

data class CompanyListingsState(
    val companies : List<CompanyListing> = emptyList(),
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val searchQuery : String = ""
)
