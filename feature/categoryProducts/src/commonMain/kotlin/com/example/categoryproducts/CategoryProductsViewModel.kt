package com.example.categoryproducts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.domain.usecase.GetProductByCategoryUseCase
import com.example.model.RequestState
import com.example.navigation.routes.CategoryProductsScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CategoryProductsViewModel(
    private val getProductByCategoryUseCase: GetProductByCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val categoryName = savedStateHandle.toRoute<CategoryProductsScreen>().categoryName
    private val products = getProductByCategoryUseCase(categoryName)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )

    var searchQuery = MutableStateFlow("")
        private set

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredProducts =
        searchQuery
            .debounce(300)
            .flatMapLatest() {
                if (it.isEmpty()) products
                else
                    products.map { productState ->
                        when (productState) {
                            is RequestState.Success -> RequestState.Success(
                                productState.data.filter { product ->
                                    product.title.equals(it, ignoreCase = true)
                                }
                            )
                            else -> productState
                        }
                    }

            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = RequestState.Loading
            )

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }


}