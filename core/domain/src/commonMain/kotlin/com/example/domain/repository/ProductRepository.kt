package com.example.domain.repository

import com.example.model.Product
import com.example.model.RequestState
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun createNewProduct(product: Product): Flow<RequestState<Unit>>
    suspend fun uploadImageToStorage(thumbnail: String): Flow<RequestState<String>>
    fun getLatestProduct(): Flow<RequestState<List<Product>>>
    fun getProductById(id: String): Flow<RequestState<Product>>
    fun updateProduct(product: Product): Flow<RequestState<Unit>>
    fun deleteProduct(product: Product):Flow<RequestState<Unit>>
    fun searchProductByTitle(searchQuery: String): Flow<RequestState<List<Product>>>
    fun getProductsByIds(ids: Set<String>): Flow<RequestState<List<Product>>>
    fun getProductsByCategory(category: String): Flow<RequestState<List<Product>>>


}
