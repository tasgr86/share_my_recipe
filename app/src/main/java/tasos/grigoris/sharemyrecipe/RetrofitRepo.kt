package tasos.grigoris.sharemyrecipe

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.media.Rating
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.create
import tasos.grigoris.sharemyrecipe.Model.*
import java.io.File
import kotlin.coroutines.coroutineContext


class RetrofitRepo(_apiServices: RetrofitAPI = RetrofitClient.createClient().create(RetrofitAPI::class.java)){

    val apiServices = _apiServices


    fun getRecipes() : LiveData<ArrayList<TheRecipes>> {

        val recipes = MutableLiveData<ArrayList<TheRecipes>>()
        val call = apiServices.fetchRecipes()

        call.enqueue(object : Callback<ArrayList<TheRecipes>> {
            override fun onResponse(call: Call<ArrayList<TheRecipes>>, response: Response<ArrayList<TheRecipes>>) {

                if (response.isSuccessful)
                    recipes.value = response.body()

                println("got recipes: ".plus(recipes))

            }

            override fun onFailure(call: Call<ArrayList<TheRecipes>>?, t: Throwable?) {

                println("Get Recipes Failure ".plus(t.toString()))
                recipes.value = null

            }
        })

        return recipes
    }


    fun getCategories() : LiveData<ArrayList<TheCategories>> {

        val categories = MutableLiveData<ArrayList<TheCategories>>()
        val call = apiServices.fetchCategories()

        call.enqueue(object : Callback<ArrayList<TheCategories>> {
            override fun onResponse(call: Call<ArrayList<TheCategories>>, response: Response<ArrayList<TheCategories>>) {

                if (response.isSuccessful)
                    categories.value = response.body()

            }

            override fun onFailure(call: Call<ArrayList<TheCategories>>?, t: Throwable?) {

                println("Get Ingredients Failure ".plus(t.toString()))
                categories.value = null

            }
        })

        return categories

    }


    fun getStepsAndIngredients(recipeID : Int, userID : Int) : LiveData<TheStepsAndIngredients>{

        val list = MutableLiveData<TheStepsAndIngredients>()
        val call = apiServices.fetchStepsAndIngredients(recipeID, userID)

        call.enqueue(object : Callback<TheStepsAndIngredients> {
            override fun onResponse(call: Call<TheStepsAndIngredients>, response: Response<TheStepsAndIngredients>) {

                if (response.isSuccessful)
                    list.value = response.body()

            }

            override fun onFailure(call: Call<TheStepsAndIngredients>?, t: Throwable?) {

                println("Get Ingredients of Recipe Failure ".plus(t.toString()))
                list.value = null

            }
        })

        return list

    }


    fun getIngredientsOfRecipe(recipeID : Int) : LiveData<ArrayList<TheIngredients>>{

        val ingredients = MutableLiveData<ArrayList<TheIngredients>>()
        val call = apiServices.fetchIngredientsOfRecipe(recipeID)

        call.enqueue(object : Callback<ArrayList<TheIngredients>> {
            override fun onResponse(call: Call<ArrayList<TheIngredients>>, response: Response<ArrayList<TheIngredients>>) {

                if (response.isSuccessful)
                    ingredients.value = response.body()

            }

            override fun onFailure(call: Call<ArrayList<TheIngredients>>?, t: Throwable?) {

                println("Get Ingredients of Recipe Failure ".plus(t.toString()))
                ingredients.value = null

            }
        })

        return ingredients

    }

    fun getSteps(recipeID : Int) : LiveData<ArrayList<TheSteps>>{

        val ingredients = MutableLiveData<ArrayList<TheSteps>>()
        val call = apiServices.fetchSteps(recipeID)

        call.enqueue(object : Callback<ArrayList<TheSteps>> {
            override fun onResponse(call: Call<ArrayList<TheSteps>>, response: Response<ArrayList<TheSteps>>) {

                if (response.isSuccessful)
                    ingredients.value = response.body()

            }

            override fun onFailure(call: Call<ArrayList<TheSteps>>?, t: Throwable?) {

                println("Get Ingredients of Recipe Failure ".plus(t.toString()))
                ingredients.value = null

            }
        })

        return ingredients

    }


    fun getAddRecipeReq() : LiveData<TheAddRecipeReq>{

        val ingredients = MutableLiveData<TheAddRecipeReq>()
        val call = apiServices.fetchAddRecipe()

        call.enqueue(object : Callback<TheAddRecipeReq> {
            override fun onResponse(call: Call<TheAddRecipeReq>, response: Response<TheAddRecipeReq>) {

                if (response.isSuccessful)
                    ingredients.value = response.body()

            }

            override fun onFailure(call: Call<TheAddRecipeReq>?, t: Throwable?) {

                ingredients.value = null

            }
        })

        return ingredients

    }


