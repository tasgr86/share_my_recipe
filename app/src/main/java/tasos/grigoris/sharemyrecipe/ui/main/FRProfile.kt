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

class FRProfile: Fragment() {

    private lateinit var viewModel              : MainViewModel
    private lateinit var authorizationService   : AuthorizationService
    private var authState                       : AuthState? = null
    private lateinit var response               : TheLoginResponse
    private lateinit var authStateManager       : AuthStateManager

    companion object {

        private const val AUTH_CODE = 1

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fr_profile, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        authorizationService = AuthorizationService(view!!.context)
        authStateManager = AuthStateManager(requireContext())
        authState = authStateManager.getState()

        if (authState == null){

            loadSignInView()
            init()

        }else {

            if (authState?.needsTokenRefresh!!) {

                verify()

            } else {

                setUpResponse()
                loadLoggedInView()
                println("Token will expire on: ".plus(authState?.accessTokenExpirationTime))

            }
        }

        loadBackground()

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        fr_profile_connect_gmail.setOnClickListener { init() }

    }


    private fun setUpResponse(){

        response = authStateManager.getResponse()!!

    }

    private fun verify(){

        println("REFREST TOKEN 1")

        authState?.performActionWithFreshTokens(authorizationService) { accessToken, idToken, ex ->

            if (ex != null){

                Toast.makeText(requireContext(), "Could not refresh tokens ... Logging out ...",
                    Toast.LENGTH_SHORT).show()

                authStateManager.deleteResponse()
                authStateManager.deleteState()
                loadSignInView()

            }else {

                println("REFREST TOKEN 2 ".plus(idToken!!).plus(" ").plus(accessToken!!))
                verifyAccessToken(idToken!!, accessToken!!)

            }
        }
    }


    private fun init(){

        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse("https://accounts.google.com/o/oauth2/v2/auth") /* auth endpoint */,
            Uri.parse("https://www.googleapis.com/oauth2/v4/token") /* token endpoint */
        )

        authState = AuthState(serviceConfig)

        authStateManager.setState(authState!!)

        val clientId = getString(R.string.clientID)
        val redirectUri = Uri.parse(getString(R.string.auth_uri))
        val builder = AuthorizationRequest.Builder(serviceConfig, clientId, ResponseTypeValues.CODE, redirectUri)

        builder.setScopes("openid profile email")
        val request = builder.build()

        val authService = AuthorizationService(requireContext())
        val authIntent = authService.getAuthorizationRequestIntent(request)
        startActivityForResult(authIntent, AUTH_CODE)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val resp = AuthorizationResponse.fromIntent(data!!)
        val ex = AuthorizationException.fromIntent(data)

        println("auth state manager ".plus(authStateManager))
        println("resp: ".plus(resp))
        println("ex: ".plus(ex))

        authStateManager.storeStateAfterAuthorization(resp, ex)

        authForAccessToken(resp!!)

        println("STEP 1 authorization code : ".plus(resp.authorizationCode))
        println("STEP 1: ".plus(ex))

    }


    private fun authForAccessToken(resp : AuthorizationResponse){

        authorizationService.performTokenRequest(           // Exchange authorization token for access token with the authorization server

            resp.createTokenExchangeRequest()) { respT, exT ->

            if (respT != null) {

                // exchange succeeded

                // ID Token asserts the user's identity (OpenID)
                // Access token retrieves consented user info

                authStateManager.storeStateAfterAuthentication(respT, exT)
                authState = authStateManager.getState()!!
                verify()

            } else {

                println("authorization failed")

            }
        }
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