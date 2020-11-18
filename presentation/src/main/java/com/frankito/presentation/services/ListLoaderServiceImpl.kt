package com.frankito.presentation.services

import com.frankito.domain.services.ListLoaderService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull

class ListLoaderServiceImpl : ListLoaderService {
    @ExperimentalCoroutinesApi
    private var channel: BroadcastChannel<Boolean?> = ConflatedBroadcastChannel(null)

    @FlowPreview
    @ExperimentalCoroutinesApi
    override val isLoading: Flow<Boolean>
        get() = channel.openSubscription().consumeAsFlow().drop(1).filterNotNull()

    @ExperimentalCoroutinesApi
    override fun startLoading() {
        channel.offer(true)
    }

    @ExperimentalCoroutinesApi
    override fun stopLoading() {
        channel.offer(false)
    }
}