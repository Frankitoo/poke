package com.frankito.presentation.features.pokemondetails

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.frankito.presentation.R
import com.frankito.presentation.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_pokemon_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class PokemonDetailsFragment : BaseFragment<PokemonDetailsViewModel>() {

    override val layoutRes: Int = R.layout.fragment_pokemon_details

    override val viewModel: PokemonDetailsViewModel by viewModel()

    override fun setupViews(view: View) {
        viewModel.pokemonDetail.observe(this) {pokemonDetail ->
            tvName.text = pokemonDetail.name

            val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
            Glide.with(imageView.context)
                .load(pokemonDetail.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.pokeball)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(imageView)
        }
    }
}