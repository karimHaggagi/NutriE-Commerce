package com.example.cart

import ContentWithMessageBar
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cart.component.CartItemCard
import com.example.cart.model.ProductCartEffect
import com.example.designsystem.*
import com.example.designsystem.component.InfoCard
import com.example.designsystem.component.LoadingCard
import com.example.model.DisplayResult
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    navigateToCheckout: (Double) -> Unit
) {
    val viewModel = koinViewModel<CartViewModel>()
    val cartItem by viewModel.cartItemWithProduct.collectAsStateWithLifecycle()
    val totalAmount by viewModel.totalAmount.collectAsStateWithLifecycle()
    val messageBarState = rememberMessageBarState()

    LaunchedEffect(Unit) {
        viewModel.cartEffects.collectLatest {
            when (it) {
                is ProductCartEffect.ShowErrorMessage -> messageBarState.addError(it.message)
                is ProductCartEffect.ShowSuccessMessage -> messageBarState.addSuccess(it.message)
            }
        }
    }
    ContentWithMessageBar(
        contentBackgroundColor = Surface,
        messageBarState = messageBarState,
        errorMaxLines = 2,
        errorContainerColor = SurfaceError,
        errorContentColor = TextWhite,
        successContainerColor = SurfaceBrand,
        successContentColor = TextPrimary
    ) {
        cartItem.DisplayResult(
            onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
            onSuccess = { data ->
                Scaffold(
                    floatingActionButton = {
                        if (data.isNotEmpty())
                            FloatingActionButton(
                                onClick = { navigateToCheckout(totalAmount) },
                                containerColor = ButtonPrimary,
                                contentColor = IconPrimary,
                                content = {
                                    Icon(
                                        painter = painterResource(Resources.Icon.RightArrow),
                                        contentDescription = "Right icon",
                                        tint = IconPrimary
                                    )
                                }
                            )
                    }
                ) {

                    if (data.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = data,
                                key = { it.first.id }
                            ) { pair ->
                                CartItemCard(
                                    cartItem = pair.first,
                                    product = pair.second,
                                    onMinusClick = { quantity ->
                                        viewModel.updateCartItemQuantity(
                                            cartItem = pair.first,
                                            quantity = quantity
                                        )
                                    },
                                    onPlusClick = { quantity ->
                                        viewModel.updateCartItemQuantity(
                                            cartItem = pair.first,
                                            quantity = quantity
                                        )
                                    },
                                    onDeleteClick = {
                                        viewModel.deleteCartItem(cartItem = pair.first)
                                    }
                                )
                            }
                        }
                    } else {
                        InfoCard(
                            image = Resources.Image.ShoppingCart,
                            title = "Empty Cart",
                            subtitle = "Check some of our products."
                        )
                    }
                }
            },
            onError = { message ->
                InfoCard(
                    image = Resources.Image.Cat,
                    title = "Oops!",
                    subtitle = message
                )
            },
            transitionSpec = fadeIn() togetherWith fadeOut()
        )
    }

}