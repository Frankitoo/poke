package com.frankito.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.frankito.data.api.PokemonApi
import com.frankito.data.api.mappers.toDomainModel
import com.frankito.data.database.dao.PokemonDao
import com.frankito.domain.models.pokemon.PokemonDetail
import com.frankito.domain.models.pokemon.PokemonListItem
import com.frankito.domain.repositories.PokemonRepository
import kotlinx.coroutines.flow.Flow

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

    override suspend fun getPokemon(name: String): PokemonDetail {
        return pokemonApi.getPokemonDetails(name).toDomainModel()
    }
}