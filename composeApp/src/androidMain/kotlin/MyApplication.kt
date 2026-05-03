package com.example.e_commerce

import android.app.Application
import com.example.e_commerce.initializeKoin
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidContext(this@MyApplication)
        }
        FirebaseApp.initializeApp(this)
    }
}