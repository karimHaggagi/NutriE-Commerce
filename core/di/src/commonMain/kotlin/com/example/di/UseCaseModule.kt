package com.example.di

import com.example.domain.usecase.AddItemToCartUseCase
import com.example.domain.usecase.CreateCustomerUseCase
import com.example.domain.usecase.CreateOrderUseCase
import com.example.domain.usecase.CreateProductUseCase
import com.example.domain.usecase.DeleteCartItemUseCase
import com.example.domain.usecase.DeleteProductUseCase
import com.example.domain.usecase.GetCustomerInfoUseCase
import com.example.domain.usecase.GetLatestDiscountProductsUseCase
import com.example.domain.usecase.GetLatestNewProductUseCase
import com.example.domain.usecase.GetLatestProductsUseCase
import com.example.domain.usecase.BeginPaymentTransactionUseCase
import com.example.domain.usecase.GetIntentPaymentDataUseCae
import com.example.domain.usecase.GetProductByCategoryUseCase
import com.example.domain.usecase.GetProductByIdUseCase
import com.example.domain.usecase.GetProductsByIdsUseCase
import com.example.domain.usecase.ResetIntentPaymentDataUseCase
import com.example.domain.usecase.SearchQueryByTitleUseCase
import com.example.domain.usecase.SignOutUseCase
import com.example.domain.usecase.UpdateCartItemUseCase
import com.example.domain.usecase.UpdateCustomerInfoUseCase
import com.example.domain.usecase.UpdateProductUseCase
import com.example.domain.usecase.UploadImageUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val UseCaseModule = module {
    singleOf(::CreateCustomerUseCase)
    singleOf(::SignOutUseCase)
    singleOf(::GetCustomerInfoUseCase)
    singleOf(::UpdateCustomerInfoUseCase)
    singleOf(::CreateProductUseCase)
    singleOf(::UploadImageUseCase)
    singleOf(::GetLatestProductsUseCase)
    singleOf(::GetProductByIdUseCase)
    singleOf(::UpdateProductUseCase)
    singleOf(::DeleteProductUseCase)
    singleOf(::SearchQueryByTitleUseCase)
    singleOf(::GetLatestDiscountProductsUseCase)
    singleOf(::GetLatestNewProductUseCase)
    singleOf(::AddItemToCartUseCase)
    singleOf(::GetProductsByIdsUseCase)
    singleOf(::UpdateCartItemUseCase)
    singleOf(::DeleteCartItemUseCase)
    singleOf(::GetProductByCategoryUseCase)
    singleOf(::CreateOrderUseCase)
    singleOf(::BeginPaymentTransactionUseCase)
    singleOf(::GetIntentPaymentDataUseCae)
    singleOf(::ResetIntentPaymentDataUseCase)

}