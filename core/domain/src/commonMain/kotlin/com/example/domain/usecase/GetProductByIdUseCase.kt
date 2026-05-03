package com.example.domain.usecase

import com.example.domain.repository.ProductRepository

class GetProductByIdUseCase(private val repository: ProductRepository) {
    operator fun invoke(id: String) = repository.getProductById(id)
}