package tasos.grigoris.sharemyrecipe

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.joda.time.format.DateTimeFormat
import tasos.grigoris.sharemyrecipe.Model.TheRecipes
import tasos.grigoris.sharemyrecipe.Model.TheStoredRecipes
import java.time.LocalDate

class MyPrefs(var context : Context){

    val fmt = DateTimeFormat.forPattern("yyyyMMdd")
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val gson = Gson()
    private val typeRecipes = object : TypeToken<ArrayList<TheRecipes>>() {}.type
    private val typeStoredRecipes = object : TypeToken<ArrayList<TheStoredRecipes>>() {}.type

    fun storeFont(font : Float){

        prefs.edit().putFloat("font", font).apply()

    }


    fun getFont() : Float{

        val defaultFont = context.resources.getDimension(R.dimen.small) / context.resources.displayMetrics.density

        return prefs.getFloat("font", defaultFont)

    }


    fun storeRecipes(recipes : ArrayList<TheRecipes>){

        prefs.edit().putString("recipes", gson.toJson(recipes)).apply()

    }


    fun getRecipes() : ArrayList<TheRecipes>?{

        if (!prefs.contains("recipes"))
            return null

        val recipes_str = prefs.getString("recipes", "")
        return gson.fromJson(recipes_str, typeRecipes)

    }


    fun getTheStoredRecipe(id : Int) : TheStoredRecipes? {

        if (!prefs.contains("stored_recipes"))
            return null

        val storedStr = prefs.getString("stored_recipes", "")
        val list = gson.fromJson<ArrayList<TheStoredRecipes>>(storedStr, typeStoredRecipes)

        list.forEach {

            if (it.recipe.id == id)
                return it

        }

        return null

    }



    fun getFavoriteRecipes() : ArrayList<TheStoredRecipes>? {

        if (!prefs.contains("stored_recipes"))
            return null

        val favorites = ArrayList<TheStoredRecipes>()

        val storedStr = prefs.getString("stored_recipes", "")
        val list = gson.fromJson<ArrayList<TheStoredRecipes>>(storedStr, typeStoredRecipes)

        list.forEach {

            if (it.isFavorite)
                favorites.add(it)

        }


        list.forEach {

            println("FAVORITES LIST OF STORED RECIPES: ".plus(it.recipe.id).plus(" ").
                plus(it.recipe.title).plus(" ").plus(it.isFavorite))

        }

        return favorites

    }



    fun storeTheRecipe(recipe : TheStoredRecipes) {

        val list : ArrayList<TheStoredRecipes>

        if (prefs.contains("stored_recipes")) {

            val storedStr = prefs.getString("stored_recipes", "")
            list = gson.fromJson<ArrayList<TheStoredRecipes>>(storedStr, typeStoredRecipes)

        }else
            list = ArrayList()

        list.add(recipe)

        val str = gson.toJson(list)
        prefs.edit().putString("stored_recipes", str).apply()

        list.forEach {

            println("LIST OF STORED RECIPES: ".plus(it.recipe.id).plus(" ").
                plus(it.recipe.title).plus(" ").plus(it.isFavorite))

        }

    }



    fun updateFavoriteRecipe(id : Int, isFavorite : Boolean) {

        if (!prefs.contains("stored_recipes"))
            return

        val storedStr = prefs.getString("stored_recipes", "")
        val list = gson.fromJson<ArrayList<TheStoredRecipes>>(storedStr, typeStoredRecipes)

        list.forEach {

            if (it.recipe.id == id){

                it.isFavorite = isFavorite
                val str = gson.toJson(list)
                prefs.edit().putString("stored_recipes", str).apply()

                return

            }
        }
    }


    fun getLastDaySplashWasShown() : org.joda.time.LocalDate{

        return fmt.parseLocalDate(prefs.getString("last_splash_screen", "20190101"))

    }

    fun storeSplashDay(){

        prefs.edit().putString("last_splash_screen", fmt.print(org.joda.time.LocalDate.now())).apply()

    }

}