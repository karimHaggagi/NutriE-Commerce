package com.example.e_commerce

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform