package tasos.grigoris.sharemyrecipe.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.add_step_row2.view.*
import tasos.grigoris.sharemyrecipe.R

class AddIngredientsAdapter(private val context: Context, private var list: ArrayList<String>) : RecyclerView.Adapter<AddIngredientsAdapter.Holder>() {

    public var listener : ((list : ArrayList<String>) -> Unit)? = null

        override fun onCreateViewHolder(view: ViewGroup, viewType: Int): Holder {

            return Holder(LayoutInflater.from(context).inflate(R.layout.add_step_row2, view, false))

        }

        override fun getItemCount(): Int {

            return list.size

        }

        override fun onBindViewHolder(holder: Holder, pos: Int) {

            holder.name.setText(list[pos])
            holder.til.hint = context.getString(R.string.add_ingredient_title)
            holder.count.text = (1 + pos).toString()

        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val name     = itemView.add_step2_txt_et
            val til      = itemView.add_step2_txt
            val delete   = itemView.add_step_row2_delete
            var count    = itemView.add_step_row2_count

            init {

                name.addTextChangedListener(object : TextWatcher{

                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                        list[adapterPosition] = s.toString()

                    }
                })

                delete.setOnClickListener {

                    list.removeAt(layoutPosition)
                    notifyItemChanged(layoutPosition)

                    listener?.invoke(list)

                    list.forEach {

                        println("INGREDIENTS CHECK: ".plus(it))

                    }

                }

            }

        }

    }