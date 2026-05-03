package com.example.productoverview.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.model.Product
import com.example.utils.getScreenWidth
import kotlin.math.absoluteValue

@Composable
fun HorizontalProductCard(
    products: List<Product>,
    navigateToProductDetails: (String) -> Unit

) {

    val pagerState = rememberPagerState(pageCount = { products.size })
    val screenWidth = remember { getScreenWidth() }.dp
    val pageWidth = screenWidth - 120.dp
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 45.dp),
        pageSpacing = 0.dp,
        pageSize = PageSize.Fixed(pageWidth)
    ) { page ->

        // Correct offset calculation (works for last page)

        val pageOffset = pagerState.getOffsetDistanceInPages(page).absoluteValue


        // Scale from 0.85 to 1f
        val scale = lerp(
            start = 0.85f,
            stop = 1f,
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        )

        // Fade side items
        val alpha = lerp(
            start = 0.5f,
            stop = 1f,
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        )

        // Parallax translation (left/right)
        val translationX = lerp(
            start = 0f,     // left/right offset
            stop = 1f,
            fraction = 1f - pageOffset.coerceIn(0f, 1f)
        )

        Card(
            modifier = Modifier
                .height(300.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                    this.translationX = translationX
                }
               // .aspectRatio(16 / 9f)
            ,
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            onClick = {
                navigateToProductDetails(products[page].id)
            }
        ) {
            ProductOverViewCard(
                product = products[page], isSelected = pagerState.currentPage == page
            )
        }
    }
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            // .align(Alignment.BottomCenter)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(16.dp)
            )
        }
    }
}