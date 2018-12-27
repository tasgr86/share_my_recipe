package tasos.grigoris.sharemyrecipe.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.step_row.view.*
import kotlinx.android.synthetic.main.step_title.view.*
import tasos.grigoris.sharemyrecipe.Model.TheSteps
import tasos.grigoris.sharemyrecipe.R

class StepsAdapter (private val context: Context, _array: ArrayList<TheSteps>, _textSize : Float, _headerTextSize : Float)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var stepCount               = 1
    var array                   = _array
    var ROW                     = 0
    var TITLE                   = 1
    var textSize                = _textSize
    var headerTextSize          = _headerTextSize

    override fun getItemViewType(position: Int): Int {

        return if (array[position].step_type == 0)
            ROW
        else
            TITLE

    }


    fun updateAdapter(size : Float, headerSize : Float){

        stepCount = 1
        textSize = size
        headerTextSize = headerSize
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ROW)
            Holder(LayoutInflater.from(context).inflate(R.layout.step_row, view, false))
        else
            TitleHolder(LayoutInflater.from(context).inflate(R.layout.step_title, view, false))

    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {

        if (holder is Holder) {

            holder.text.text = array[pos].step
            holder.count.text = stepCount.toString()
            stepCount++

            holder.text.textSize = textSize

        }else {

            (holder as TitleHolder).title.text = array[pos].step
            holder.title.textSize = headerTextSize

        }

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text = itemView.step_row_text
        val count = itemView.step_row_count

    }

    inner class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.step_title_label

    }

}
