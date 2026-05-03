package com.example

sealed interface AuthScreenEffect{
    data class ShowSuccessMessage(val message: String): AuthScreenEffect
    data class ShowErrorMessage(val message: String): AuthScreenEffect
    data object NavigateToHome: AuthScreenEffect
}