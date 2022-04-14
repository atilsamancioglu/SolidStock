package com.atilsamancioglu.solidstock.presentation.company_info

import com.atilsamancioglu.solidstock.domain.model.CompanyInfo
import com.atilsamancioglu.solidstock.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos : List<IntradayInfo> = emptyList(),
    val company : CompanyInfo? = null,
    val isLoading : Boolean = false,
    val error : String? = null
)
