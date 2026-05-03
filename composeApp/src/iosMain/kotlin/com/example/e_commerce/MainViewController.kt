package com.example.e_commerce

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController(configure = {initializeKoin()}) { App() }