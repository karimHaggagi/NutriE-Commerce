package com.example.manageproduct.utils

import android.net.Uri

actual class PlatformUri(val uri: Uri?) {
    actual val uriString: String
        get() = uri.toString()
}