package com.example.domain.usecase

import com.example.domain.repository.ProductRepository

class GetProductsByIdsUseCase(private val productRepository: ProductRepository){
    operator fun invoke(productIds: Set<String>) = productRepository.getProductsByIds(productIds)
}