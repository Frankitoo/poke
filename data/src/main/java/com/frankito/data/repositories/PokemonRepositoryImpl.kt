package com.frankito.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.frankito.data.api.PokemonApi
import com.frankito.data.api.mappers.toDomainModel
import com.frankito.data.database.dao.PokemonDao
import com.frankito.domain.error.exceptions.NetworkException
import com.frankito.domain.error.exceptions.ServerException
import com.frankito.domain.models.pokemon.PokemonDetail
import com.frankito.domain.models.pokemon.PokemonListItem
import com.frankito.domain.repositories.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PokemonRepositoryImpl(
    private val pokemonApi: PokemonApi,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun fetchPokemons(): Flow<PagingData<PokemonListItem>> {
        return Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = false, prefetchDistance = 3),
            remoteMediator = PokemonRemoteMediator(pokemonApi, pokemonDao),
            pagingSourceFactory = { pokemonDao.getPokemons() }
        ).flow
    }

    override suspend fun getPokemon(name: String): PokemonDetail = withContext(Dispatchers.IO) {
        try {
            return@withContext pokemonApi.getPokemonDetails(name).toDomainModel()
        } catch (e: ServerException) {
            throw e
        } catch (e: NetworkException) {
            throw e
        } catch (e:Exception) {
            throw e
        }
    }
}