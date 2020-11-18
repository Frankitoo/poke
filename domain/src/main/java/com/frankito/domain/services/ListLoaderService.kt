package com.frankito.domain.services

import kotlinx.coroutines.flow.Flow

interface ListLoaderService {
    val isLoading: Flow<Boolean>
    fun startLoading()
    fun stopLoading()
}