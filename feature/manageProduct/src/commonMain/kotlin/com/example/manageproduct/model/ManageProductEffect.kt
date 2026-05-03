package com.example.manageproduct.model

sealed interface ManageProductEffect {
    data object ProductAddedSuccessfully : ManageProductEffect
    data object ProductUpdatedSuccessfully : ManageProductEffect
    data object ProductDeletedSuccessfully : ManageProductEffect
    data class Error(val message: String) : ManageProductEffect
}