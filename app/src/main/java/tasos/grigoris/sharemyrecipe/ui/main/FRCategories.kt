package tasos.grigoris.sharemyrecipe.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment.*
import tasos.grigoris.sharemyrecipe.Adapters.CategoriesAdapter
import tasos.grigoris.sharemyrecipe.Model.TheCategories
import tasos.grigoris.sharemyrecipe.R

class FRCategories : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter : CategoriesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        viewModel.getCategories().observe(this, Observer<ArrayList<TheCategories>>{

            if (it == null)
                fragment_no_items.visibility = View.VISIBLE
            else {

                loadAdapter(it)
                adapter.notifyDataSetChanged()

            }

        })

    }

    private fun loadAdapter(list : ArrayList<TheCategories>){

        val lm = GridLayoutManager(requireContext(), 2)
        adapter = CategoriesAdapter(requireContext(), list)
        fragment_rv.layoutManager = lm
        fragment_rv.adapter = adapter

    }

}
