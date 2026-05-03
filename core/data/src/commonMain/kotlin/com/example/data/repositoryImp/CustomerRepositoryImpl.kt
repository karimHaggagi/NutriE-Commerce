package com.example.data.repositoryImp

import com.example.domain.repository.CustomerRepository
import com.example.model.CartItem
import com.example.model.Customer
import com.example.network.CustomerRemoteDataSource
import dev.gitlive.firebase.auth.FirebaseUser

class CustomerRepositoryImpl(private val customerRemoteDataSource: CustomerRemoteDataSource) :
    CustomerRepository {

    override fun createCustomer(user: FirebaseUser) = customerRemoteDataSource.createCustomer(user)
    override fun getCustomerInfo() = customerRemoteDataSource.getCustomerInfo()
    override fun updateCustomerInfo(customer: Customer) = customerRemoteDataSource.updateCustomerInfo(customer)
    override fun addItemToCard(cartItem: CartItem) = customerRemoteDataSource.addItemToCard(cartItem)
    override fun updateCartItemQuantity(cartItem: CartItem) = customerRemoteDataSource.updateCartItemQuantity(cartItem)
    override fun deleteCartItem(cartItem: CartItem) = customerRemoteDataSource.deleteCartItem(cartItem)
    override fun signOut() = customerRemoteDataSource.signOut()
}