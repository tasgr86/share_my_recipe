package tasos.grigoris.sharemyrecipe.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import tasos.grigoris.sharemyrecipe.Model.*
import tasos.grigoris.sharemyrecipe.MyFavorites
import tasos.grigoris.sharemyrecipe.RetrofitRepo

class MainViewModel (var _application: Application) : AndroidViewModel(_application) {

    private lateinit var recipes                    : LiveData<ArrayList<TheRecipes>>
    private lateinit var categories                 : LiveData<ArrayList<TheCategories>>
    private lateinit var stepsAndIngredients        : LiveData<TheStepsAndIngredients>
    private lateinit var addRecipe                  : LiveData<TheAddRecipeReq>
    private lateinit var recipesOfCategory          : LiveData<ArrayList<TheRecipes>>

    fun getRecipes() : LiveData<ArrayList<TheRecipes>> {

        if (::recipes.isInitialized)
            return recipes

        recipes = RetrofitRepo().getRecipes()
        return recipes

    }

    fun getCategories() : LiveData<ArrayList<TheCategories>> {

        if (::categories.isInitialized)
            return categories

        categories = RetrofitRepo().getCategories()
        return categories

    }


    fun getStepsAndIngredients(recipeID : Int, userID : Int) : LiveData<TheStepsAndIngredients> {

        if (::stepsAndIngredients.isInitialized)
            return stepsAndIngredients

        stepsAndIngredients = RetrofitRepo().getStepsAndIngredients(recipeID, userID)
        return stepsAndIngredients

    }


    fun getAddRecipeReq() : LiveData<TheAddRecipeReq> {

        if (::addRecipe.isInitialized)
            return addRecipe

        addRecipe = RetrofitRepo().getAddRecipeReq()
        return addRecipe

    }


    fun getRecipesOfCategory(categoryID : Int) : LiveData<ArrayList<TheRecipes>> {

        if (::recipesOfCategory.isInitialized)
            return recipesOfCategory

        recipesOfCategory = RetrofitRepo().getRecipesOfCategory(categoryID)
        return recipesOfCategory

    }

    fun getFavorites() : ArrayList<TheStoredRecipes>{

        return MyFavorites(_application).getFavorites()

    }

    fun storeFavorites(fav : TheStoredRecipes){

        MyFavorites(_application).storeFavorites(fav)

    }

    fun removeFavorite(id : Int){

        MyFavorites(_application).removeFavorite(id)

    }

    fun isFavorite(id : Int) : Boolean{

        return MyFavorites(_application).isFavorite(id)

    }


    fun submitRating(userID : Int, recipeID: Int, rating : Int) : LiveData<String>{

        return RetrofitRepo().submitRating(userID, recipeID, rating)

    }


    fun sendRecipe(recipe : TheRecipeToSend) : LiveData<String>{

        return RetrofitRepo().sendRecipe(recipe)

    }
}