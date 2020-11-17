package com.frankito.presentation.features.pokemondetails

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.frankito.presentation.R
import com.frankito.presentation.ui.BaseFragment
import com.frankito.presentation.utils.getTypeColor
import kotlinx.android.synthetic.main.fragment_pokemon_details.*
import kotlinx.android.synthetic.main.fragment_pokemon_details.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class PokemonDetailsFragment : BaseFragment<PokemonDetailsViewModel>() {

    override val layoutRes: Int = R.layout.fragment_pokemon_details

    override val viewModel: PokemonDetailsViewModel by viewModel()

    override fun setupViews(view: View) {
        viewModel.fetchPokemon()

        view.tvPrimaryType.visibility = View.GONE
        view.tvSecondaryType.visibility = View.GONE

        viewModel.pokemonDetailLiveData.observe(this) { pokemonDetail ->
            tvName.text = pokemonDetail.name

            val heightString = "${pokemonDetail.height.value} ${pokemonDetail.height.unit}"
            tvHeightValue.text = heightString

            val weightString = "${pokemonDetail.weight.value} ${pokemonDetail.weight.unit}"
            tvWeightValue.text = weightString

            val experienceString =
                "${pokemonDetail.experience.value} ${pokemonDetail.experience.unit}"
            tvExperienceValue.text = experienceString

            val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
            Glide.with(imageView.context)
                .load(pokemonDetail.imageUrl)
                .fitCenter()
                .placeholder(R.drawable.pokeball)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(imageView)

            setupTypeViews(pokemonDetail.types)
            setupAbilities(pokemonDetail.abilities)
        }
    }

    private fun setupTypeViews(types: List<String>) {
        if (types.isNotEmpty()) {
            val type = types.first()
            tvPrimaryType.text = type
            val color = ContextCompat.getColor(requireContext(), type.getTypeColor())
            tvPrimaryType.backgroundTintList = ColorStateList.valueOf(color)
            tvPrimaryType.visibility = View.VISIBLE
        } else {
            tvPrimaryType.visibility = View.GONE
        }
        if (types.size >= 2) {
            val type = types[1]
            tvSecondaryType.text = type
            val color = ContextCompat.getColor(requireContext(), type.getTypeColor())
            tvSecondaryType.backgroundTintList = ColorStateList.valueOf(color)
            tvSecondaryType.visibility = View.VISIBLE
        } else {
            tvSecondaryType.visibility = View.GONE
        }
    }

    private fun setupAbilities(abilities: List<String>) {
        if (abilities.isNotEmpty()) {
            rvAbilities.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvAbilities.adapter = AbilityAdapter(abilities)
            rvAbilities.visibility = View.VISIBLE
        } else {
            rvAbilities.visibility = View.GONE
        }
    }
}