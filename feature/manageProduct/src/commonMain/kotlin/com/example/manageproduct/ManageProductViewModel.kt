package com.example.manageproduct

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.domain.usecase.CreateProductUseCase
import com.example.domain.usecase.DeleteProductUseCase
import com.example.domain.usecase.GetProductByIdUseCase
import com.example.domain.usecase.UpdateProductUseCase
import com.example.domain.usecase.UploadImageUseCase
import com.example.manageproduct.model.ManageProductEffect
import com.example.manageproduct.model.ManageProductUiState
import com.example.model.Product
import com.example.model.RequestState
import com.example.model.onFailure
import com.example.model.onSuccess
import com.example.model.ProductCategory
import com.example.navigation.routes.ManageProductScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ManageProductViewModel(
    private val createProductUseCase: CreateProductUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val id = savedStateHandle.toRoute<ManageProductScreen>().id
    var manageProductUiState by mutableStateOf(ManageProductUiState())
        private set

    private val _productEffect = Channel<ManageProductEffect>()
    val productEffect = _productEffect.receiveAsFlow()

    init {
        if (id != null) {
            getProductById(id)
        }
    }

    private fun getProductById(id: String) {
        viewModelScope.launch {
            getProductByIdUseCase(id).collectLatest { requestState ->
                requestState
                    .onSuccess {
                        manageProductUiState = ManageProductUiState(
                            id = it.id,
                            createdAt = it.createdAt,
                            title = it.title,
                            description = it.description,
                            thumbnail = it.thumbnail,
                            category = ProductCategory.valueOf(it.category),
                            flavors = it.flavors?.joinToString(",") ?: "",
                            weight = it.weight,
                            price = it.price,
                            isNew = it.isNew,
                            isPopular = it.isPopular,
                            isDiscounted = it.isDiscounted
                        )
                    }
                    .onFailure {
                        _productEffect.send(ManageProductEffect.Error(it))
                    }
            }
        }
    }

    val isFormValid: Boolean
        get() = manageProductUiState.title.isNotEmpty() &&
                manageProductUiState.description.isNotEmpty() &&
                manageProductUiState.price != 0.0


    fun updateId(value: String) {
        manageProductUiState = manageProductUiState.copy(id = value)
    }

    fun updateCreatedAt(value: Long) {
        manageProductUiState = manageProductUiState.copy(createdAt = value)
    }

    fun updateTitle(value: String) {
        manageProductUiState = manageProductUiState.copy(title = value)
    }

    fun updateDescription(value: String) {
        manageProductUiState = manageProductUiState.copy(description = value)
    }

    fun updateThumbnail(value: String) {
        manageProductUiState = manageProductUiState.copy(thumbnail = value)
    }

    fun updateThumbnailUploaderState(value: RequestState<Unit>) {
        // thumbnailUploaderState = value
    }

    fun updateCategory(value: ProductCategory) {
        manageProductUiState = manageProductUiState.copy(category = value)
    }

    fun updateFlavors(value: String) {
        manageProductUiState = manageProductUiState.copy(flavors = value)
    }

    fun updateWeight(value: Int?) {
        manageProductUiState = manageProductUiState.copy(weight = value)
    }

    fun updatePrice(value: Double) {
        manageProductUiState = manageProductUiState.copy(price = value)
    }

    fun updateNew(value: Boolean) {
        manageProductUiState = manageProductUiState.copy(isNew = value)
    }

    fun updatePopular(value: Boolean) {
        manageProductUiState = manageProductUiState.copy(isPopular = value)
    }

    fun updateDiscounted(value: Boolean) {
        manageProductUiState = manageProductUiState.copy(isDiscounted = value)
    }

    fun createNewProduct() {
        // uploadImageToStorage(manageProductUiState.thumbnail)
        addProduct("")
    }

    fun uploadImageToStorage(thumbnail: String) {
        viewModelScope.launch {
           // _createNewProductEffect.send(RequestState.Loading)
            uploadImageUseCase(thumbnail).collectLatest { result ->
                result
                    .onSuccess {
                        addProduct(it)
                    }
                    .onFailure {
                        _productEffect.send(ManageProductEffect.Error(it))
                    }
            }
        }
    }

    fun addProduct(imageUrl: String) {
        viewModelScope.launch {
            createProductUseCase(
                product = Product(
                    id = manageProductUiState.id,
                    createdAt = manageProductUiState.createdAt,
                    title = manageProductUiState.title,
                    description = manageProductUiState.description,
                    thumbnail = imageUrl,
                    category = manageProductUiState.category.name,
                    flavors = manageProductUiState.flavors.split(','),
                    weight = manageProductUiState.weight,
                    price = manageProductUiState.price,
                    isNew = manageProductUiState.isNew,
                    isPopular = manageProductUiState.isPopular,
                    isDiscounted = manageProductUiState.isDiscounted
                )
            ).collectLatest { result ->
                if (result.isSuccess())
                    _productEffect.send(ManageProductEffect.ProductAddedSuccessfully)
                else
                    _productEffect.send(ManageProductEffect.Error(result.toString()))
            }
        }
    }

    fun updateProduct(){
        viewModelScope.launch {
            updateProductUseCase(
                product = Product(
                    id = manageProductUiState.id,
                    createdAt = manageProductUiState.createdAt,
                    title = manageProductUiState.title,
                    description = manageProductUiState.description,
                    thumbnail = manageProductUiState.thumbnail,
                    category = manageProductUiState.category.name,
                    flavors = manageProductUiState.flavors.split(','),
                    weight = manageProductUiState.weight,
                    price = manageProductUiState.price,
                    isNew = manageProductUiState.isNew,
                    isPopular = manageProductUiState.isPopular,
                    isDiscounted = manageProductUiState.isDiscounted
                )
            ).collectLatest {
                if (it.isSuccess())
                    _productEffect.send(ManageProductEffect.ProductUpdatedSuccessfully)
                else
                    _productEffect.send(ManageProductEffect.Error(it.toString()))
            }
        }
    }

    fun deleteProduct(){
        viewModelScope.launch {
            deleteProductUseCase(
                product = Product(
                    id = manageProductUiState.id,
                    createdAt = manageProductUiState.createdAt,
                    title = manageProductUiState.title,
                    description = manageProductUiState.description,
                    thumbnail = manageProductUiState.thumbnail,
                    category = manageProductUiState.category.name,
                    flavors = manageProductUiState.flavors.split(','),
                    weight = manageProductUiState.weight,
                    price = manageProductUiState.price,
                    isNew = manageProductUiState.isNew,
                    isPopular = manageProductUiState.isPopular,
                    isDiscounted = manageProductUiState.isDiscounted
                )
            ).collectLatest {
                if (it.isSuccess())
                    _productEffect.send(ManageProductEffect.ProductDeletedSuccessfully)
                else
                    _productEffect.send(ManageProductEffect.Error(it.toString()))
            }
        }
    }
}