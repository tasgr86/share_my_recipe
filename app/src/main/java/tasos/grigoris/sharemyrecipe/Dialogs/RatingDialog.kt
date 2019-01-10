package tasos.grigoris.sharemyrecipe.Dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.rating_dialog.*
import tasos.grigoris.sharemyrecipe.R

class RatingDialog : DialogFragment() {

    private lateinit var _view: View
    var ratingListener: RatingListener? = null

    interface RatingListener {

        fun onRatingDialog(value : Int)

    }

    override fun onResume() {

        super.onResume()
        val params = dialog.window!!.attributes
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT
        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // view!!.findViewById(R.id.back_icon).setOnClickListener({ view -> dismiss() })

        return inflater.inflate(R.layout.rating_dialog, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        rating_dialog_ratingbar.rating = lastRating.toFloat()

        rating_dialog_submit.setOnClickListener {

            ratingListener?.onRatingDialog(rating_dialog_ratingbar.rating.toInt())

        }

        // dialog.window!!.attributes.windowAnimations = R.style.dialog_anim

    }

    companion object {

        var textSize = 0F
        var lastRating = 0

        fun newInstance(rating : Int): RatingDialog {

            lastRating = rating

            return RatingDialog()

        }
    }
}