package tasos.grigoris.sharemyrecipe.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_recipe.*
import tasos.grigoris.sharemyrecipe.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.graphics.Bitmap.CompressFormat
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import java.io.*
import tasos.grigoris.sharemyrecipe.Adapters.AddIngredientsAdapter
import tasos.grigoris.sharemyrecipe.Adapters.AddStepsAdapter
import tasos.grigoris.sharemyrecipe.Utils
import android.view.ViewGroup
import android.widget.*
import tasos.grigoris.sharemyrecipe.Model.*


class AddRecipe : AppCompatActivity() {

    private lateinit var file1                  : File
    private lateinit var file2                  : File
    private lateinit var viewModel              : MainViewModel
    private lateinit var title                  : String
    private lateinit var shortDescription       : String
    private var posotitaID                      = 0
    private lateinit var posotitaLabel          : String
    private lateinit var posotites              : ArrayList<ThePosotites>
    private lateinit var categories             : ArrayList<TheCategories>
    private lateinit var ingredientsAdapter     : AddIngredientsAdapter
    private lateinit var stepsAdapter           : AddStepsAdapter
    private var ingredientsList                 = ArrayList<TheIngredients>()
    private var steps                           = ArrayList<TheSteps>()
    private var selectedPosotitaPos             = 0
    private var selectedCategoryPos             = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_recipe)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        add_recipe_feature_img.setOnClickListener {

            startGallery(1)

        }

        add_recipe_photo1.setOnClickListener {

            startGallery(2)

        }

        add_recipe_publish.setOnClickListener {

            submitRecipe()

        }

        add_recipe_add_ingredient.setOnClickListener {

            ingredientsList.add(TheIngredients(name = "", posotita = 0.0, monadaPl = ""))
            ingredientsAdapter.notifyDataSetChanged()

        }

        add_recipe_add_step.setOnClickListener {

            steps.add(TheSteps(step = "", step_type = 1))
            stepsAdapter.notifyDataSetChanged()

        }

        add_recipe_add_section.setOnClickListener {

            steps.add(TheSteps(step = "", step_type = 0))
            stepsAdapter.notifyDataSetChanged()

        }

        getPosotites()
        loadIngredientsAdapter()
        loadStepsAdapter()

    }


    private fun loadIngredientsAdapter(){

        ingredientsList.add(TheIngredients(name = "", posotita = 0.0, monadaPl = ""))

        val llm = LinearLayoutManager(this@AddRecipe)
        ingredientsAdapter = AddIngredientsAdapter(this@AddRecipe, ingredientsList)
        add_recipe_ingredients_rv.layoutManager = llm
        add_recipe_ingredients_rv.adapter = ingredientsAdapter

    }


    private fun loadStepsAdapter(){

        steps.add(TheSteps(step = "", step_type = 1))

        val llm = LinearLayoutManager(this@AddRecipe)
        stepsAdapter = AddStepsAdapter(this@AddRecipe, steps)
        add_recipe_steps_rv.layoutManager = llm
        add_recipe_steps_rv.adapter = stepsAdapter

    }


    private fun getPosotites(){

        viewModel.getAddRecipeReq().observe(this, Observer<TheAddRecipeReq>{

            categories = it!!.categories!!
            posotites = it.posotites!!
            setUpSpinner(1)
            setUpSpinner(2)

        })

    }


    private fun setUpSpinner(type : Int){

        val list = ArrayList<String>()

        if (type == 1){

            list.add("Επιλογή ποσότητας")

            (posotites as ArrayList<ThePosotites>).forEach {

                list.add(it.posotitaLabelPlural!!)

            }

        }

        else {

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
                if (position == 0) {
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.WHITE)
                }
                return view
            }
        }


        adapter.setDropDownViewResource(R.layout.spinner_item)

        val spinner : Spinner = if (type == 1)
            add_recipe_racions_label
        else
            add_recipe_category

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

        println("Submit recipe ".plus(Utils().getCurrentDate()))


        if (!::file1.isInitialized || !::file2.isInitialized){

            Toast.makeText(this, "Add images", Toast.LENGTH_SHORT).show()

        }else{

            if (!areIngredientsFilled()){

                Toast.makeText(this, "Ingredients fields are not filled ", Toast.LENGTH_SHORT).show()

            }else{

                if (!areStepsFilled()){

                    Toast.makeText(this, "Steps are not filled ", Toast.LENGTH_SHORT).show()

                }else{

                    if (add_recipe_title.text.isNullOrEmpty()){

                        Toast.makeText(this, "Enter recipe title", Toast.LENGTH_SHORT).show()

                    }else{

                        if (add_recipe_racions.text.isNullOrEmpty() || selectedPosotitaPos == 0){

                            Toast.makeText(this, "Enter number of rations", Toast.LENGTH_SHORT).show()

                        }else{

                            val shortDescription = add_recipe_description.text.toString()
                            val posotitaLabel = add_recipe_racions.text.toString()
                            val posotitaID = posotites[selectedPosotitaPos - 1].posotitaID.toString()
                            val title = add_recipe_title.text.toString()
                            val categoryID = categories[selectedCategoryPos - 1].id.toString()
                            val prepTime = add_recipe_preapration_time.text.toString()
                            val nationality = 1.toString()
                            val difficulty  = 2.toString()
                            val userID      = "blablaID"

                            val recipe = TheRecipeToSend(file1, file2, title, steps, ingredientsList, shortDescription, posotitaID,
                                posotitaLabel, categoryID, prepTime, nationality, difficulty, userID)

                            viewModel.sendRecipe(recipe)

                        }
                    }

                }
            }
        }

    }


    private fun areIngredientsFilled() : Boolean{

        if (ingredientsList.size < 1)
            return false

        ingredientsList.forEach {

            if (it.name.isNullOrEmpty() || it.posotita == 0.0 || it.monadaPl.isNullOrEmpty())
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

            Toast.makeText(this, "requestCode ".plus(requestCode.toString().plus(" file 1")), Toast.LENGTH_SHORT).show()

            add_recipe_feature_img.setImageBitmap(bitmap)
            file1 = File(cacheDir, "pl1.jpg")
            file1.createNewFile()
            fos = FileOutputStream(file1)

        }else if (requestCode == 2){

            Toast.makeText(this, "requestCode ".plus(requestCode.toString().plus(" file 2")), Toast.LENGTH_SHORT).show()

            add_recipe_photo1.setImageBitmap(bitmap)
            file2 = File(cacheDir, "pl2.jpg")
            file2.createNewFile()
            fos = FileOutputStream(file2)

        }

        fos.write(bitmapdata)
        fos.flush()
        fos.close()

    }

    private fun createBitmap(pickedImage : Uri) : Bitmap{

        val filePath = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(pickedImage, filePath, null, null, null)
        cursor.moveToFirst()
        val imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]))

        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

//        cursor.close()

        println("IMAGEPATH ".plus(imagePath))
        println("OPTIONS ".plus(options))

        return BitmapFactory.decodeFile(imagePath, options)

    }

}