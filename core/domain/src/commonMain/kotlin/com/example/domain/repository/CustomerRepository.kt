package com.example.domain.repository

import com.example.model.CartItem
import com.example.model.Customer
import com.example.model.RequestState
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun createCustomer(user: FirebaseUser): Flow<RequestState<Unit>>
    fun getCustomerInfo(): Flow<RequestState<Customer>>
    fun updateCustomerInfo(customer: Customer): Flow<RequestState<Unit>>
    fun addItemToCard(cartItem: CartItem): Flow<RequestState<Unit>>
    fun updateCartItemQuantity(cartItem: CartItem): Flow<RequestState<Unit>>
    fun deleteCartItem(cartItem: CartItem): Flow<RequestState<Unit>>
    fun signOut(): Flow<RequestState<Unit>>
}