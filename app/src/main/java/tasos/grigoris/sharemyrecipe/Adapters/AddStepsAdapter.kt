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
import kotlinx.android.synthetic.main.add_step_row2.view.*
import tasos.grigoris.sharemyrecipe.Model.TheSteps
import tasos.grigoris.sharemyrecipe.R

class AddStepsAdapter(private val context: Context, private var list: ArrayList<TheSteps>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var STEP        = 0
    var SECTION     = 1
    public var listener    : ((list : ArrayList<TheSteps>) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {

        return if (list[position].step_type == 1) STEP
        else SECTION

    }

    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == STEP)
            StepHolder(LayoutInflater.from(context).inflate(R.layout.add_step_row2, view, false))
        else
            SectionHolder(LayoutInflater.from(context).inflate(R.layout.add_step_row2, view, false))

    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {

        if (holder is SectionHolder){

            holder.tilSection.hint = context.getString(R.string.add_section)
            holder.section.setText(list[pos].section)
            holder.sectionCount.text = "Ε".plus(getSection(pos))

        }else if (holder is StepHolder){

            holder.stepCount.text = (getStep(pos)).toString()
            holder.tilStep.hint = context.getString(R.string.add_step)
            holder.step.setText(list[pos].step)

        }

    }

    inner class SectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tilSection      = itemView.add_step2_txt
        val sectionCount    = itemView.add_step_row2_count
        val section         = itemView.add_step2_txt_et
        val delete          = itemView.add_step_row2_delete

        init {

            section.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    list[adapterPosition].section = s.toString()
                    list[adapterPosition].step_type = 0

                }
            })

            delete.setOnClickListener {

                deleteRow(layoutPosition)

            }
        }
    }

    inner class StepHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tilStep      = itemView.add_step2_txt
        var stepCount    = itemView.add_step_row2_count
        var step         = itemView.add_step2_txt_et
        var delete       = itemView.add_step_row2_delete

        init {

            step.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if (!s.isNullOrEmpty()) {

                        list[adapterPosition].step = s.toString()
                        list[adapterPosition].step_type = 1

                    }

                }
            })

            delete.setOnClickListener {

                deleteRow(layoutPosition)

            }
        }
    }


    private fun deleteRow(pos : Int){

        list.removeAt(pos)
        Toast.makeText(context, "removed ".plus(pos.toString()), Toast.LENGTH_SHORT).show()
        notifyDataSetChanged()
        listener?.invoke(list)

        list.forEach {

            println("STEPS CHECK: ".plus(it.step).plus(" section: ").plus(it.section).plus(" ").plus(it.step_type))

        }

    }

    private fun getStep(pos : Int) : Int{

        var step = 0

        for (i in 0 .. pos){

            if (list[i].step_type == 1)
                step++

        }

        return step

    }


    private fun getSection(pos : Int) : Int{

        var step = 0

        for (i in 0 .. pos){

            if (list[i].step_type == 0)
                step++

        }

        return step

    }

}