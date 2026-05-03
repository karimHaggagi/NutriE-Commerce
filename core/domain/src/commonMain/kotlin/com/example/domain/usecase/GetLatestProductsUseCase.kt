package com.example.domain.usecase

import com.example.domain.repository.ProductRepository

class GetLatestProductsUseCase(private val repository: ProductRepository) {
    operator fun invoke() = repository.getLatestProduct()
}