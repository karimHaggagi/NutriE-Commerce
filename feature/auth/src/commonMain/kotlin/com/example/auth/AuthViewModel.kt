package com.example.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.AuthScreenEffect
import com.example.auth.Constants.WEB_CLIENT_ID
import com.example.domain.usecase.CreateCustomerUseCase
import com.example.model.onFailure
import com.example.model.onSuccess
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val createCustomerUseCase: CreateCustomerUseCase) : ViewModel() {
    private val _createCustomerEffect = Channel<AuthScreenEffect>()
    val createCustomerEffect = _createCustomerEffect.receiveAsFlow()

    init {
        GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = WEB_CLIENT_ID))

    }
    fun createCustomer(user: FirebaseUser?) {

        viewModelScope.launch {
            if (user != null) {
                createCustomerUseCase(user).collectLatest { result ->
                    result
                        .onSuccess {
                            _createCustomerEffect.send(AuthScreenEffect.ShowSuccessMessage("Authentication successful!"))
                            delay(2000)
                            _createCustomerEffect.send(AuthScreenEffect.NavigateToHome)
                        }
                        .onFailure { e ->
                            _createCustomerEffect.send(AuthScreenEffect.ShowErrorMessage(e)
                            )

                        }
                }
            }else{
                _createCustomerEffect.send(AuthScreenEffect.ShowErrorMessage("User is null"))
            }
        }
    }

    fun handleFirebaseAuthResponse(result: Result<FirebaseUser?>) {
        viewModelScope.launch {
            result.onSuccess { user ->
                createCustomer(user = user)
            }.onFailure { error ->
                if (error.message?.contains("A network error") == true) {
                    _createCustomerEffect.send(
                        AuthScreenEffect.ShowErrorMessage(("Internet connection unavailable."))
                    )
                } else if (error.message?.contains("Idtoken is null") == true) {
                    _createCustomerEffect.send(
                        AuthScreenEffect.ShowErrorMessage(("Sign in canceled."))
                    )
                } else {
                    _createCustomerEffect.send(
                        AuthScreenEffect.ShowErrorMessage((error.message ?: "Unknown"))
                    )
                }
            }
        }
    }

}