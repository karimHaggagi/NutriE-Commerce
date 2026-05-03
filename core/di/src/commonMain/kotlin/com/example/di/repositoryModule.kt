package com.example.di

import com.example.domain.repository.CustomerRepository
import com.example.data.repositoryImp.CustomerRepositoryImpl
import com.example.domain.repository.OrderRepository
import com.example.domain.repository.ProductRepository
import com.example.data.repositoryImp.ProductRepositoryImpl
import com.example.data.repositoryImp.OrderRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::CustomerRepositoryImpl) { bind<CustomerRepository>() }
    singleOf(::ProductRepositoryImpl) { bind<ProductRepository>() }
    singleOf(::OrderRepositoryImpl) { bind<OrderRepository>() }
}
