package tasos.grigoris.sharemyrecipe.ui.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import tasos.grigoris.sharemyrecipe.Model.*
import tasos.grigoris.sharemyrecipe.MyFavorites
import tasos.grigoris.sharemyrecipe.RetrofitRepo

class MainViewModel (var _application: Application) : AndroidViewModel(_application) {

    fun getRecipes() : LiveData<ArrayList<TheRecipes>> {

        return RetrofitRepo().getRecipes()

    }

    fun getCategories() : LiveData<ArrayList<TheCategories>> {

        return RetrofitRepo().getCategories()

    }

    fun getIngredientsOfRecipe(recipeID : Int) : LiveData<ArrayList<TheIngredients>> {

        return RetrofitRepo().getIngredientsOfRecipe(recipeID)

    }

    fun getStepsAndIngredients(recipeID : Int, userID : Int) : LiveData<TheStepsAndIngredients> {

        return RetrofitRepo().getStepsAndIngredients(recipeID, userID)

    }

    fun getSteps(recipeID : Int) : LiveData<ArrayList<TheSteps>> {

        return RetrofitRepo().getSteps(recipeID)

    }


    fun getAddRecipeReq() : LiveData<TheAddRecipeReq> {

        return RetrofitRepo().getAddRecipeReq()

    }



    fun getRecipesOfCategory(categoryID : Int) : LiveData<ArrayList<TheRecipes>> {

        return RetrofitRepo().getRecipesOfCategory(categoryID)

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
