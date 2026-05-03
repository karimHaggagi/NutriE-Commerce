package com.example.domain.usecase

import com.example.domain.repository.ProductRepository

class SearchQueryByTitleUseCase(private val productRepository: ProductRepository) {
    operator fun invoke(searchQuery: String) = productRepository.searchProductByTitle(searchQuery)
}