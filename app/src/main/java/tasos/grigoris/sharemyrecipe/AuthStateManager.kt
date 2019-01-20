package tasos.grigoris.sharemyrecipe

import android.content.Context
import android.net.Uri
import android.preference.PreferenceManager
import com.google.gson.reflect.TypeToken
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.TokenResponse
import tasos.grigoris.sharemyrecipe.Model.TheLoginResponse
import com.google.gson.GsonBuilder

class AuthStateManager(val context : Context){

    var gsonSerialize = GsonBuilder().registerTypeAdapter(Uri::class.java, UriSerializer()).create()
    var gsonDeserialize = GsonBuilder().registerTypeAdapter(Uri::class.java, UriDeserializer()).create()
    private lateinit var state  : AuthState
  //  private val gson            = Gson()
    private val prefs           = PreferenceManager.getDefaultSharedPreferences(context)
    private val type            = object : TypeToken<AuthState>() {}.type
    private val typeResponse    = object : TypeToken<TheLoginResponse>() {}.type

    fun getState() : AuthState?{

        if (::state.isInitialized)
            return state

        if (!prefs.contains("auth_state"))
            return null

        state = gsonDeserialize.fromJson(prefs.getString("auth_state", ""), type)
        return state

    }

    fun storeStateAfterAuthorization(response : AuthorizationResponse?, ex : AuthorizationException?){

        state.update(response, ex)
        val responseStr = gsonSerialize.toJson(state)
        prefs.edit().putString("auth_state", responseStr).apply()

        println("3 ".plus(getState()).plus(" ").plus(responseStr))

    }

    fun storeStateAfterAuthentication(response : TokenResponse, ex : AuthorizationException?){

        state.update(response, ex)
        val responseStr = gsonSerialize.toJson(state)
        prefs.edit().putString("auth_state", responseStr).apply()

    }

    fun setState(state : AuthState){

        this.state = state
        val responseStr = gsonSerialize.toJson(state)
        prefs.edit().putString("auth_state", responseStr).apply()

        println("2 setState: ".plus(responseStr))

    }

    fun getResponse() : TheLoginResponse?{

        if (!prefs.contains("login_response"))
            return null

        return gsonDeserialize.fromJson(prefs.getString("login_response", ""), typeResponse)

    }

    fun storeResponse(response : TheLoginResponse){

        val responseStr = gsonSerialize.toJson(response)
        prefs.edit().putString("login_response", responseStr).apply()

    }

    fun deleteResponse(){

        prefs.edit().remove("login_response").apply()

    }

    fun deleteState(){

        prefs.edit().remove("auth_state").apply()

    }

}