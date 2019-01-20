package tasos.grigoris.sharemyrecipe.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fr_profile.*
import net.openid.appauth.*
import tasos.grigoris.sharemyrecipe.R
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import tasos.grigoris.sharemyrecipe.AuthStateManager
import tasos.grigoris.sharemyrecipe.Model.TheLoginResponse
import tasos.grigoris.sharemyrecipe.RetrofitRepo
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class FRProfile: Fragment() {

    private lateinit var viewModel              : MainViewModel
    private lateinit var authorizationService   : AuthorizationService
    private var authState                       : AuthState? = null
    private lateinit var response               : TheLoginResponse
    private lateinit var authStateManager       : AuthStateManager
    private lateinit var account                : GoogleSignInAccount

    companion object {

        private const val AUTH_CODE = 1

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fr_profile, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        loadBackground()

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        fr_profile_connect_gmail.setOnClickListener { init() }

    }


    private fun setUpResponse(){

        response = authStateManager.getResponse()!!

    }

    override fun onStart() {
        super.onStart()

        // If returns an object (rather than null), the user has already signed in with Google

        account = GoogleSignIn.getLastSignedInAccount(context)!!

    }



    private fun init(){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        var mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }


    private fun authForAccessToken(resp : AuthorizationResponse){

    }


    private fun verifyAccessToken(user_id : String, accessToken : String){

        RetrofitRepo().verifyUser(user_id, accessToken).observe(this, Observer<TheLoginResponse>{

            response = it!!
            val status = it.status

            println("REFRESH TOKEN 3 ".plus(response))

            when (status) {

                1       -> {

                    Toast.makeText(context, it.email.plus(" logged in with success"), Toast.LENGTH_SHORT).show()
                    loadLoggedInView()

                    authStateManager.storeResponse(response)
                    authStateManager.storeStateAfterAuthentication(authState?.lastTokenResponse!!, authState?.authorizationException)
                    authState = authStateManager.getState()!!

                }
                2       -> Toast.makeText(context, "Token has expired", Toast.LENGTH_SHORT).show()
                else    -> Toast.makeText(context, "Auth failed ".plus(status), Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun loadBackground(){

        fr_profile_constraint.background = ContextCompat.getDrawable(requireContext(), R.drawable.profile_backgound)

    }

    private fun loadLoggedInView(){

        logged_in_layout.visibility     = View.VISIBLE
        sign_in_layout.visibility       = View.GONE

        fr_profile_name.text = response.givenName.plus(" ").plus(response.familyName)

    }

    private fun loadSignInView(){

        logged_in_layout.visibility     = View.GONE
        sign_in_layout.visibility       = View.VISIBLE

    }
}