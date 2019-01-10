package tasos.grigoris.sharemyrecipe.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.show_recipe.*
import org.json.JSONObject
import tasos.grigoris.sharemyrecipe.*
import tasos.grigoris.sharemyrecipe.Adapters.IngredientsAdapter
import tasos.grigoris.sharemyrecipe.Adapters.StepsAdapter
import tasos.grigoris.sharemyrecipe.Dialogs.RatingDialog
import tasos.grigoris.sharemyrecipe.Dialogs.SliderDialog
import tasos.grigoris.sharemyrecipe.Model.*

class ShowRecipe : AppCompatActivity() {

    private lateinit var recipe             : TheRecipes
    private lateinit var theStoredRecipe    : TheStoredRecipes
    private lateinit var viewModel          : MainViewModel
    private lateinit var ingrAdapter        : IngredientsAdapter
    private lateinit var stepsAdapter       : StepsAdapter
    private lateinit var ingredients        : ArrayList<TheIngredients>
    private lateinit var steps              : ArrayList<TheSteps>
    private var textSize                    = 0F
    private var headersTextSize             = 0F
    private var racionesTextSize            = 0F
    private var startLoading                = 0L
    private var finishLoading               = 0L
    private var myRating                    = 0
    private lateinit var myPrefs            : MyPrefs

    var pic_url = "https://simplebudget.eu/recipes/pictures/recipes/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_recipe)

        myPrefs = MyPrefs(this)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val lastFont = myPrefs.getFont()

        textSize = lastFont
        headersTextSize = lastFont + 4
        racionesTextSize = lastFont + 4

        setSupportActionBar(show_recipe_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recipe = intent.getParcelableExtra("recipe")

        if (myPrefs.getTheStoredRecipe(recipe.id) != null) { // When activity gets loaded from Favorites fragment

            theStoredRecipe = myPrefs.getTheStoredRecipe(recipe.id)!!
            show_recipe_content.visibility = View.VISIBLE
            show_recipe_loading_layout.visibility = View.GONE
            ingredients = theStoredRecipe.ingredients
            steps = theStoredRecipe.steps
            loadLayout()
            loadIngredientsAdapter()
            loadStepsAdapter()
            updateFavoriteIcon()

        } else {

            show_recipe_loading_layout.visibility = View.VISIBLE
            Glide.with(applicationContext).load(R.drawable.loading3).into(DrawableImageViewTarget(show_recipe_gif))
            getStepsAndIngredients()

        }


        show_recipe_ingredients_font.setOnClickListener {

            loadSlider()

        }

        show_recipe_steps_row.setOnClickListener {

            loadSlider()

        }

        show_recipes_add_favorite.setOnClickListener {

            if (theStoredRecipe.isFavorite){

                myPrefs.updateFavoriteRecipe(recipe.id, false)
                Snackbar.make(show_recipe_top, getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT).show()

            }else{

                myPrefs.updateFavoriteRecipe(recipe.id, true)
                Snackbar.make(show_recipe_top, getString(R.string.added_to_favorites), Snackbar.LENGTH_LONG).show()

            }

            theStoredRecipe = myPrefs.getTheStoredRecipe(recipe.id)!!
            updateFavoriteIcon()

        }

        show_recipe_myrate.setOnClickListener { loadRating() }

    }

    private fun updateFavoriteIcon(){

        if (theStoredRecipe.isFavorite)
            show_recipes_add_favorite.background = ContextCompat.getDrawable(this, R.drawable.ic_favorites_show_recipe_red)
        else
            show_recipes_add_favorite.background = ContextCompat.getDrawable(this, R.drawable.ic_favorites_show_recipe)

    }

    private fun loadSlider(){

        val fm = supportFragmentManager
        val dialog = SliderDialog.newInstance(textSize)
        dialog.show(fm, "as")
        dialog.sliderListener = (object : SliderDialog.SliderListener {

            override fun onSliderDialog(value: Int) {

                myPrefs.storeFont(value.toFloat())

                textSize = value.toFloat()
                headersTextSize = value.toFloat() + 4
                racionesTextSize = value.toFloat() + 4
                ingrAdapter.updateAdapter(textSize, racionesTextSize)
                stepsAdapter.updateAdapter(textSize, headersTextSize)

            }
        })

    }



    private fun loadLayout(){

        show_recipe_title.text = theStoredRecipe.recipe.title
        Picasso.get().load(pic_url.plus(theStoredRecipe.recipe.photo1)).into(show_recipe_background)

    }


    private fun loadIngredientsAdapter(){

        val lm = LinearLayoutManager(applicationContext)
        ingrAdapter = IngredientsAdapter(applicationContext, ingredients, theStoredRecipe.recipe, textSize, racionesTextSize)
        rv_systatika.layoutManager = lm
        rv_systatika.adapter = ingrAdapter
        rv_systatika.isNestedScrollingEnabled = false

    }


    private fun loadStepsAdapter(){

        val lm = LinearLayoutManager(applicationContext)
        stepsAdapter = StepsAdapter(applicationContext, steps, textSize, headersTextSize)
        show_recipe_steps.layoutManager = lm
        show_recipe_steps.adapter = stepsAdapter
        show_recipe_steps.isNestedScrollingEnabled = false

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        onBackPressed()

        return super.onOptionsItemSelected(item)

    }

    private fun loadRating(){

        val lifecycle = this
        val fm = supportFragmentManager
        val dialog = RatingDialog.newInstance(myRating)
        dialog.show(fm, "as")
        dialog.ratingListener = (object : RatingDialog.RatingListener {

            override fun onRatingDialog(value: Int) {

                dialog.dismiss()

                viewModel.submitRating(1, recipe.id, value).observe(lifecycle, Observer<String>{

                    if ((JSONObject(it)["status"] as Int) == 1) {

                        myRating = JSONObject(it).getInt("myRating")
                        updateRatings(myRating, JSONObject(it).getDouble("avg"))

                    }
                })
            }
        })
    }


    private fun getStepsAndIngredients(){

        startLoading = System.currentTimeMillis()

        viewModel.getStepsAndIngredients(recipe.id, 1).observe(this, Observer<TheStepsAndIngredients>{

            if (it?.ingredients == null || it.steps == null) {

                Handler().postDelayed({ finish() }, 1000)

            }else{

                ingredients = it.ingredients
                steps = it.steps

                theStoredRecipe = TheStoredRecipes(recipe, steps, ingredients, false)
                myPrefs.storeTheRecipe(theStoredRecipe)

                if (it.myRating != null)
                    myRating = it.myRating

                updateRatings(it.myRating, it.avg)

                finishLoading = System.currentTimeMillis()

                val diff = finishLoading - startLoading

                if (diff < MyConsts.minLoading){

                    val delay = MyConsts.optimalLoading - diff
                    Handler().postDelayed({ loadContent() }, delay)

                } else {

                    loadContent()

                }
            }
        })
    }


    private fun loadContent(){

        show_recipe_loading_layout.visibility = View.GONE
        show_recipe_content.visibility = View.VISIBLE

        loadLayout()
        loadIngredientsAdapter()
        loadStepsAdapter()

    }


    private fun updateRatings(myRating : Int?, avg : Double?){

        if (myRating == null)
            show_recipe_myrate_txt.text = getString(R.string.my_rate).plus(" ").plus("-")
        else
            show_recipe_myrate_txt.text = getString(R.string.my_rate).plus(" ").plus(myRating)

        if (avg == null)
            show_recipe_avg.text = "-"
        else
            show_recipe_avg.text = avg.toString().plus("/").plus("5")

    }
}