package com.example.network.ktor

import com.example.model.RequestState
import com.example.network.CheckoutRemoteDataSource
import com.example.network.ktor.PayPalConstant.AUTH_KEY
import com.example.network.ktor.PayPalConstant.BASE_URL
import com.example.network.ktor.dto.OrderRequest
import com.example.network.ktor.dto.OrderResponse
import com.example.network.ktor.dto.PayPalTokenDTO
import com.example.network.ktor.utils.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.util.encodeBase64

class PaypalCheckoutRemoteDataSource(private val httpClient: HttpClient): CheckoutRemoteDataSource {
    override suspend fun getToken(): RequestState<PayPalTokenDTO> {
        val authKey = AUTH_KEY.encodeBase64()
        return safeCall<PayPalTokenDTO> {
            httpClient.post(TOKEN) {
                headers {
                    append(HttpHeaders.Authorization, "Basic $authKey")
                    append(
                        HttpHeaders.ContentType,
                        ContentType.Application.FormUrlEncoded.toString()
                    )
                }
                setBody("grant_type=client_credentials")
            }
        }
    }

    override suspend fun beginCheckout(
        order: OrderRequest,
        token: String,
        uniqueId: String
    ): RequestState<OrderResponse> {
        return safeCall<OrderResponse> {
            httpClient.post(ORDER) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                    contentType(ContentType.Application.Json)
                    append("PayPal-Request-Id", uniqueId)
                }
                setBody(order)
            }

        }
    }

    companion object{
        const val TOKEN = "$BASE_URL/v1/oauth2/token"
        const val ORDER = "$BASE_URL/v2/checkout/orders"
    }
}