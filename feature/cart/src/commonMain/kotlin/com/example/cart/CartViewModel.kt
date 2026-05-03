package com.example.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cart.model.ProductCartEffect
import com.example.domain.usecase.DeleteCartItemUseCase
import com.example.domain.usecase.GetCustomerInfoUseCase
import com.example.domain.usecase.GetProductsByIdsUseCase
import com.example.domain.usecase.UpdateCartItemUseCase
import com.example.model.CartItem
import com.example.model.RequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val getCustomerInfoUseCase: GetCustomerInfoUseCase,
    private val getProductsByIdsUseCase: GetProductsByIdsUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase
) : ViewModel() {

    private val _cartEffects = Channel<ProductCartEffect>()
    val cartEffects = _cartEffects.receiveAsFlow()
    private val customer = getCustomerInfoUseCase()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val products = customer
        .flatMapLatest { customerState ->
            if (customerState.isSuccess()) {
                val productIds = customerState.getSuccessData().cart.map { it.productId }.toSet()
                if (productIds.isNotEmpty()) {
                    getProductsByIdsUseCase(productIds)
                } else flowOf(RequestState.Success(emptyList()))
            } else if (customerState.isError()) {
                flowOf(RequestState.Error(customerState.getErrorMessage()))
            } else flowOf(RequestState.Loading)
        }

    val cartItemWithProduct = combine(customer,products){
        customerState, productsState ->
        when {
            customerState.isSuccess() && productsState.isSuccess() -> {
                val cart = customerState.getSuccessData().cart
                val products = productsState.getSuccessData()

                val result = cart.mapNotNull { cartItem ->
                    val product = products.find { it.id == cartItem.productId }
                    product?.let { cartItem to it }
                }

                RequestState.Success(result)
            }

            customerState.isError() -> RequestState.Error(customerState.getErrorMessage())
            productsState.isError() -> RequestState.Error(productsState.getErrorMessage())

            else -> RequestState.Loading
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        RequestState.Loading
    )

    val totalAmount = cartItemWithProduct
        .map { cartItemsState ->
        when (cartItemsState) {
            is RequestState.Success -> {
                val cartItemsWithProducts = cartItemsState.getSuccessData()
                cartItemsWithProducts.sumOf { (cartItem, product) ->
                    cartItem.quantity * product.price
                }
            }
            else -> 0.0
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        0.0
    )
    fun updateCartItemQuantity(cartItem: CartItem, quantity: Int) {
        viewModelScope.launch {
            updateCartItemUseCase(cartItem.copy(quantity = quantity))
                .collectLatest {
                    if (it.isSuccess()) {
                        _cartEffects.send(ProductCartEffect.ShowSuccessMessage("Quantity updated successfully"))
                    } else if (it.isError()) {
                        _cartEffects.send(ProductCartEffect.ShowErrorMessage(it.getErrorMessage()))
                    }
                }
        }
    }
    fun deleteCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            deleteCartItemUseCase(cartItem)
                .collectLatest {
                    if (it.isSuccess()) {
                        _cartEffects.send(ProductCartEffect.ShowSuccessMessage("Item removed from cart"))
                    } else if (it.isError()) {
                        _cartEffects.send(ProductCartEffect.ShowErrorMessage(it.getErrorMessage()))
                    }
                }
        }
    }

}