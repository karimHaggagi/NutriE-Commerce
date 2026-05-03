package com.example.home

import ContentWithMessageBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.adminpanel.navigation.navigateToAdminPanelScreen
import com.example.designsystem.Alpha
import com.example.designsystem.BebasNeueFont
import com.example.designsystem.FontSize
import com.example.designsystem.IconPrimary
import com.example.designsystem.Resources
import com.example.designsystem.Surface
import com.example.designsystem.SurfaceBrand
import com.example.designsystem.SurfaceError
import com.example.designsystem.SurfaceLighter
import com.example.designsystem.TextPrimary
import com.example.designsystem.TextWhite
import com.example.home.component.BottomBar
import com.example.home.component.CustomDrawer
import com.example.home.domain.BottomBarDestination
import com.example.home.domain.CustomDrawerState
import com.example.home.domain.isOpened
import com.example.home.domain.opposite
import com.example.home.navigation.homeNavGraph
import com.example.home.navigation.isTopLevelDestination
import com.example.home.navigation.navigateToTopLevelDestination
import com.example.model.RequestState
import com.example.navigation.routes.CartScreen
import com.example.navigation.routes.CategoryScreen
import com.example.navigation.routes.HomeRoute
import com.example.navigation.routes.ProductOverviewScreen
import com.example.profile.navigation.navigateToProfileScreen
import com.example.utils.getScreenWidth
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNavigationScreen(
    navigateToAuth: () -> Unit
) {
    val viewModel = koinViewModel<HomeNavigationViewModel>()
    val customer by viewModel.customerInfo.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState()
    val currentDestination = currentRoute.value?.destination

    val selectedDestination =
        when {
            currentDestination?.hasRoute<ProductOverviewScreen>() == true -> BottomBarDestination.ProductsOverview
            currentDestination?.hasRoute<CartScreen>() == true -> BottomBarDestination.Cart
            currentDestination?.hasRoute<CategoryScreen>() == true -> BottomBarDestination.Categories
            else -> BottomBarDestination.ProductsOverview
        }

    val screenWidth = remember { getScreenWidth() }
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }

    val offsetValue by remember { derivedStateOf { (screenWidth / 1.5).dp } }
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp
    )


    val animatedBackground by animateColorAsState(
        targetValue = if (drawerState.isOpened()) SurfaceLighter else Surface
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f
    )

    val animatedRadius by animateDpAsState(
        targetValue = if (drawerState.isOpened()) 20.dp else 0.dp
    )
    val messageBarState = rememberMessageBarState()


    LaunchedEffect(Unit) {
        viewModel.signOutUserEffect.collectLatest {
            when (it) {
                is RequestState.Error -> {
                    messageBarState.addError(it.message)
                }

                is RequestState.Success<*> -> {
                    navigateToAuth()
                }

                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBackground)
            .systemBarsPadding()
    ) {
        CustomDrawer(
            customer = customer,
            onProfileClick = {
                drawerState = drawerState.opposite()
                navController.navigateToProfileScreen()
            },
            onContactUsClick = {},
            onSignOutClick = {
                viewModel.signOut()
            },
            onAdminPanelClick = {
                drawerState = drawerState.opposite()
                navController.navigateToAdminPanelScreen()
            }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(size = animatedRadius))
                .offset(x = animatedOffset)
                .scale(scale = animatedScale)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(size = animatedRadius),
                    ambientColor = Color.Black.copy(alpha = Alpha.DISABLED),
                    spotColor = Color.Black.copy(alpha = Alpha.DISABLED)
                )
        ) {

            Scaffold(
                containerColor = Surface,
                topBar = {
                    AnimatedVisibility(
                        visible = currentDestination?.isTopLevelDestination() ?: true
                    ) {
                        CenterAlignedTopAppBar(
                            title = {
                                AnimatedContent(
                                    targetState = selectedDestination
                                ) { destination ->
                                    Text(
                                        text = destination.title,
                                        fontFamily = BebasNeueFont(),
                                        fontSize = FontSize.LARGE,
                                        color = TextPrimary
                                    )
                                }
                            },
                            navigationIcon = {
                                AnimatedContent(
                                    targetState = drawerState
                                ) { drawer ->
                                    if (drawer.isOpened()) {
                                        IconButton(onClick = {
                                            drawerState = drawerState.opposite()
                                        }) {
                                            Icon(
                                                painter = painterResource(Resources.Icon.Close),
                                                contentDescription = "Close icon",
                                                tint = IconPrimary
                                            )
                                        }
                                    } else {
                                        IconButton(onClick = {
                                            drawerState = drawerState.opposite()
                                        }) {
                                            Icon(
                                                painter = painterResource(Resources.Icon.Menu),
                                                contentDescription = "Menu icon",
                                                tint = IconPrimary
                                            )
                                        }
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Surface,
                                scrolledContainerColor = Surface,
                                navigationIconContentColor = IconPrimary,
                                titleContentColor = TextPrimary,
                                actionIconContentColor = IconPrimary
                            )
                        )
                    }
                }
            ) { padding ->
                ContentWithMessageBar(
                    contentBackgroundColor = Surface,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding()
                        ),
                    messageBarState = messageBarState,
                    errorMaxLines = 2,
                    errorContainerColor = SurfaceError,
                    errorContentColor = TextWhite,
                    successContainerColor = SurfaceBrand,
                    successContentColor = TextPrimary
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        NavHost(
                            modifier = Modifier.weight(1f),
                            navController = navController,
                            startDestination = HomeRoute
                        ) {
                            homeNavGraph(
                                navController = navController
                            )
                        }
                        AnimatedVisibility(
                            visible = currentDestination?.isTopLevelDestination() ?: true
                        ) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .padding(all = 12.dp)
                            ) {
                                BottomBar(
                                    customer = customer,
                                    selected = selectedDestination,
                                    onSelect = { destination ->
                                        navigateToTopLevelDestination(
                                            navController = navController,
                                            topLevelDestination = destination
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}