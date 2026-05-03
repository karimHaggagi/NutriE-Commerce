package com.example.network.ktor.utils

import com.example.model.RequestState
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException

fun <T,R>Result<T>.map(map:(T)->R): Result<R>{
    return fold(
        onSuccess = { value -> Result.success(map(value)) },
        onFailure = { exception -> Result.failure(exception) }
    )
}


suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): RequestState<T> {
    val response = try {
        execute()
    } catch (e: SocketTimeoutException) {
        return RequestState.Error("Request timed out. Please try again.")
    } catch (e: UnresolvedAddressException) {
        return RequestState.Error("No internet connection. Please check your network settings.")
    } catch (e: Exception) {
        println(e.message)
        return RequestState.Error("An unexpected error occurred. Please try again.")
    }
    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): RequestState<T> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                RequestState.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                RequestState.Error("Failed to parse response. Please try again.")
            }
        }

        408 -> RequestState.Error("Request timed out. Please try again.")
        429 -> RequestState.Error("Too many requests. Please slow down and try again.")
        in 500..599 -> RequestState.Error("Server error. Please try again later.")
        else -> RequestState.Error("An unexpected error occurred. Please try again.")
    }
}