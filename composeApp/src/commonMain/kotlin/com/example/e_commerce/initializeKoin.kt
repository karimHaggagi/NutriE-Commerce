package com.example.e_commerce

import com.example.di.UseCaseModule
import com.example.di.dataSourceModule
import com.example.di.networkModule
import com.example.di.platformEngineModule
import com.example.di.repositoryModule
import com.example.di.targetModule
import com.example.di.viewModelModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initializeKoin(
    config:(KoinApplication.()->Unit)?=null
){
    startKoin {
        config?.invoke(this)
        modules(
            listOf(
                viewModelModule,
                UseCaseModule,
                repositoryModule,
                dataSourceModule,
                targetModule,
                networkModule,
                platformEngineModule
            )
        )
    }
}