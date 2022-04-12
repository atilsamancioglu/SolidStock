package com.atilsamancioglu.solidstock.data.mapper

import com.atilsamancioglu.solidstock.data.local.CompanyListingEntity
import com.atilsamancioglu.solidstock.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing() : CompanyListing {
    return CompanyListing(
        name, symbol, exchange
    )
}

fun CompanyListing.toCompanyListingEntity() : CompanyListingEntity {
    return CompanyListingEntity(
        name, symbol, exchange
    )
}