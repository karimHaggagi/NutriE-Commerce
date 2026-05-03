package com.example.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.category.component.CategoryCard
import com.example.model.ProductCategory

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    navigateToCategorySearch: (ProductCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProductCategory.entries.forEach { category ->
            CategoryCard(
                category = category,
                onClick = { navigateToCategorySearch(category) }
            )
        }
    }
}