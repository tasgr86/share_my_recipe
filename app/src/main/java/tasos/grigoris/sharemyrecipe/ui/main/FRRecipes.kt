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
import tasos.grigoris.sharemyrecipe.MyApplication
import tasos.grigoris.sharemyrecipe.MyPrefs
import tasos.grigoris.sharemyrecipe.R

    class FRRecipes : Fragment() {

        private lateinit var viewModel: MainViewModel
        private lateinit var prefs : MyPrefs

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

            return inflater.inflate(R.layout.fr_recipes, container, false)

        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            prefs = MyPrefs(requireContext())

            viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

            viewModel.getRecipes().observe(this, Observer<ArrayList<TheRecipes>>{

                if (it != null) {

                    prefs.storeRecipes(it)
                    loadAdapter(it)

                } else {

                    val list = prefs.getRecipes()

                    println("COULD NOT FETCH RECIPES. PREFS: ".plus(list))

                    if (list == null)
                        fr_recipes_no_recipes.visibility = View.VISIBLE
                    else
                        loadAdapter(list)

                }

            })

        }


        private fun loadAdapter(list : ArrayList<TheRecipes>){

            list.forEach {

                println("fetched list of recipes: ".plus(it.id).plus(it.categoryName).plus(" ").plus(it.title))

            }

            val flip_adapter = FlipAdapter(requireContext(), list)
            fr_recipes_flip_rv.adapter = flip_adapter

            if (!MyApplication.hasPeaked) {

                MyApplication.hasPeaked = true
                fr_recipes_flip_rv.peakNext(true)

            }
        }

    }
