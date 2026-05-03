package com.example.data.repositoryImp

import com.example.data.utils.getUri
import com.example.domain.repository.ProductRepository
import com.example.model.Product
import com.example.model.RequestState
import com.example.network.ProductRemoteDataSource
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(private val remoteDataSource: ProductRemoteDataSource) :
    ProductRepository {
    override fun createNewProduct(product: Product) = remoteDataSource.createNewProduct(product)
    override suspend fun uploadImageToStorage(thumbnail: String): Flow<RequestState<String>> {
        val file = getUri(thumbnail)
       return remoteDataSource.uploadImageToStorage(file)
    }

    override fun getLatestProduct() = remoteDataSource.getLatestProduct()
    override fun getProductById(id: String) = remoteDataSource.getProductById(id)
    override fun updateProduct(product: Product) = remoteDataSource.updateProduct(product)
    override fun deleteProduct(product: Product) = remoteDataSource.deleteProduct(product)
    override fun searchProductByTitle(searchQuery: String) = remoteDataSource.searchProductByTitle(searchQuery)
    override fun getProductsByIds(ids: Set<String>) = remoteDataSource.getProductsByIds(ids)
    override fun getProductsByCategory(category: String) = remoteDataSource.getProductsByCategory(category)

}