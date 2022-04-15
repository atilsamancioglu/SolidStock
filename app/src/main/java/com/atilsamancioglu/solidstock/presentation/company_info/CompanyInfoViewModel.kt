package com.atilsamancioglu.solidstock.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atilsamancioglu.solidstock.domain.repository.StockRepository
import com.atilsamancioglu.solidstock.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(

    //get access to navigation arguments, without passing them from UI to VM
    private val savedStateHandle: SavedStateHandle,
    private val repository : StockRepository

) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            //Since we are going to do two network calls here, we can use async to make it quicker
            // This way it would be independent from other call in coroutine scope
            /*
            val companyInfoResult = repository.getCompanyInfo(symbol = symbol)
            val intradayInfoResult = repository.getIntradayInfo(symbol = symbol)

             */
            val companyInfoResult = async { repository.getCompanyInfo(symbol = symbol) }
            val intradayInfoResult = async { repository.getIntradayInfo(symbol = symbol) }

            when(val result = companyInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(company = result.data, isLoading = false, error = null)
                }

                is Resource.Error -> {
                    state = state.copy(company = null, isLoading = false, error = result.message)
                }

                is Resource.Loading -> {

                }
            }

            when(val result = intradayInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(stockInfos = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    state = state.copy(company = null, isLoading = false, error = result.message)
                }

                is Resource.Loading -> {

                }
            }
        }
    }

}