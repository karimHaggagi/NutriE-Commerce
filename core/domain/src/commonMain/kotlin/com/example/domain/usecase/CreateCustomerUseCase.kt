package com.example.domain.usecase

import com.example.domain.repository.CustomerRepository
import dev.gitlive.firebase.auth.FirebaseUser

class CreateCustomerUseCase(private val customerRepository: CustomerRepository) {
     operator fun invoke(user: FirebaseUser) = customerRepository.createCustomer(user)
}