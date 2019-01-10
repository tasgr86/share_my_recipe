package tasos.grigoris.sharemyrecipe.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment.*
import tasos.grigoris.sharemyrecipe.Adapters.FavoritesAdapter
import tasos.grigoris.sharemyrecipe.Model.TheStoredRecipes
import tasos.grigoris.sharemyrecipe.Model.TheRecipes
import tasos.grigoris.sharemyrecipe.MyPrefs
import tasos.grigoris.sharemyrecipe.R

class FRFavorites : Fragment() {

    private lateinit var prefs          : MyPrefs
    private lateinit var viewModel      : MainViewModel
    private lateinit var adapter        : FavoritesAdapter
    private lateinit var recipes        : ArrayList<TheRecipes>
    private var favorites               : ArrayList<TheStoredRecipes>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        prefs = MyPrefs(requireContext())

    }


    override fun onResume() {
        super.onResume()

        favorites = prefs.getFavoriteRecipes()

        if (favorites == null || (favorites != null && favorites!!.size == 0)) {

            fragment_no_items.text = getString(R.string.no_favorites_to_show)
            fragment_no_items.visibility = View.VISIBLE
            fragment_rv.visibility = View.GONE
            return

        }

        recipes = ArrayList()

        favorites!!.forEach {

            recipes.add(it.recipe)

        }

        loadAdapter()

    }

    private fun loadAdapter(){

        val lm = LinearLayoutManager(context)
        adapter = FavoritesAdapter(context!!, favorites!!)
        fragment_rv.layoutManager = lm
        fragment_rv.adapter = adapter

    }

}
