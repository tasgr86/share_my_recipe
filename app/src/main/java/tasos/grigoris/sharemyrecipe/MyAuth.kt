package tasos.grigoris.sharemyrecipe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class MyAuth(val context : Context){

    fun init(){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(context.getString(R.string.clientID))
                .requestServerAuthCode(context.getString(R.string.clientID)).requestEmail().build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        val intent = mGoogleSignInClient.getSignInIntent()
        (context as Activity).startActivityForResult(intent, 1)

    }

    fun getAccessToken(data: Intent) {

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        try {

            val account = task.getResult(ApiException::class.java)

            println("TESTTTT ".plus(account).plus(" ").plus(account!!.email))

        } catch (e: ApiException) {
            e.printStackTrace()
            println("status code: " + e.statusCode.toString())
        }

    }

}