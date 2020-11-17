package com.frankito.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frankito.domain.models.pokemon.PokemonListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Query("SELECT * FROM PokemonListItem ORDER BY id ASC")
    fun getPokemons(): PagingSource<Int, PokemonListItem>

    @Query("SELECT * FROM PokemonListItem WHERE id = :id")
    fun getPokemon(id: Long): Flow<PokemonListItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PokemonListItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PokemonListItem>)

    @Query("DELETE FROM PokemonListItem")
    suspend fun deleteAll()
}
