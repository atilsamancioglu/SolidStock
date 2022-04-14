package com.atilsamancioglu.solidstock.data.mapper

import com.atilsamancioglu.solidstock.data.local.CompanyListingEntity
import com.atilsamancioglu.solidstock.data.remote.dto.CompanyInfoDto
import com.atilsamancioglu.solidstock.domain.model.CompanyInfo
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

fun CompanyInfoDto.toCompanyInfo() : CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}