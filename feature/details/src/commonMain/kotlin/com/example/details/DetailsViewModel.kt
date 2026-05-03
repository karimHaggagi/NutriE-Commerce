package com.example.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.domain.usecase.AddItemToCartUseCase
import com.example.domain.usecase.GetProductByIdUseCase
import com.example.model.CartItem
import com.example.model.RequestState
import com.example.model.MIN_QUANTITY
import com.example.navigation.routes.DetailsScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val id = savedStateHandle.toRoute<DetailsScreen>().id?:""

    val productDetails = getProductByIdUseCase(id)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            RequestState.Loading
        )

    private val _addProductEffect = Channel<RequestState<Unit>>()
    val addProductEffect = _addProductEffect.receiveAsFlow()
    var quantity by mutableStateOf(MIN_QUANTITY)
        private set

    var selectedFlavor: String? by mutableStateOf(null)
        private set

    fun updateQuantity(value: Int) {
        quantity = value
    }
    fun updateFlavor(value: String) {
        selectedFlavor = value
    }
    fun addItemToCart() {
        viewModelScope.launch {
            addItemToCartUseCase(
                CartItem(
                    productId = id,
                    flavor = selectedFlavor,
                    quantity = quantity
                )
            ).collectLatest {
                _addProductEffect.send(it)
            }
        }
    }
}