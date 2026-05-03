package com.example.manageproduct.utils

import androidx.compose.runtime.Composable

expect class PhotoPicker {
    @Composable
    fun pickPhoto(onPhotoPicked: (PlatformUri) -> Unit)
    fun open()
}