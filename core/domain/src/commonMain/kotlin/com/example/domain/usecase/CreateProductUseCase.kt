package com.example.domain.usecase

import com.example.domain.repository.ProductRepository
import com.example.model.Product

class CreateProductUseCase(private val repository: ProductRepository) {
    operator fun invoke(product: Product) = repository.createNewProduct(product)
}