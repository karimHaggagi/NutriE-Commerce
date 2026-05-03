package com.example.manageproduct.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import platform.Foundation.NSURL

actual class PlatformUri(val nsUrl: NSURL?) {
    actual val uriString: String
        get() = nsUrl?.absoluteString ?: ""
}
