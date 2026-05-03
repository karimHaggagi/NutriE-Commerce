package com.example.productoverview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.Alpha
import com.example.designsystem.FontSize
import com.example.designsystem.Resources
import com.example.designsystem.TextPrimary
import com.example.designsystem.component.InfoCard
import com.example.designsystem.component.LoadingCard
import com.example.designsystem.component.ProductCard
import com.example.productoverview.component.HorizontalProductCard
import com.example.model.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductOverviewScreen(
    navigateToProductDetails: (String) -> Unit
) {
    val viewModel = koinViewModel<ProductOverviewViewViewModel>()
    val productsState by viewModel.products.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        productsState.DisplayResult(
            onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
            onError = {
                InfoCard(
                    modifier = Modifier.fillMaxSize(),
                    title = "Oops!",
                    subtitle = it,
                    image = Resources.Image.Cat
                )
            },
            onSuccess = { products ->
               Column(modifier = Modifier.fillMaxSize()) {
                   HorizontalProductCard(
                       products = products.newProduct,
                       navigateToProductDetails = navigateToProductDetails
                   )
                   Spacer(modifier = Modifier.height(24.dp))
                   Text(
                       modifier = Modifier
                           .fillMaxWidth()
                           .alpha(Alpha.HALF),
                       text = "Discounted Products",
                       fontSize = FontSize.EXTRA_REGULAR,
                       color = TextPrimary,
                       textAlign = TextAlign.Center
                   )
                   Spacer(modifier = Modifier.height(16.dp))
                   LazyColumn() {
                       items(products.discountProduct) { product ->
                           ProductCard(
                               product = product,
                               onClick = {
                                   navigateToProductDetails(product.id)
                               })
                       }
                   }

               }
            }
        )
    }
}