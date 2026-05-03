package com.example.profile

import ContentWithMessageBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.*
import com.example.designsystem.component.InfoCard
import com.example.designsystem.component.LoadingCard
import com.example.designsystem.component.PrimaryButton
import com.example.designsystem.component.ProfileForm
import com.example.model.RequestState
import com.example.model.DisplayResult
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileScreen(
    navigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val uiState by viewModel.profileUiState.collectAsStateWithLifecycle()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
//    val screenReady = viewModel.screenReady
//    val screenState = viewModel.screenState
//    val isFormValid = viewModel.isFormValid
    val messageBarState = rememberMessageBarState()

    LaunchedEffect(Unit) {
        viewModel.profileUpdateEffect.collectLatest { effect->
            when(effect){
                is RequestState.Error -> {
                    messageBarState.addError(effect.message)
                }
                is RequestState.Success<*> -> {
                    messageBarState.addSuccess("Successfully updated!")
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Profile",
                        fontFamily = BebasNeueFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back Arrow icon",
                            tint = IconPrimary
                        )
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
    ) { padding ->
        ContentWithMessageBar(
            contentBackgroundColor = Surface,
            modifier = Modifier
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(
                        top = 12.dp,
                        bottom = 24.dp
                    )
                    .imePadding()
            ) {
                screenState.DisplayResult(
                    onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
                    onSuccess = {
                        Column(modifier = Modifier.fillMaxSize()) {
                            ProfileForm(
                                modifier = Modifier.weight(1f),
                                country = uiState.country,
                                onCountrySelect = viewModel::updateCountry,
                                firstName = uiState.firstName,
                                onFirstNameChange = viewModel::updateFirstName,
                                lastName = uiState.lastName,
                                onLastNameChange = viewModel::updateLastName,
                                email = uiState.email,
                                city = uiState.city,
                                onCityChange = viewModel::updateCity,
                                postalCode = uiState.postalCode,
                                onPostalCodeChange = viewModel::updatePostalCode,
                                address = uiState.address,
                                onAddressChange = viewModel::updateAddress,
                                phoneNumber = uiState.phoneNumber?.number,
                                onPhoneNumberChange = viewModel::updatePhoneNumber
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            PrimaryButton(
                                text = "Update",
                                icon = Resources.Icon.Checkmark,
                                enabled = viewModel.isFormValid,
                                onClick = viewModel::updateCustomer
                            )
                        }
                    },
                    onError = { message ->
                        InfoCard(
                            image = Resources.Image.Cat,
                            title = "Oops!",
                            subtitle = message
                        )
                    }
                )
            }
        }
    }
}