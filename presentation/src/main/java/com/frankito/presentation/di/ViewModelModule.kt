package com.frankito.presentation.di

import com.frankito.presentation.features.pokemondetails.PokemonDetailsViewModel
import com.frankito.presentation.features.pokemonlist.PokemonListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PokemonListViewModel(get(), get(), get(), get()) }
    viewModel { PokemonDetailsViewModel(get(), get()) }
}