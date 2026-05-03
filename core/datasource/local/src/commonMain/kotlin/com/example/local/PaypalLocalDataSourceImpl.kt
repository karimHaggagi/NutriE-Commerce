package com.example.local

import com.example.model.PaymentData
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.observable.makeObservable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@OptIn(ExperimentalSettingsApi::class)
class PaypalLocalDataSourceImpl : PaymentDataSource {
    private val settings: ObservableSettings = Settings().makeObservable()

    private val IS_SUCCESS = "isSuccess_paypal"
    private val ERROR = "error_paypal"
    private val TOKEN = "token_paypal"
    override fun setPaymentData(
        isSuccess: Boolean?,
        errorMessage: String?,
        token: String?
    ) {
        isSuccess?.let { settings.putBoolean(IS_SUCCESS, it) }
        errorMessage?.let { settings.putString(ERROR, it) }
        token?.let { settings.putString(TOKEN, it) }

    }

    override fun getPaymentData() = callbackFlow {
        fun getCurrentPaymentProcessed(): PaymentData{
            return PaymentData(
                isSuccess =settings.getBooleanOrNull(IS_SUCCESS),
                errorMessage = settings.getStringOrNull(ERROR),
                token = settings.getStringOrNull(TOKEN)
            )
        }

        while (true) {
            this.send(getCurrentPaymentProcessed())
            delay(1000) // Check for updates every second
        }

    }

    override fun reset() {
        settings.clear()
    }

}