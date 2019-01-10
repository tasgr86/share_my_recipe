package tasos.grigoris.sharemyrecipe.ui.main

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fr_profile.*
import tasos.grigoris.sharemyrecipe.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class FRProfile: Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fr_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadBackground()

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        fr_profile_connect_gmail.setOnClickListener {

            init()

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        println("ACTIVITY RESULT: ".plus(data))

        if (data != null) {

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


    private fun init(){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.clientID))
            .requestServerAuthCode(getString(R.string.clientID)).requestEmail().build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val intent = mGoogleSignInClient.signInIntent
        (context as Activity).startActivityForResult(intent, 1)

    }

    private fun loadBackground(){

        fr_profile_constraint.background = ContextCompat.getDrawable(requireContext(), R.drawable.profile_backgound)

    }


}
