package com.frankito.presentation.features.pokemonlist

import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frankito.presentation.R
import com.frankito.presentation.ui.BaseFragment
import com.frankito.presentation.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.viewmodel.ext.android.viewModel


class PokemonListFragment : BaseFragment<PokemonListViewModel>() {

    companion object {
        const val SPAN_COUNT = 3
    }

    override val layoutRes: Int = R.layout.fragment_pokemon_list

    override val viewModel: PokemonListViewModel by viewModel()

    private val pokemonListAdapter = PokemonListAdapter()

    private val gridDecoration: RecyclerView.ItemDecoration by lazy {
        GridSpacingItemDecoration(
            SPAN_COUNT, resources.getDimension(R.dimen.margin_grid).toInt(), false
        )
    }

    override fun setupViews() {
        initAdapter()

        swipe_refresh.setOnRefreshListener { pokemonListAdapter.refresh() }
    }

    private fun initAdapter() {
        pokemonListAdapter.onClickListener = { item -> viewModel.onPokemonSelected(item) }

        rvPokemons.addItemDecoration(gridDecoration)
        rvPokemons.layoutManager = GridLayoutManager(activity, SPAN_COUNT)
        rvPokemons.adapter = pokemonListAdapter.withLoadStateHeaderAndFooter(
            header = PokemonLoadingAdapter { pokemonListAdapter.retry() },
            footer = PokemonLoadingAdapter { pokemonListAdapter.retry() }
        )

        lifecycleScope.launchWhenCreated {
            pokemonListAdapter.loadStateFlow.collectLatest { loadStates ->
                swipe_refresh?.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.fetchPokemons().collectLatest { pagingData ->
                pokemonListAdapter.submitData(pagingData)
            }
        }
    }
}