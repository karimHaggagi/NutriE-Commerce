package com.example.di

import com.example.local.PaymentDataSource
import com.example.local.PaypalLocalDataSourceImpl
import com.example.network.CheckoutRemoteDataSource
import com.example.network.CustomerRemoteDataSource
import com.example.network.OrderRemoteDataSource
import com.example.network.ProductRemoteDataSource
import com.example.network.firebase.FirebaseCustomerRemoteDataSource
import com.example.network.firebase.FirebaseOrderRemoteDataSource
import com.example.network.firebase.FirebaseProductRemoteDataSource
import com.example.network.ktor.PaypalCheckoutRemoteDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule = module {
    singleOf(::FirebaseCustomerRemoteDataSource) {
        bind<CustomerRemoteDataSource>()
    }
    singleOf(::FirebaseProductRemoteDataSource) {
        bind<ProductRemoteDataSource>()
    }
    singleOf(::FirebaseOrderRemoteDataSource) {
        bind<OrderRemoteDataSource>()
    }
    singleOf(::PaypalCheckoutRemoteDataSource) {
        bind<CheckoutRemoteDataSource>()
    }
    singleOf(::PaypalLocalDataSourceImpl) {
        bind<PaymentDataSource>()
    }
}