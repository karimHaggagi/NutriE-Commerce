package com.example.network.firebase

import com.example.model.CartItem
import com.example.model.Customer
import com.example.model.RequestState
import com.example.network.CustomerRemoteDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlin.collections.emptyList
import kotlin.collections.mapOf
import kotlin.to

class FirebaseCustomerRemoteDataSource : CustomerRemoteDataSource {

    private val customerCollection = Firebase.firestore.collection("customers")

    override fun createCustomer(user: FirebaseUser) = flow {
        try {
            val customer = Customer(
                id = user.uid,
                firstName = user.displayName?.split(" ")?.firstOrNull() ?: "",
                lastName = user.displayName?.split(" ")?.lastOrNull() ?: "",
                email = user.email ?: ""
            )
            if (!customerCollection.document(user.uid).get().exists) {
                customerCollection.document(user.uid).set(customer)
                customerCollection.document(user.uid)
                    .collection("privateData")
                    .document("role")
                    .set(mapOf("isAdmin" to false))
            }
            emit(RequestState.Success(Unit))
        } catch (e: Exception) {
            //onError(e.localizedMessage ?: "An error occurred")
            emit(RequestState.Error(e.message ?: ""))
        }
    }

    override fun getCurrentUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override fun getCustomerInfo()= channelFlow {
        try {
            val privateDataDocument = customerCollection.document(getCurrentUserId() ?: "xtv3aIAu46V5Hlpq8ENG5zlR1Lv1")
                .collection("privateData")
                .document("role")
                .get()
            customerCollection.document(getCurrentUserId() ?: "xtv3aIAu46V5Hlpq8ENG5zlR1Lv1")
                .snapshots
                .collectLatest {document->
                    if(document.exists){
                        val customer = Customer(
                            id = document.id,
                            firstName = document.get(field = "firstName"),
                            lastName = document.get(field = "lastName"),
                            email = document.get(field = "email"),
                            city = document.get(field = "city"),
                            postalCode = document.get(field = "postalCode"),
                            address = document.get(field = "address"),
                            phoneNumber = document.get(field = "phoneNumber"),
                            cart = document.get(field = "cart"),
                            isAdmin = privateDataDocument.get(field = "isAdmin")
                        )
                        send(RequestState.Success(data = customer))
                    } else {
                        send(RequestState.Error("Queried customer document does not exist."))
                    }
                }

        }catch (e: Exception){
            //onError(e.localizedMessage ?: "An error occurred")
            send(RequestState.Error(e.message ?: ""))
        }
    }

    override fun updateCustomerInfo(customer: Customer)= flow {
        try {
            customerCollection.document(customer.id)
                .update(
                    "firstName" to customer.firstName,
                        "lastName" to customer.lastName,
                        "email" to customer.email,
                        "city" to customer.city,
                        "postalCode" to customer.postalCode,
                        "address" to customer.address,
                        "phoneNumber" to customer.phoneNumber,
                )
            emit(RequestState.Success(Unit))
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }

    override fun addItemToCard(cartItem: CartItem) = flow {
       try {
           val currentUserId = getCurrentUserId() ?: throw Exception("User is not authenticated.")
           val customerDocRef = customerCollection.document(currentUserId)
           val customerSnapshot = customerDocRef.get()
           if (customerSnapshot.exists) {
               val existingCart = customerSnapshot.get<List<CartItem>>("cart") ?: emptyList()
               val updatedCart = existingCart + cartItem
               customerDocRef
                   .set(mapOf("cart" to updatedCart), merge = true)
               emit(RequestState.Success(Unit))
           }

       } catch (e: Exception) {
           emit(RequestState.Error(e.message ?: ""))
       }
    }

    override fun updateCartItemQuantity(cartItem: CartItem)= flow {
        try {
            val currentUserId = getCurrentUserId() ?: throw Exception("User is not authenticated.")
            val customerDocRef = customerCollection.document(currentUserId)
            val customerSnapshot = customerDocRef.get()
            if (customerSnapshot.exists) {
                val existingCart = customerSnapshot.get<List<CartItem>>("cart") ?: emptyList()
                val updatedCart = existingCart.map { cart->
                    if(cart.id == cartItem.id){
                        cart.copy(quantity = cartItem.quantity)
                    } else {
                        cart
                    }
                }
                customerDocRef
                    .update(mapOf("cart" to updatedCart))
                emit(RequestState.Success(Unit))
            }
        }catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }

    override fun deleteCartItem(cartItem: CartItem) = flow {
        try {
            val currentUserId = getCurrentUserId() ?: throw Exception("User is not authenticated.")
            val customerDocRef = customerCollection.document(currentUserId)
            val customerSnapshot = customerDocRef.get()
            if (customerSnapshot.exists) {
                val existingCart = customerSnapshot.get<List<CartItem>>("cart") ?: emptyList()
                val updatedCart = existingCart.filterNot { it.id == cartItem.id }
                customerDocRef
                    .update(mapOf("cart" to updatedCart))
                emit(RequestState.Success(Unit))
            }
        }catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }

    override fun deleteAllCartItem()= flow {
        try {
            val currentUserId = getCurrentUserId() ?: throw Exception("User is not authenticated.")
            val customerDocRef = customerCollection.document(currentUserId)
            val customerSnapshot = customerDocRef.get()
            if (customerSnapshot.exists) {
                customerDocRef
                    .update(mapOf("cart" to emptyList<CartItem>()))
                emit(RequestState.Success(Unit))
            }
        }catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }

    override fun signOut() = flow {
        try {
            Firebase.auth.signOut()
            emit(RequestState.Success(Unit))
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }
}