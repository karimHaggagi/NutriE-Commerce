package com.example.domain.usecase

import com.example.domain.repository.ProductRepository
import com.example.model.map
import kotlinx.coroutines.flow.map

class GetLatestNewProductUseCase(private val productRepository: ProductRepository) {
    operator fun invoke() = productRepository.getLatestProduct()
        .map { response->
            response.map { products ->
                products.filter { it.isNew }
            }
        }}