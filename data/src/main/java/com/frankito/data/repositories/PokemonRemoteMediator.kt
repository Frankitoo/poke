package com.frankito.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.frankito.data.api.PokemonApi
import com.frankito.data.api.mappers.toDomainModel
import com.frankito.data.database.dao.PokemonDao
import com.frankito.domain.models.pokemon.PokemonListItem
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val pokemonApi: PokemonApi,
    private val pokemonDao: PokemonDao,
) : RemoteMediator<Int, PokemonListItem>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonListItem>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    lastItem.id
                }
            }

            // If loadKey is zero than it should call refresh
            val response = pokemonApi.getPokemonList(
                limit = state.config.pageSize,
                offset = loadKey ?: 0,
            )

            val pokemonList = response.results.map {
                it.toDomainModel()
            }

            pokemonDao.insertAll(pokemonList)

            MediatorResult.Success(endOfPaginationReached = response.next == null)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}