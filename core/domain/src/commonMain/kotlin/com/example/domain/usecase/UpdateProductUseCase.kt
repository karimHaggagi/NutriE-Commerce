package com.example.domain.usecase

import com.example.domain.repository.ProductRepository
import com.example.model.Product

class UpdateProductUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(product: Product) = repository.updateProduct(product)
}