package com.example.data.utils

import dev.gitlive.firebase.storage.File
import androidx.core.net.toUri

actual fun getUri(path: String): File {
    val uri = path.toUri()
    return File(uri)
}