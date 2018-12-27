package tasos.grigoris.sharemyrecipe.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.add_step_row.view.*
import tasos.grigoris.sharemyrecipe.Model.TheSteps
import tasos.grigoris.sharemyrecipe.R

class AddStepsAdapter(private val context: Context, private var list: ArrayList<TheSteps>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var STEP        = 0
    var SECTION     = 1

    override fun getItemViewType(position: Int): Int {

        return if (list[position].step_type == 1) STEP
        else SECTION

    }

    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == STEP)
            StepHolder(LayoutInflater.from(context).inflate(R.layout.add_step_row, view, false))
        else
            SectionHolder(LayoutInflater.from(context).inflate(R.layout.add_step_row, view, false))

    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {

        if (holder is SectionHolder){

            holder.stepCount.visibility = View.GONE
            holder.section.hint = context.getString(R.string.section_hint)

            holder.section.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    list[pos].step = s.toString()
                    list[pos].step_type = 0

                }
            })

        }else if (holder is StepHolder){

            holder.stepCount.text = (getStep(pos)).toString()

            holder.step.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    list[pos].step = s.toString()
                    list[pos].step_type = 1

                }
            })

        }

    }

    inner class SectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val stepCount    = itemView.add_step_row_count
        val section      = itemView.add_step_txt
        val delete       = itemView.add_step_row_delete

        init {

            delete.setOnClickListener {

                deleteRow(layoutPosition)

            }
        }
    }

    inner class StepHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val stepCount    = itemView.add_step_row_count
        val step         = itemView.add_step_txt
        val delete       = itemView.add_step_row_delete

        init {

            delete.setOnClickListener {

                deleteRow(layoutPosition)

            }
        }
    }


    private fun deleteRow(pos : Int){

        list.removeAt(pos)
        Toast.makeText(context, "removed ".plus(pos.toString()), Toast.LENGTH_SHORT).show()
        notifyDataSetChanged()

    }

    private fun getStep(pos : Int) : Int{

        var step = 0

        for (i in 0 .. pos){

            if (list[i].step_type == 1)
                step++

        }

        return step

    }

}