package com.example.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCustomerInfoUseCase
import com.example.domain.usecase.UpdateCustomerInfoUseCase
import com.example.model.Customer
import com.example.model.PhoneNumber
import com.example.model.RequestState
import com.example.model.onFailure
import com.example.model.onSuccess
import com.example.profile.model.ProfileUiState
import com.example.model.Country
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.ranges.contains

class ProfileViewModel(
    private val getCustomerInfoUseCase: GetCustomerInfoUseCase,
    private val updateCustomerInfoUseCase: UpdateCustomerInfoUseCase
) : ViewModel() {
    private val _profilUiState =
        MutableStateFlow<ProfileUiState>(ProfileUiState())
    val profileUiState = _profilUiState.asStateFlow()

    var screenState = MutableStateFlow<RequestState<ProfileUiState>>(RequestState.Loading)
        private set

    private val _profileUpdateEffect = Channel<RequestState<Unit>>()
    val profileUpdateEffect = _profileUpdateEffect.receiveAsFlow()

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
    }

    private fun getCustomerInfo() {
        viewModelScope.launch {
            getCustomerInfoUseCase().collectLatest { result ->
                result
                    .onSuccess { customer ->
                        _profilUiState.emit(
                                ProfileUiState(
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
        _profilUiState.update {it.copy(firstName = value)}
    }

    fun updateLastName(value: String) {
        _profilUiState.update {it.copy(lastName = value)}
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
                        countryCode = it.phoneNumber?.countryCode?:0,
                        number = value
                    )
                )
        }
    }

    fun updateCustomer() {
        viewModelScope.launch {
            _profileUpdateEffect.send(RequestState.Loading)
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
                        _profileUpdateEffect.send(RequestState.Success(Unit))
                    }.onFailure {
                        _profileUpdateEffect.send(RequestState.Error(it))
                    }
            }
        }
    }

}