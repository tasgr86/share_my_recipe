package tasos.grigoris.sharemyrecipe.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fr_profile.*
import tasos.grigoris.sharemyrecipe.R

class FRProfile: Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fr_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadBackground()

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)



    }

    private fun loadBackground(){

        fr_profile_constraint.background = ContextCompat.getDrawable(requireContext(), R.drawable.profile_backgound)

    }

}
