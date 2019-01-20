package tasos.grigoris.sharemyrecipe

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tasos.grigoris.sharemyrecipe.Model.TheLoginResponse

class ResponseManager(val context : Context){

    private val gson = Gson()
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val type = object : TypeToken<ArrayList<TheLoginResponse>>() {}.type

    fun getResponse() : TheLoginResponse?{

        if (!prefs.contains("login_response"))
            return null

        return gson.fromJson(prefs.getString("login_response", ""), type)

    }

    fun storeResponse(response : TheLoginResponse){

        val responseStr = gson.toJson(response)
        prefs.edit().putString("login_response", responseStr).apply()

    }

    fun deleteResponse(){

        prefs.edit().remove("login_response").apply()

    }

}