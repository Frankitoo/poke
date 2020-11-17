package com.frankito.presentation.features.pokemonlist

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frankito.presentation.R
import com.frankito.presentation.ui.BaseFragment
import com.frankito.presentation.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import kotlinx.android.synthetic.main.fragment_pokemon_list.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.reflect.Field


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

    override fun setupViews(view: View) {
        initAdapter(view)

        view.swipe_refresh.setOnRefreshListener { pokemonListAdapter.refresh() }
    }

    private fun initAdapter(view: View) {
        pokemonListAdapter.onClickListener = { item -> viewModel.onPokemonSelected(item) }

        view.rvPokemons.addItemDecoration(gridDecoration)
        view.rvPokemons.layoutManager = GridLayoutManager(activity, SPAN_COUNT)
        view.rvPokemons.adapter = pokemonListAdapter.withLoadStateHeaderAndFooter(
            header = PokemonLoadingAdapter { pokemonListAdapter.retry() },
            footer = PokemonLoadingAdapter { pokemonListAdapter.retry() }
        )

        lifecycleScope.launch {
            pokemonListAdapter.loadStateFlow.collectLatest { loadStates ->
                view.swipe_refresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launch {
            viewModel.fetchPokemons().collectLatest { pagingData ->
                pokemonListAdapter.submitData(pagingData)
            }
        }
    }
}