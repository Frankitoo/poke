package com.frankito.data.api

import com.frankito.data.api.models.response.PokemonDetailResponse
import com.frankito.data.api.models.response.PokemonListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {
    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name")
        name : String) : PokemonDetailResponse
}