    fun getRecipesOfCategory(categoryID : Int) : LiveData<ArrayList<TheRecipes>>{

        val recipes = MutableLiveData<ArrayList<TheRecipes>>()
        val call = apiServices.fetchRecipesOfCategory(categoryID)

        call.enqueue(object : Callback<ArrayList<TheRecipes>> {
            override fun onResponse(call: Call<ArrayList<TheRecipes>>, response: Response<ArrayList<TheRecipes>>) {

                if (response.isSuccessful)
                    recipes.value = response.body()

            }

            override fun onFailure(call: Call<ArrayList<TheRecipes>>?, t: Throwable?) {

                println("Get Recipes of Category Failure ".plus(t.toString()))
                recipes.value = null

            }
        })

        return recipes

    }


    fun submitRating(userID : Int, recipeID: Int, rating: Int) : LiveData<String>{

        val theResponse = MutableLiveData<String>()
        val scalarApiServices = RetrofitClient.createScalarClient().create(RetrofitAPI::class.java)
        val call = scalarApiServices.submitRating(userID, recipeID, rating)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful)
                    theResponse.value = response.body().toString()

            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {

                println("submit rating Failure ".plus(t.toString()))

            }
        })

        return theResponse

    }


    fun verifyUser(userID : String, authCode : String) : LiveData<String>{

        val theResponse = MutableLiveData<String>()
        val scalarApiServices = RetrofitClient.createScalarClient().create(RetrofitAPI::class.java)
        val call = scalarApiServices.verifyUser(userID, authCode)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                println("response success: ".plus(response.body().toString()))

                if (response.isSuccessful)
                    theResponse.value = response.body().toString()

            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {

                println("user authorization Failure ".plus(t.toString()))

            }
        })


        return theResponse

    }



    fun sendRecipe(recipe : TheRecipeToSend) : LiveData<String>{

        val gson = Gson()

        val title                   = RequestBody.create(MediaType.parse("text/plain"), recipe.title!!)
        val steps                   = RequestBody.create(MediaType.parse("text/plain"), gson.toJson(recipe.steps))
        val ingredients             = RequestBody.create(MediaType.parse("text/plain"), gson.toJson(recipe.ingredients))
        val shortDesc               = RequestBody.create(MediaType.parse("text/plain"), recipe.shortDesc!!)
        val posotitaID              = RequestBody.create(MediaType.parse("text/plain"), recipe.posotitaID!!)
        val posotitaLabel           = RequestBody.create(MediaType.parse("text/plain"), recipe.posotitaLabel!!)
        val categoryID              = RequestBody.create(MediaType.parse("text/plain"), recipe.categoryID!!)
        val prepTime                = RequestBody.create(MediaType.parse("text/plain"), recipe.prepTime!!)
        val nationality             = RequestBody.create(MediaType.parse("text/plain"), recipe.nationality!!)
        val difficulty              = RequestBody.create(MediaType.parse("text/plain"), recipe.difficulty!!)
        val userID                  = RequestBody.create(MediaType.parse("text/plain"), recipe.userID!!)

        val theResponse = MutableLiveData<String>()
        val scalarApiServices = RetrofitClient.createScalarClient().create(RetrofitAPI::class.java)

        val requestBodyF1 = RequestBody.create(MediaType.parse("*/*"), recipe.file1!!)
        val fileToUploadF1 = MultipartBody.Part.createFormData("file1", recipe.file1.name, requestBodyF1)
        val filenameF1 = RequestBody.create(MediaType.parse("text/plain"), recipe.file1.name)

        val requestBodyF2 = RequestBody.create(MediaType.parse("*/*"), recipe.file2!!)
        val fileToUploadF2 = MultipartBody.Part.createFormData("file2", recipe.file2.name, requestBodyF2)
        val filenameF2 = RequestBody.create(MediaType.parse("text/plain"), recipe.file2.name)

        val call = scalarApiServices.sendRecipe(filenameF1, fileToUploadF1, filenameF2, fileToUploadF2, title, steps, ingredients,  shortDesc, posotitaID,
            posotitaLabel, categoryID, prepTime, nationality, difficulty, userID)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {

                println("Send recipe success ".plus(response.body().toString()))

                theResponse.value = response.body().toString()

            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {

                println("submit rating Failure ".plus(t.toString()))

            }
        })

        return theResponse

    }

}