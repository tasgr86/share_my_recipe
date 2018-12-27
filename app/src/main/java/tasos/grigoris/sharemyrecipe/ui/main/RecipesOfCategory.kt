package tasos.grigoris.sharemyrecipe.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import tasos.grigoris.sharemyrecipe.Adapters.MainAdapter
import tasos.grigoris.sharemyrecipe.Model.TheRecipes
import tasos.grigoris.sharemyrecipe.R
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.recipes_of_category.*


class RecipesOfCategory : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var categoryID = 0
    private lateinit var recipes : ArrayList<TheRecipes>
    private lateinit var adapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipes_of_category)

        setSupportActionBar(recipes_of_category_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        categoryID = intent.getIntExtra("category_id", 0)

        println("view recipes for category ".plus(categoryID))

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.getRecipesOfCategory(categoryID).observe(this, Observer<ArrayList<TheRecipes>>{

            recipes = it!!
            loadAdapter(it)

            recipes.forEach { println("recipe: ".plus(it.title).plus(" ").plus(it.shortInstructions)) }

        })


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        onBackPressed()

        return super.onOptionsItemSelected(item)

    }

    private fun loadAdapter(list : ArrayList<TheRecipes>){

        val lm = LinearLayoutManager(applicationContext)
        adapter = MainAdapter(applicationContext, list)
        recipes_of_category_rv.layoutManager = lm
        recipes_of_category_rv.adapter = adapter


//        val flip_adapter = FoldingRecipesAdapter(applicationContext, list)
//        list_view.adapter = flip_adapter
//        list_view.peakNext(true)


    }
}