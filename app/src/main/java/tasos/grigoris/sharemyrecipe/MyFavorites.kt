package tasos.grigoris.sharemyrecipe

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import tasos.grigoris.sharemyrecipe.Model.TheStoredRecipes
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.Type

class MyFavorites (val context: Context){

    var prefs : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    var gson : Gson = Gson()
    var type: Type = object : TypeToken<ArrayList<TheStoredRecipes>>() {}.type

    fun getFavorites() : ArrayList<TheStoredRecipes> {

        if (!hasFavorites())
            return ArrayList()

        val favoritesStr = prefs.getString("favorites", "")
        return gson.fromJson(favoritesStr, type)

    }

    fun storeFavorites(newFav : TheStoredRecipes) : Boolean{

        return try {

            val favorites = getFavorites()
            favorites.add(newFav)
            val str = gson.toJson(favorites)
            prefs.edit().putString("favorites", str).apply()

            true

        }catch (e : Exception){

            e.printStackTrace()
            false

        }
    }

    fun removeFavorite(id : Int) : Boolean{

        return try {

            val newFavorites = ArrayList<TheStoredRecipes>()
            val favorites = getFavorites()

            favorites.forEach {

                if (it.recipe.id != id)
                    newFavorites.add(it)

            }

            val str = gson.toJson(newFavorites)
            prefs.edit().putString("favorites", str).apply()

            true

        }catch (e : Exception){

            e.printStackTrace()
            false

        }
    }

    private fun hasFavorites() : Boolean{

        return prefs.contains("favorites")

    }


    fun isFavorite(id : Int) : Boolean{

        getFavorites().forEach {

            if (it.recipe.id == id)
                return true

        }

        return false

    }

}