package com.example.domain.usecase

import com.example.domain.repository.CustomerRepository

class SignOutUseCase (private val repository: CustomerRepository) {
    operator fun invoke() = repository.signOut()
}