package com.example.domain.usecase

import com.example.domain.repository.ProductRepository

class UploadImageUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(thumbnail: String) = repository.uploadImageToStorage(thumbnail)
}