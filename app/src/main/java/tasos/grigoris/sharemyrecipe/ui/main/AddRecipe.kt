package tasos.grigoris.sharemyrecipe.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import tasos.grigoris.sharemyrecipe.R
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap.CompressFormat
import android.graphics.Color
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import java.io.*
import tasos.grigoris.sharemyrecipe.Adapters.AddIngredientsAdapter
import tasos.grigoris.sharemyrecipe.Adapters.AddStepsAdapter
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import kotlinx.android.synthetic.main.add_recipe3.*
import org.json.JSONObject
import tasos.grigoris.sharemyrecipe.Model.*
import tasos.grigoris.sharemyrecipe.MyConsts

class AddRecipe : AppCompatActivity() {

    private lateinit var file1                  : File
    private lateinit var file2                  : File
    private lateinit var viewModel              : MainViewModel
    private lateinit var preparationTime        : String
    private lateinit var difficulty             : String
    private lateinit var posotites              : ArrayList<ThePosotites>
    private lateinit var categories             : ArrayList<TheCategories>
    private lateinit var ingredientsAdapter     : AddIngredientsAdapter
    private lateinit var stepsAdapter           : AddStepsAdapter
    private var ingredientsList                 = ArrayList<String>()
    private var steps                           = ArrayList<TheSteps>()
    private var selectedPosotitaPos             = 0
    private var selectedCategoryPos             = 0
    private var startLoading                    = 0L
    private var finishLoading                   = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_recipe3)

        showLoadingLayout()

        add_recipe_toolbar.setTitle("")
        setSupportActionBar(add_recipe_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        add_recipe_toolbar.setTitle(getString(R.string.add_recipe_title))

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        add_recipe_feature.setOnClickListener {

            startGallery(1)

        }

        add_recipe_photo1.setOnClickListener {

            startGallery(2)

        }

        add_recipe_publish.setOnClickListener {

            submitRecipe()

        }

        add_recipe_add_ingredient.setOnClickListener {

            ingredientsList.add("")
            ingredientsAdapter.notifyItemChanged(ingredientsList.size)

            ingredientsList.forEach {

                println("INGREDIENTS CHECK: ".plus(it))

            }

        }

        add_recipe_add_step.setOnClickListener {

            steps.add(TheSteps(step = "", step_type = 1))
            stepsAdapter.notifyItemChanged(steps.size)

            steps.forEach {

                println("STEPS CHECK: ".plus(it.step).plus(" section: ").plus(it.section).plus(" ").plus(it.step_type))

            }

        }

        add_recipe_add_section.setOnClickListener {

            steps.add(TheSteps(step = "", step_type = 0))
            stepsAdapter.notifyDataSetChanged()

            steps.forEach {

                println("STEPS CHECK: ".plus(it.step).plus(" section: ").plus(it.section).plus(" ").plus(it.step_type))

            }

        }

        getPosotites()
        loadIngredientsAdapter()
        loadStepsAdapter()

    }


    private fun loadIngredientsAdapter(){

        ingredientsList.add("")

        val llm = LinearLayoutManager(this@AddRecipe)
        ingredientsAdapter = AddIngredientsAdapter(this@AddRecipe, ingredientsList)
        add_recipe_ingredients_rv.layoutManager = llm
        add_recipe_ingredients_rv.adapter = ingredientsAdapter

        ingredientsAdapter.listener = {

            ingredientsList = ArrayList()
            ingredientsList = it
            ingredientsAdapter.notifyDataSetChanged()

        }

    }


    private fun loadStepsAdapter(){

        steps.add(TheSteps(step = "", step_type = 1))

        val llm = LinearLayoutManager(this@AddRecipe)
        stepsAdapter = AddStepsAdapter(this@AddRecipe, steps)
        add_recipe_steps_rv.layoutManager = llm
        add_recipe_steps_rv.adapter = stepsAdapter

        stepsAdapter.listener = {

            steps = ArrayList()
            steps = it
            stepsAdapter.notifyDataSetChanged()

        }

    }


    private fun getPosotites(){

        startLoading = System.currentTimeMillis()

        viewModel.getAddRecipeReq().observe(this, Observer<TheAddRecipeReq>{

            if (it?.categories == null || it.posotites == null){

                AlertDialog.Builder(this).setMessage(getString(R.string.add_recipe_no_connection))
                    .setNegativeButton(getString(R.string.close)) { dialog, which -> finish() }.create().show()

            }else{

                finishLoading = System.currentTimeMillis()

                val diff = finishLoading - startLoading

                if (diff < MyConsts.minLoading){

                    val delay = MyConsts.optimalLoading - diff
                    Handler().postDelayed({

                        hideLoadingLayout()
                        categories = it.categories
                        posotites = it.posotites
                        setUpSpinner(1)
                        setUpSpinner(2)

                    }, delay)

                } else {

                    hideLoadingLayout()
                    categories = it.categories
                    posotites = it.posotites
                    setUpSpinner(1)
                    setUpSpinner(2)

                }

            }

        })

    }


    private fun setUpSpinner(type : Int){

        val list = ArrayList<String>()

        if (type == 1){

            list.add("Επιλογή ποσότητας")

            (posotites as ArrayList<ThePosotites>).forEach {

                list.add(it.posotitaLabelPlural!!)

            }

        } else {

            list.add("Επιλογή κατηγορίας")

            categories.forEach {

                list.add(it.name!!)

            }

        }

        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list) {

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0)
                    tv.setTextColor(Color.GRAY)
                 else
                    tv.setTextColor(Color.WHITE)

                return view
            }
        }


        adapter.setDropDownViewResource(R.layout.spinner_item)

        val spinner : Spinner = if (type == 1)
            add_recipe_racions_label
        else
            add_recipe_category_label

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                (view as TextView).setTextColor(ContextCompat.getColor(applicationContext, R.color.white))

                if (type == 1)
                    selectedPosotitaPos = position
                else
                    selectedCategoryPos = position

            }
        }
    }


    private fun submitRecipe(){

        if (!::file1.isInitialized || !::file2.isInitialized){

            Toast.makeText(this, "Add images", Toast.LENGTH_SHORT).show()

        }else{

            if (!areIngredientsFilled()){

                Toast.makeText(this, "Ingredients fields are not filled ", Toast.LENGTH_SHORT).show()

            }else{

                if (!areStepsFilled()){

                    Toast.makeText(this, "Steps are not filled ", Toast.LENGTH_SHORT).show()

                }else{

                    if (add_recipe_title_et.text.isNullOrEmpty()){

                        Toast.makeText(this, "Enter recipe title", Toast.LENGTH_SHORT).show()

                    }else{

                        if (add_recipe_racions_et.text.isNullOrEmpty() || selectedPosotitaPos == 0){

                            Toast.makeText(this, "Enter number of rations", Toast.LENGTH_SHORT).show()

                        }else{

                            startLoading = System.currentTimeMillis()

                            showLoadingLayout()

                            val shortDescription        = add_recipe_description_et.text.toString()
                            val posotitaLabel           = add_recipe_racions_et.text.toString()
                            val posotitaID              = posotites[selectedPosotitaPos - 1].posotitaID.toString()
                            val title                   = add_recipe_title_et.text.toString()
                            val categoryID              = categories[selectedCategoryPos - 1].id.toString()
                            val nationality             = 1.toString()
                            val userID                  = "blablaID"
                            val preparationTime         = add_recipe_time_et.text.toString()
                            difficulty = "1"

                            val recipe = TheRecipeToSend(file1, file2, title, steps, ingredientsList, shortDescription, posotitaID,
                                posotitaLabel, categoryID, preparationTime, nationality, difficulty, userID)

                            viewModel.sendRecipe(recipe).observe(this, Observer<String>{

                                println("RECIPE SEND ".plus(it))

                                finishLoading = System.currentTimeMillis()

                                val diff = finishLoading - startLoading

                                if (diff < MyConsts.minLoading){

                                    val delay = MyConsts.optimalLoading - diff
                                    Handler().postDelayed({

                                        hideLoadingLayout()
                                        showSuccessAD(JSONObject(it).getBoolean("success"))

                                    }, delay)

                                } else {

                                    hideLoadingLayout()
                                    showSuccessAD(JSONObject(it).getBoolean("success"))

                                }

                            })

                        }
                    }

                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        onBackPressed()

        return super.onOptionsItemSelected(item)

    }


    private fun areIngredientsFilled() : Boolean{

        if (ingredientsList.size < 1)
            return false

        ingredientsList.forEach {

            if (it.isEmpty())
                return false

        }

        return true

    }


    private fun areStepsFilled() : Boolean{

        if (steps.size < 1)
            return false

        steps.forEach {

            if (it.step.isNullOrEmpty() && it.section.isNullOrEmpty())
                return false

        }

        return true

    }


    private fun startGallery(requestCode : Int){

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode)

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data!!.data)

        val bos = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.JPEG, 10 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()
        lateinit var fos : FileOutputStream

        if (requestCode == 1) {

            add_recipe_feature_img.setImageBitmap(bitmap)
            file1 = File(cacheDir, "pl1.jpg")
            file1.createNewFile()
            fos = FileOutputStream(file1)

            add_recipe_feature_icon.visibility = View.GONE
            add_recipe_feature_label.visibility = View.GONE

        }else if (requestCode == 2){

            add_recipe_photo1_img.setImageBitmap(bitmap)
            file2 = File(cacheDir, "pl2.jpg")
            file2.createNewFile()
            fos = FileOutputStream(file2)

            add_recipe_photo1_icon.visibility = View.GONE
            add_recipe_photo1_label.visibility = View.GONE

        }

        fos.write(bitmapdata)
        fos.flush()
        fos.close()

    }


    private fun showSuccessAD(result : Boolean){

        val message : String = if (result)
            getString(R.string.upload_success)
        else
            getString(R.string.upload_failure)

        AlertDialog.Builder(this).setMessage(message.plus(result.toString())).
            setPositiveButton(getString(R.string.close)) { dialog, which -> finish() }.show()

    }


    private fun showLoadingLayout(){

        add_recipe_content.visibility = View.GONE
        add_recipe_loading_layout.visibility = View.VISIBLE
        Glide.with(applicationContext).load(R.drawable.loading3).into(DrawableImageViewTarget(add_recipe_gif))

    }

    private fun hideLoadingLayout(){

        add_recipe_content.visibility = View.VISIBLE
        add_recipe_loading_layout.visibility = View.GONE

    }

}