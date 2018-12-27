package tasos.grigoris.sharemyrecipe.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fr_recipes.*
import tasos.grigoris.sharemyrecipe.Adapters.FlipAdapter
import tasos.grigoris.sharemyrecipe.Model.TheRecipes
import tasos.grigoris.sharemyrecipe.R

class FRRecipes : Fragment() {

    private lateinit var viewModel: MainViewModel
//    private lateinit var adapter : MainAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fr_recipes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)


        println("FRecipesssss")

        viewModel.getRecipes().observe(this, Observer<ArrayList<TheRecipes>>{

            loadAdapter(it!!)
//            adapter.notifyDataSetChanged()


            it.forEach {

                println("RECIPES: ".plus(it.id).plus(" ").plus(it.title).plus(" ").plus(" avg: ".plus(it.avg)))

            }


        })

    }


    private fun loadAdapter(list : ArrayList<TheRecipes>){

      //  adapter = MainAdapter(requireContext(), list)
        val flip_adapter = FlipAdapter(requireContext(), list)
        fr_recipes_flip_rv.adapter = flip_adapter
        fr_recipes_flip_rv.peakNext(true)

    }

}
