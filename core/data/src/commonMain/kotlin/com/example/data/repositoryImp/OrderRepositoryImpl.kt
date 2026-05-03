package com.example.data.repositoryImp

import com.example.domain.repository.OrderRepository
import com.example.local.PaymentDataSource
import com.example.model.Customer
import com.example.model.Order
import com.example.model.RequestState
import com.example.network.CheckoutRemoteDataSource
import com.example.network.CustomerRemoteDataSource
import com.example.network.OrderRemoteDataSource
import com.example.network.ktor.dto.Amount
import com.example.network.ktor.dto.Name
import com.example.network.ktor.dto.OrderRequest
import com.example.network.ktor.dto.PurchaseUnit
import com.example.network.ktor.dto.Shipping
import com.example.network.ktor.dto.ShippingAddress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class OrderRepositoryImpl(
    private val orderRemoteDataSource: OrderRemoteDataSource,
    private val customerRemoteDataSource: CustomerRemoteDataSource,
    private val checkoutRemoteDataSource: CheckoutRemoteDataSource,
    private val paymentDataSource: PaymentDataSource
) : OrderRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun createOrder(order: Order): Flow<RequestState<Unit>> {

        return orderRemoteDataSource.createOrder(order)
            .flatMapLatest { orderState ->
                if (orderState is RequestState.Success) {
                    customerRemoteDataSource.deleteAllCartItem()
                } else {
                    flow { emit(orderState) }
                }

            }.flatMapLatest {
                flow { emit(it) }
            }

    }

    @OptIn(ExperimentalUuidApi::class)
    override fun beginCheckout(order: Order, customer: Customer) = flow {
        val tokenStatus = checkoutRemoteDataSource.getToken()
        if (tokenStatus.isError())
            emit(RequestState.Error(tokenStatus.getErrorMessage()))
        if (tokenStatus.isSuccess()) {
            val token = tokenStatus.getSuccessData().accessToken
            val uniqueId = Uuid.random().toHexString()

            val orderRequest = OrderRequest(
                purchaseUnits = listOf(
                    PurchaseUnit(
                        referenceId = uniqueId,
                        amount = Amount(
                            currencyCode = "USD",
                            value = order.totalAmount.toString()
                        ),
                        shipping = Shipping(
                            name = Name(fullName = "${customer.firstName} ${customer.lastName}"),
                            address = ShippingAddress(
                                addressLine1 = customer.address ?: "",
                                addressLine2 = "",
                                city = customer.city ?: "",
                                state = customer.city ?: "",
                                postalCode = customer.postalCode?.toString() ?: "",
                                countryCode = "US"
                            )
                        )
                    )
                )
            )

            val checkoutStatus =
                checkoutRemoteDataSource.beginCheckout(orderRequest, token, uniqueId)
            if (checkoutStatus.isSuccess()) {
                emit(
                    RequestState.Success(
                        checkoutStatus.getSuccessData().links.firstOrNull { it.rel == "payer-action" }?.href
                            ?: ""
                    )
                )
            } else {
                emit(RequestState.Error(checkoutStatus.getErrorMessage()))
            }
        } else {
            emit(RequestState.Error("Token is not available."))
        }
    }

    override fun getPaymentData() = paymentDataSource.getPaymentData()
    override fun resetPaymentData() = paymentDataSource.reset()


}