package tasos.grigoris.sharemyrecipe.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fr_profile.*
import tasos.grigoris.sharemyrecipe.Model.TheLoginResponse
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import tasos.grigoris.sharemyrecipe.R
import tasos.grigoris.sharemyrecipe.SignInManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import org.json.JSONObject
import tasos.grigoris.sharemyrecipe.RetrofitRepo

class FRProfile: Fragment() {

    private lateinit var viewModel              : MainViewModel
    private lateinit var signInManager          : SignInManager

    companion object {

        private const val AUTH_CODE = 1

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fr_profile, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadBackground()

        signInManager = SignInManager(requireContext())
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val account = signInManager.getAccount()

        if (account == null)
            loadSignInView()
        else
            loadLoggedInView(account)

        fr_profile_connect_gmail.setOnClickListener { signIn(signInManager.initGSO()) }

        fr_profile_sign_out.setOnClickListener {

            signInManager.initGSO()
            signInManager.signOut()
            loadSignInView()

        }

    }


    private fun signIn(signInClient : GoogleSignInClient) {

        val signInIntent = signInClient.signInIntent
        startActivityForResult(signInIntent, AUTH_CODE)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == AUTH_CODE) {

            // The Task returned from this call is always completed, no need to attach  a listener.

            try {

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                signInManager.updateAccount(account)

                // Signed in successfully, show authenticated UI.

                verifyAccessToken(account!!, account.idToken!!)

            }catch (e : ApiException){

                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.

                Toast.makeText(context, "FAILED ".plus(e.statusCode), Toast.LENGTH_LONG).show()

            }
        }
    }


    private fun verifyAccessToken(account : GoogleSignInAccount, accessToken : String){

        RetrofitRepo().verifyUser(accessToken).observe(this, Observer<String>{

            val status = JSONObject(it).getInt("status")

            when (status) {

                1 -> {

                    signInManager.updateAccount(account)
                    loadLoggedInView(account)

                }
                2 -> Toast.makeText(context, "Token verification has failed", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun loadBackground(){

        fr_profile_constraint.background = ContextCompat.getDrawable(requireContext(), R.drawable.profile_backgound)

    }

    private fun loadLoggedInView(account: GoogleSignInAccount){

        logged_in_layout.visibility     = View.VISIBLE
        sign_in_layout.visibility       = View.GONE

        fr_profile_name.text = account.givenName.plus(" ").plus(account.familyName)

    }

    private fun loadSignInView(){

        logged_in_layout.visibility     = View.GONE
        sign_in_layout.visibility       = View.VISIBLE

    }
}