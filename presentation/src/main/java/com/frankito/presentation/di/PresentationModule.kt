package com.frankito.presentation.di

import com.frankito.domain.services.ListLoaderService
import com.frankito.presentation.services.ListLoaderServiceImpl
import org.koin.dsl.module

val presentationModule = module {
    single<ListLoaderService> { ListLoaderServiceImpl() }
}

