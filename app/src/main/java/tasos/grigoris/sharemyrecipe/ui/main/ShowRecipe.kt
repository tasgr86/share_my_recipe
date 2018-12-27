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

    private lateinit var viewModel      : MainViewModel
    private lateinit var recipe         : TheRecipes
    private lateinit var ingrAdapter    : IngredientsAdapter
    private lateinit var stepsAdapter   : StepsAdapter
    private lateinit var ingredients    : ArrayList<TheIngredients>
    private lateinit var steps          : ArrayList<TheSteps>
    private var textSize                = 0F
    private var headersTextSize         = 0F
    private var racionesTextSize        = 0F
    private var minLoading              = 1000L
    private var optimalLoading          = 1000L
    private var startLoading            = 0L
    private var finishLoading           = 0L

    var pic_url = "https://simplebudget.eu/recipes/pictures/recipes/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_recipe)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        textSize = resources.getDimension(R.dimen.show_recipe_txt) / resources.displayMetrics.density
        headersTextSize = resources.getDimension(R.dimen.show_recipe_headers) / resources.displayMetrics.density
        racionesTextSize = resources.getDimension(R.dimen.show_recipe_raciones) / resources.displayMetrics.density

        setSupportActionBar(show_recipe_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recipe = intent.getParcelableExtra("recipe")

        if (intent.hasExtra("ingredients") && intent.hasExtra("steps")) {

            show_recipe_content.visibility = View.VISIBLE
            show_recipe_loading_layout.visibility = View.GONE
            ingredients = intent.getParcelableArrayListExtra("ingredients")
            steps = intent.getParcelableArrayListExtra("steps")
            loadLayout()
            loadIngredientsAdapter()
            loadStepsAdapter()

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

            if (viewModel.isFavorite(recipe.id)){

                viewModel.removeFavorite(recipe.id)
                Snackbar.make(show_recipe_top, getString(R.string.removed_from_favorites), Snackbar.LENGTH_SHORT).show()

            }else{

                viewModel.storeFavorites(TheFavorites(recipe, steps, ingredients))
                Snackbar.make(show_recipe_top, getString(R.string.added_to_favorites), Snackbar.LENGTH_LONG).show()

            }

            updateFavoriteIcon()

        }

        show_recipe_myrate.setOnClickListener { loadRating() }

        updateFavoriteIcon()

    }

    private fun updateFavoriteIcon(){

        if (viewModel.isFavorite(recipe.id))
            show_recipes_add_favorite.background = ContextCompat.getDrawable(this, R.drawable.ic_favorite_red)
        else
            show_recipes_add_favorite.background = ContextCompat.getDrawable(this, R.drawable.ic_favorite)

    }

    private fun loadSlider(){

        val fm = supportFragmentManager
        val dialog = SliderDialog.newInstance(textSize)
        dialog.show(fm, "as")
        dialog.sliderListener = (object : SliderDialog.SliderListener {

            override fun onSliderDialog(value: Int) {

                textSize = value.toFloat()
                headersTextSize = value.toFloat() + 4
                racionesTextSize = value.toFloat() + 8
                ingrAdapter.updateAdapter(textSize, racionesTextSize)
                stepsAdapter.updateAdapter(textSize, headersTextSize)

            }
        })

    }




    private fun loadLayout(){

        show_recipe_title.text = recipe.title
        Picasso.get().load(pic_url.plus(recipe.photo1)).into(show_recipe_background)

    }


    private fun loadIngredientsAdapter(){

        val lm = LinearLayoutManager(applicationContext)
        ingrAdapter = IngredientsAdapter(
            applicationContext,
            ingredients,
            recipe,
            textSize,
            racionesTextSize
        )
        rv_systatika.layoutManager = lm
        rv_systatika.adapter = ingrAdapter
        rv_systatika.isNestedScrollingEnabled = false

    }


    private fun loadStepsAdapter(){

        val lm = LinearLayoutManager(applicationContext)
        stepsAdapter =
                StepsAdapter(applicationContext, steps, textSize, headersTextSize)
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
        val dialog = RatingDialog.newInstance()
        dialog.show(fm, "as")
        dialog.ratingListener = (object : RatingDialog.RatingListener {

            override fun onRatingDialog(value: Int) {

                dialog.dismiss()

                viewModel.submitRating(1, recipe.id, value).observe(lifecycle, Observer<String>{

                    if ((JSONObject(it)["status"] as Int) == 1) {

                        updateRatings(JSONObject(it).getInt("myRating"), JSONObject(it).getDouble("avg"))
//                        show_recipe_myrate_txt.text = getString(R.string.my_rate).plus(" ").plus(value.toString())

                    }
                })

            }
        })

    }


    private fun getStepsAndIngredients(){

        startLoading = System.currentTimeMillis()

        Toast.makeText(this, "Geting recipe ".plus(recipe.id), Toast.LENGTH_LONG).show()

        viewModel.getStepsAndIngredients(recipe.id, 1).observe(this, Observer<TheStepsAndIngredients>{

            ingredients = it!!.ingredients!!
            steps = it.steps!!


            println("Fetched avg ".plus(it.avg).plus(" ").plus(it.myRating))

            it.ingredients!!.forEach {

                println("Fetched ingreients: ".plus(it.ingredientsID).plus("").plus(it.name))

            }


            it.steps!!.forEach {

                println("Fetched steps: ".plus(it.step_id).plus("").plus(it.step))

            }



            updateRatings(it.myRating, it.avg)

            finishLoading = System.currentTimeMillis()

            val diff = finishLoading - startLoading

            if (diff < minLoading){

                val delay = optimalLoading - diff
                Handler().postDelayed({ loadContent() }, delay)

            } else {

                loadContent()

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
            show_recipe_avg.text = avg.toString()


    }

}