package com.example.productoverview.model

import com.example.model.Product

data class ProductOverviewModel(
    val newProduct: List<Product> = emptyList(),
    val discountProduct: List<Product> = emptyList()
)