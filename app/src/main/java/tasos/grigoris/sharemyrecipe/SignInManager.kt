package tasos.grigoris.sharemyrecipe

import android.app.Activity
import android.content.Context
import com.google.android.gms.auth.api.signin.*

class SignInManager (val context : Context){

    private lateinit var gso                : GoogleSignInOptions
    lateinit var signInClient               : GoogleSignInClient
    private var account                     : GoogleSignInAccount? = null

    fun initGSO() : GoogleSignInClient{

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.clientID))
            .requestEmail().build()

        signInClient = GoogleSignIn.getClient(context, gso)

        return signInClient

    }

    fun getAccount() : GoogleSignInAccount?{

        account = GoogleSignIn.getLastSignedInAccount(context)
        return this.account

    }

    fun isLoggedIn() : Boolean{

        return account != null

    }

    fun updateAccount(account: GoogleSignInAccount?){

        this.account = account

    }

    fun signOut() {

        signInClient.signOut().addOnCompleteListener(context as Activity) {

            println(it.result)

        }
    }

}