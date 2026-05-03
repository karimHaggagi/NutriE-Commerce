package com.example.di

import com.example.network.ktor.utils.HttpClientFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory.create(get()) }
}

expect val platformEngineModule: Module