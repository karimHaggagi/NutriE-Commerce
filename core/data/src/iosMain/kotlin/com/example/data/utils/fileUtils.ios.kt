package com.example.data.utils

import dev.gitlive.firebase.storage.File

actual fun getUri(path: String): File {
    val nsUrl = platform.Foundation.NSURL.fileURLWithPath(path)
    return File(nsUrl)
}