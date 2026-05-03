package com.example.network.firebase

import com.example.model.Product
import com.example.model.RequestState
import com.example.network.ProductRemoteDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class FirebaseProductRemoteDataSource : ProductRemoteDataSource {
    private val productsCollection = Firebase.firestore.collection("products")

    override fun createNewProduct(product: Product) = flow {
        try {
            productsCollection.document(product.id).set(product)
            emit(RequestState.Success(Unit))
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }

    override suspend fun uploadImageToStorage(file: File) = flow {
        val storage = Firebase.storage.reference
        val imagePath = storage.child(path = "images/${Uuid.random().toHexString()}")
        try {
            withTimeout(timeMillis = 20000L) {
                imagePath.putFile(file)
                val imageUrl = imagePath.getDownloadUrl()
                emit(RequestState.Success(imageUrl))
            }
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }

    override suspend fun deleteImageFromStorage(imageUrl: String) = flow {
        try {
            val storagePath = extractFirebaseStoragePath(imageUrl)
            if (storagePath != null) {
                Firebase.storage.reference(storagePath).delete()
                emit(RequestState.Success(Unit))
            } else {
                emit(RequestState.Error("Storage Path is null."))
            }
        } catch (e: Exception) {
            emit(RequestState.Error("Error while deleting a thumbnail: $e"))
        }
    }

    override fun getLatestProduct() = channelFlow {
        try {
            productsCollection
                .orderBy("createdAt", Direction.DESCENDING)
                .limit(10)
                .snapshots
                .collectLatest { query ->
                    val products = query.documents.map { documentSnapshot ->
                        Product(
                            id = documentSnapshot.id,
                            createdAt = documentSnapshot.get("createdAt"),
                            title = documentSnapshot.get("title"),
                            description = documentSnapshot.get("description"),
                            thumbnail = documentSnapshot.get("thumbnail"),
                            category = documentSnapshot.get("category"),
                            flavors = documentSnapshot.get("flavors"),
                            weight = documentSnapshot.get("weight"),
                            price = documentSnapshot.get("price"),
                            isNew = documentSnapshot.get("isNew"),
                            isPopular = documentSnapshot.get("isPopular"),
                            isDiscounted = documentSnapshot.get("isDiscounted")
                        )

                    }

                    send(RequestState.Success(products))
                }

        } catch (e: Exception) {
            send(RequestState.Error(e.message ?: ""))
        }
    }

    override fun getProductById(id: String) = flow {
        try {
            val documentSnapshot = productsCollection.document(id).get()

            if (documentSnapshot.exists) {

                emit(
                    RequestState.Success(
                        Product(
                            id = documentSnapshot.id,
                            createdAt = documentSnapshot.get("createdAt"),
                            title = documentSnapshot.get("title"),
                            description = documentSnapshot.get("description"),
                            thumbnail = documentSnapshot.get("thumbnail"),
                            category = documentSnapshot.get("category"),
                            flavors = documentSnapshot.get("flavors"),
                            weight = documentSnapshot.get("weight"),
                            price = documentSnapshot.get("price"),
                            isNew = documentSnapshot.get("isNew"),
                            isPopular = documentSnapshot.get("isPopular"),
                            isDiscounted = documentSnapshot.get("isDiscounted")
                        )
                    )
                )
            } else
                emit(RequestState.Error("Queried product document does not exist."))
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }

    override fun updateProduct(product: Product) = flow {
        try {
            val productDocument = productsCollection.document(product.id)
            if (productDocument.get().exists) {
                productDocument.update(product)
            } else {
                emit(RequestState.Error("Queried product document does not exist."))
            }
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }

    override fun deleteProduct(product: Product) = flow {
        try {
            val productDocument = productsCollection.document(product.id)
            if (productDocument.get().exists) {
                productDocument.delete()
                emit(RequestState.Success(Unit))
            } else {
                emit(RequestState.Error("Queried product document does not exist."))
            }
            emit(RequestState.Success(Unit))
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }

    override fun searchProductByTitle(searchQuery: String) = channelFlow {
        try {
            productsCollection
                .orderBy("title")
                .startAt(searchQuery)
                .endAt("$searchQuery\uf8ff")
                .snapshots
                .collectLatest { query ->
                    val products = query.documents.map { documentSnapshot ->
                        Product(
                            id = documentSnapshot.id,
                            createdAt = documentSnapshot.get("createdAt"),
                            title = documentSnapshot.get("title"),
                            description = documentSnapshot.get("description"),
                            thumbnail = documentSnapshot.get("thumbnail"),
                            category = documentSnapshot.get("category"),
                            flavors = documentSnapshot.get("flavors"),
                            weight = documentSnapshot.get("weight"),
                            price = documentSnapshot.get("price"),
                            isNew = documentSnapshot.get("isNew"),
                            isPopular = documentSnapshot.get("isPopular"),
                            isDiscounted = documentSnapshot.get("isDiscounted")
                        )
                    }
                    send(RequestState.Success(products))
                }
        } catch (e: Exception) {
            send(RequestState.Error(e.message ?: ""))
        }
    }

    override fun getProductsByCategory(category: String) = channelFlow {
        try {
            productsCollection
                .where { "category" equalTo category }
                .snapshots
                .collectLatest {
                    val products = it.documents.map { documentSnapshot ->
                        Product(
                            id = documentSnapshot.id,
                            createdAt = documentSnapshot.get("createdAt"),
                            title = documentSnapshot.get("title"),
                            description = documentSnapshot.get("description"),
                            thumbnail = documentSnapshot.get("thumbnail"),
                            category = documentSnapshot.get("category"),
                            flavors = documentSnapshot.get("flavors"),
                            weight = documentSnapshot.get("weight"),
                            price = documentSnapshot.get("price"),
                            isNew = documentSnapshot.get("isNew"),
                            isPopular = documentSnapshot.get("isPopular"),
                            isDiscounted = documentSnapshot.get("isDiscounted")
                        )
                    }
                    send(RequestState.Success(products))
                }
        }catch (e: Exception){
            send(RequestState.Error(e.message ?: ""))
        }
    }

    override fun getProductsByIds(ids: Set<String>) = flow {
        try {
            val products = ids
                .take(10)
                .mapNotNull { id ->
                val documentSnapshot = productsCollection.document(id).get()
                if (documentSnapshot.exists) {
                    Product(
                        id = documentSnapshot.id,
                        createdAt = documentSnapshot.get("createdAt"),
                        title = documentSnapshot.get("title"),
                        description = documentSnapshot.get("description"),
                        thumbnail = documentSnapshot.get("thumbnail"),
                        category = documentSnapshot.get("category"),
                        flavors = documentSnapshot.get("flavors"),
                        weight = documentSnapshot.get("weight"),
                        price = documentSnapshot.get("price"),
                        isNew = documentSnapshot.get("isNew"),
                        isPopular = documentSnapshot.get("isPopular"),
                        isDiscounted = documentSnapshot.get("isDiscounted")
                    )
                } else null
            }
            emit(RequestState.Success(products))
        } catch (e: Exception) {
            emit(RequestState.Error(e.message ?: ""))
        }
    }

    private fun extractFirebaseStoragePath(downloadUrl: String): String? {
        val startIndex = downloadUrl.indexOf("/o/") + 3
        if (startIndex < 3) return null

        val endIndex = downloadUrl.indexOf("?", startIndex)
        val encodedPath = if (endIndex != -1) {
            downloadUrl.substring(startIndex, endIndex)
        } else {
            downloadUrl.substring(startIndex)
        }

        return decodeFirebasePath(encodedPath)
    }

    private fun decodeFirebasePath(encodedPath: String): String {
        return encodedPath
            .replace("%2F", "/")
            .replace("%20", " ")
    }
}