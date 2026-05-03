package com.example.di

import com.example.manageproduct.utils.PhotoPicker
import org.koin.dsl.module

actual val targetModule = module {
    single { PhotoPicker() }
}
