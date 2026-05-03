package com.example.network

import com.example.model.Product
import com.example.model.RequestState
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.flow.Flow

interface ProductRemoteDataSource {
    fun createNewProduct(product: Product): Flow<RequestState<Unit>>
    suspend fun uploadImageToStorage(file: File): Flow<RequestState<String>>
    suspend fun deleteImageFromStorage(imageUrl: String): Flow<RequestState<Unit>>
    fun getLatestProduct(): Flow<RequestState<List<Product>>>
    fun getProductById(id: String): Flow<RequestState<Product>>
    fun updateProduct(product: Product): Flow<RequestState<Unit>>
    fun deleteProduct(product: Product):Flow<RequestState<Unit>>
    fun searchProductByTitle(searchQuery: String): Flow<RequestState<List<Product>>>
    fun getProductsByCategory(category: String): Flow<RequestState<List<Product>>>
    fun getProductsByIds(ids: Set<String>): Flow<RequestState<List<Product>>>
}