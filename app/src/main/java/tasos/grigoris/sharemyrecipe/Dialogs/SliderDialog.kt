package tasos.grigoris.sharemyrecipe.Dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.view.Gravity
import kotlinx.android.synthetic.main.slider_dialog.*
import tasos.grigoris.sharemyrecipe.R

class SliderDialog : DialogFragment() {

    private lateinit var _view: View
    var sliderListener: SliderListener? = null

    interface SliderListener {

        fun onSliderDialog(value : Int)

    }

    override fun onResume() {

        super.onResume()
        val params = dialog.window!!.attributes.apply {

            width = RelativeLayout.LayoutParams.MATCH_PARENT
            height = RelativeLayout.LayoutParams.WRAP_CONTENT

        }
        dialog.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.slider_dialog, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        slider_dialog_seekbar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                slider_dialog_size.text = (10 + progress).toString()

                sliderListener?.onSliderDialog(progress + 10)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        slider_dialog_seekbar.progress = textSize.toInt()

    }

    companion object {

        var textSize = 0F

        fun newInstance(_textSize : Float): SliderDialog {

            textSize = _textSize - 10

            return SliderDialog()

        }
    }
}