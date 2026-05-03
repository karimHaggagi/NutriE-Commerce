package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCustomerInfoUseCase
import com.example.domain.usecase.SignOutUseCase
import com.example.model.RequestState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeNavigationViewModel(
    private val useCase: SignOutUseCase,
    private val getCustomerInfoUseCase: GetCustomerInfoUseCase
) : ViewModel() {

    val customerInfo = getCustomerInfoUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading
        )
    private val _signOutUserEffect = Channel<RequestState<Unit>>()
    val signOutUserEffect = _signOutUserEffect.receiveAsFlow()


    fun signOut() {
        viewModelScope.launch {
            _signOutUserEffect.send(RequestState.Loading)
            useCase().collect {
                _signOutUserEffect.send(it)
            }
        }
    }
}