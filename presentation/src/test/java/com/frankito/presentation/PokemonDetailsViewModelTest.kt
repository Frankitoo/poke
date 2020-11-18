package com.frankito.presentation

import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.frankito.domain.error.ErrorHandler
import com.frankito.domain.models.pokemon.PokemonDetail
import com.frankito.domain.models.pokemon.samples.Pokemons
import com.frankito.domain.repositories.PokemonRepository
import com.frankito.presentation.features.pokemondetails.PokemonDetailsViewModel
import com.google.common.truth.Truth
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineExceptionHandler
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class PokemonDetailsViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val testExceptionHandler = TestCoroutineExceptionHandler()

    private val errorHandler =
        object : ErrorHandler, CoroutineExceptionHandler by testExceptionHandler {}

    @RelaxedMockK
    lateinit var pokemonRepository: PokemonRepository

    @MockK
    lateinit var loadingObserver: Observer<Boolean>

    @MockK
    lateinit var pokemonObserver: Observer<PokemonDetail>

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        clearAllMocks()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Fetch pokemon details should start loading, fetch data and stop loading`() {
        //given

        val loading = mutableListOf<Boolean>()
        val pokemon = slot<PokemonDetail>()

        val model = createModel()

        coEvery { pokemonRepository.fetchPokemon(any()) } returns Pokemons.pokemonDetailModel

        every { pokemonObserver.onChanged(capture(pokemon)) } returns Unit
        every { loadingObserver.onChanged(capture(loading)) } returns Unit

        //when
        model.loadingLiveData.observeForever(loadingObserver)
        model.pokemonDetailLiveData.observeForever(pokemonObserver)

        model.fetchPokemon("balbasaur")

        testDispatcher.advanceUntilIdle()

        //then
        Truth.assertThat(loading.isNotEmpty()).isTrue()
        Truth.assertThat(loading.first()).isTrue()
        Truth.assertThat(pokemon.captured).isEqualTo(Pokemons.pokemonDetailModel)
        Truth.assertThat(loading.last()).isFalse()
    }

    private fun createModel(): PokemonDetailsViewModel = PokemonDetailsViewModel(
        pokemonRepository, errorHandler
    )
}