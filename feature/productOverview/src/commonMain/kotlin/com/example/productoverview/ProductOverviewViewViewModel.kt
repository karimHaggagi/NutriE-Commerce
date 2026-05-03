package com.example.productoverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetLatestDiscountProductsUseCase
import com.example.domain.usecase.GetLatestNewProductUseCase
import com.example.model.RequestState
import com.example.productoverview.model.ProductOverviewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProductOverviewViewViewModel(
    private val getLatestDiscountProductsUseCase: GetLatestDiscountProductsUseCase,
    private val getLatestNewProductUseCase: GetLatestNewProductUseCase
) :
    ViewModel() {

   private val discountProducts = getLatestDiscountProductsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Idle
        )

   private val newProducts = getLatestNewProductUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Idle
        )

    val products = combine(
        discountProducts,
        newProducts
    ) { discountProducts, newProducts ->
        if (discountProducts.isLoading() || newProducts.isLoading())
            RequestState.Loading
        else if (discountProducts is RequestState.Success && newProducts is RequestState.Success) {
            RequestState.Success(
                ProductOverviewModel(
                    discountProduct = discountProducts.data,
                    newProduct = newProducts.data
                )
            )
        }
        else if (discountProducts is RequestState.Error && newProducts is RequestState.Error) {
            RequestState.Error(newProducts.message)
        }
        else {
            RequestState.Idle
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Idle
    )
}