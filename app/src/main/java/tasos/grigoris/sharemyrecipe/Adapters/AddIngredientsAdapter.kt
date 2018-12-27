package tasos.grigoris.sharemyrecipe.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.add_ingredients_row.view.*
import tasos.grigoris.sharemyrecipe.Model.TheIngredients
import tasos.grigoris.sharemyrecipe.R

class AddIngredientsAdapter(private val context: Context, private var list: ArrayList<TheIngredients>) : RecyclerView.Adapter<AddIngredientsAdapter.Holder>() {

        override fun onCreateViewHolder(view: ViewGroup, viewType: Int): Holder {

            return Holder(LayoutInflater.from(context).inflate(R.layout.add_ingredients_row, view, false))

        }

        override fun getItemCount(): Int {

            return list.size

        }

        override fun onBindViewHolder(holder: Holder, pos: Int) {

            holder.name.addTextChangedListener(object : TextWatcher{

                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    list[pos].name = s.toString()

                }
            })

            holder.monada.addTextChangedListener(object : TextWatcher{

                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    list[pos].monadaPl = s.toString()

                }
            })

            holder.amount.addTextChangedListener(object : TextWatcher{

                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    list[pos].posotita = s.toString().toDouble()

                }
            })

        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val name   = itemView.add_ingredients_name
            val amount = itemView.add_ingredients_amount
            val monada = itemView.add_ingredients_monada
            val delete = itemView.add_ingredients_row_delete

            init {

                delete.setOnClickListener {

                 //   if (layoutPosition != 0) {

                        list.removeAt(layoutPosition)
                        Toast.makeText(context, "removed ".plus(layoutPosition.toString()), Toast.LENGTH_SHORT).show()
                        notifyDataSetChanged()

                  //  }

                }

            }

        }

    }