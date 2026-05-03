package com.example.domain.usecase

import com.example.domain.repository.ProductRepository

class GetProductByCategoryUseCase(private val productRepository: ProductRepository) {
    operator fun invoke(category: String) = productRepository.getProductsByCategory(category)
}