package com.example.checkout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.checkout.model.CheckoutEffect
import com.example.checkout.model.CheckoutUiState
import com.example.domain.usecase.CreateOrderUseCase
import com.example.domain.usecase.GetCustomerInfoUseCase
import com.example.domain.usecase.BeginPaymentTransactionUseCase
import com.example.domain.usecase.GetIntentPaymentDataUseCae
import com.example.domain.usecase.ResetIntentPaymentDataUseCase
import com.example.domain.usecase.UpdateCustomerInfoUseCase
import com.example.model.Customer
import com.example.model.Order
import com.example.model.PhoneNumber
import com.example.model.RequestState
import com.example.model.onFailure
import com.example.model.onSuccess
import com.example.model.Country
import com.example.navigation.routes.CheckoutScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.ranges.contains

class CheckoutViewModel(
    private val getCustomerInfoUseCase: GetCustomerInfoUseCase,
    private val updateCustomerInfoUseCase: UpdateCustomerInfoUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val beginPaymentTransactionUseCase: BeginPaymentTransactionUseCase,
    private val getIntentPaymentDataUseCae: GetIntentPaymentDataUseCae,
    private val resetIntentPaymentDataUseCase: ResetIntentPaymentDataUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val totalAmount = savedStateHandle.toRoute<CheckoutScreen>().totalAmount
    private val _profilUiState =
        MutableStateFlow<CheckoutUiState>(CheckoutUiState())
    val profileUiState = _profilUiState.asStateFlow()

    var screenState = MutableStateFlow<RequestState<CheckoutUiState>>(RequestState.Idle)
        private set

    private val _checkoutEffect = Channel<CheckoutEffect>()
    val checkoutEffect = _checkoutEffect.receiveAsFlow()

    val isFormValid: Boolean
        get() = with(profileUiState.value) {
            firstName.length in 3..50 &&
                    lastName.length in 3..50 &&
                    city?.length in 3..50 &&
                    postalCode != null || postalCode?.toString()?.length in 3..8 &&
                    address?.length in 3..50 &&
                    phoneNumber?.number?.length in 5..30
        }

    init {
        getCustomerInfo()
        observePaymentStatus()
    }

    private fun observePaymentStatus() {
        viewModelScope.launch {
            getIntentPaymentDataUseCae().collectLatest {
                if (!it.token.isNullOrEmpty()){
                    if (it.isSuccess == true)
                        createOrder(it.token)
                    else
                        _checkoutEffect.send(
                            CheckoutEffect.ShowError(
                                it.errorMessage ?: "Payment failed. Please try again."
                            )
                        )

                    resetIntentPaymentDataUseCase.invoke()
                }
            }
        }
    }

    private fun getCustomerInfo() {
        viewModelScope.launch {

            getCustomerInfoUseCase().collectLatest { result ->
                result
                    .onSuccess { customer ->
                        _profilUiState.emit(
                            CheckoutUiState(
                                id = customer.id,
                                firstName = customer.firstName,
                                lastName = customer.lastName,
                                email = customer.email,
                                city = customer.city,
                                postalCode = customer.postalCode,
                                address = customer.address,
                                country = Country.getCountryByCode(
                                    customer.phoneNumber?.countryCode ?: 0
                                ),
                                phoneNumber = customer.phoneNumber,
                                cartItems = customer.cart
                            )
                        )
                        screenState.emit(RequestState.Success(_profilUiState.value))


                    }.onFailure {
                        screenState.emit(RequestState.Error(it))
                    }
            }

        }
    }

    fun updateFirstName(value: String) {
        _profilUiState.update { it.copy(firstName = value) }
    }

    fun updateLastName(value: String) {
        _profilUiState.update { it.copy(lastName = value) }
    }

    fun updateCity(value: String) {
        _profilUiState.update {
            it.copy(city = value)
        }
    }

    fun updatePostalCode(value: Int?) {
        _profilUiState.update {
            it.copy(postalCode = value)
        }
    }

    fun updateAddress(value: String) {
        _profilUiState.update {
            it.copy(address = value)
        }
    }

    fun updateCountry(value: Country) {
        _profilUiState.update {
            it.copy(country = value)
        }
    }

    fun updatePhoneNumber(value: String) {
        _profilUiState.update {
            it.copy(
                phoneNumber = PhoneNumber(
                    countryCode = it.phoneNumber?.countryCode ?: 0,
                    number = value
                )
            )
        }
    }

    fun updateCustomer() {
        viewModelScope.launch {
            //  _profileUpdateEffect.send(RequestState.Loading)
            updateCustomerInfoUseCase(
                customer = Customer(
                    id = _profilUiState.value.id,
                    firstName = _profilUiState.value.firstName,
                    lastName = _profilUiState.value.lastName,
                    email = _profilUiState.value.email,
                    city = _profilUiState.value.city,
                    postalCode = _profilUiState.value.postalCode,
                    address = _profilUiState.value.address,
                    phoneNumber = _profilUiState.value.phoneNumber,
                )
            ).collectLatest { result ->
                result
                    .onSuccess {
                        // _profileUpdateEffect.send(RequestState.Success(Unit))
                    }.onFailure {
                        //_profileUpdateEffect.send(RequestState.Error(it))
                    }
            }
        }
    }

    fun createOrder(token: String? = null) {
        viewModelScope.launch {
            createOrderUseCase(
                Order(
                    customerId = _profilUiState.value.id,
                    items = _profilUiState.value.cartItems,
                    totalAmount = totalAmount,
                    token = token
                )
            ).collectLatest {
                screenState.value = RequestState.Idle

                if (it.isSuccess()) {
                    // Handle order creation success, e.g., navigate to payment status screen
                    _checkoutEffect.send(CheckoutEffect.NavigateToPaymentStatus(true, null))
                } else {
                    // Handle order creation failure, e.g., show error message
                    _checkoutEffect.send(
                        CheckoutEffect.NavigateToPaymentStatus(
                            false,
                            it.getErrorMessage()
                        )
                    )
                }
            }
        }
    }

    fun payOnDelivery() {
        createOrder()
    }

    fun payWithPayPal() {
        viewModelScope.launch {
            screenState.value = RequestState.Loading
            beginPaymentTransactionUseCase(
                order = Order(
                    customerId = _profilUiState.value.id,
                    items = _profilUiState.value.cartItems,
                    totalAmount = totalAmount
                ),
                customer = Customer(
                    id = _profilUiState.value.id,
                    firstName = _profilUiState.value.firstName,
                    lastName = _profilUiState.value.lastName,
                    email = _profilUiState.value.email,
                    city = _profilUiState.value.city,
                    postalCode = _profilUiState.value.postalCode,
                    address = _profilUiState.value.address,
                    phoneNumber = _profilUiState.value.phoneNumber,
                )
            ).collectLatest { result ->
                screenState.value = RequestState.Idle
                if (result.isSuccess()) {
                    val approvalUrl =
                        result.getSuccessData() // Assuming this returns the approval URL
                    if (approvalUrl.isNotEmpty()) {
                        // Navigate to PayPal payment screen with the approval URL
                        _checkoutEffect.send(CheckoutEffect.OpenExternalLink(approvalUrl))
                    } else {
                        // Handle case where approval URL is not available
                        _checkoutEffect.send(
                            CheckoutEffect.ShowError(
                                "Failed to initiate PayPal transaction."
                            )
                        )
                    }
                } else {
                    // Handle failure in initiating PayPal transaction
                    _checkoutEffect.send(
                        CheckoutEffect.ShowError(
                            result.getErrorMessage()
                        )
                    )
                }
            }
        }
    }

}