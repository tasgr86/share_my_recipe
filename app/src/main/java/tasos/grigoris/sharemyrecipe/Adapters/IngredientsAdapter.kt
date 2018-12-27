package tasos.grigoris.sharemyrecipe.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.systatika_header_row.view.*
import kotlinx.android.synthetic.main.systatika_row.view.*
import tasos.grigoris.sharemyrecipe.Model.TheRecipes
import tasos.grigoris.sharemyrecipe.Model.TheIngredients
import tasos.grigoris.sharemyrecipe.Utils
import tasos.grigoris.sharemyrecipe.R

class IngredientsAdapter (private val context: Context, _array: ArrayList<TheIngredients>,
                          _recipe : TheRecipes, _textSize : Float, _racionesTextSize : Float)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var recipe                  = _recipe
    var quantityMultiplier      = recipe.posotita
    var quantityStep            = recipe.posotita
    var quantityTimes           = 1
    var array                   = _array
    var numUtils                = Utils()
    var HEADER_ROW              = 0
    var INGREDIENT_ROW          = 1
    var textSize                = _textSize
    var racionesTextSize        = _racionesTextSize

    override fun getItemViewType(position: Int): Int {

        return if (position == 0) HEADER_ROW
        else INGREDIENT_ROW

    }

    fun updateAdapter(size : Float, racionesSize : Float){

        textSize = size
        racionesTextSize = racionesSize
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == HEADER_ROW)
            HolderHeader(LayoutInflater.from(context).inflate(R.layout.systatika_header_row, view, false))
        else
            Holder(LayoutInflater.from(context).inflate(R.layout.systatika_row, view, false))

    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {

        if (holder is Holder) {

            val labelTitle : String = if (quantityTimes * array[pos].posotita!! > 1.0 && !array[pos].namePL.isNullOrEmpty())
                array[pos].namePL!!
            else
                array[pos].name!!

            holder.title.text = labelTitle

            val label : String = if (quantityTimes * array[pos].posotita!! > 1.0 && !array[pos].monadaPl.isNullOrEmpty())
                array[pos].monadaPl!!
            else
                array[pos].monada!!

            holder.posotita.text = numUtils.formatPosotita(quantityTimes * array[pos].posotita!!)
                .plus(" ").plus(label)

            holder.title.textSize = textSize
            holder.posotita.textSize = textSize

        } else{

            val quantityLabel : String = if (quantityTimes == 1)
                recipe.posotitaLabelSingle.toString()
            else
                recipe.posotitaLabelPlural.toString()

            (holder as HolderHeader).quantity.text = quantityMultiplier.toString().
                plus(" ").plus(quantityLabel)

            holder.quantity.textSize = racionesTextSize

        }

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val posotita = itemView.systatika_row_posotita
        val title = itemView.systatika_row_name

    }

    inner class HolderHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val quantity = itemView.systatika_header_row_quantity
        val plus = itemView.systatika_header_row_plus
        val minus = itemView.systatika_header_row_minus

        init {

            plus.setOnClickListener {

                if (quantityTimes <= 10) {

                    quantityMultiplier = quantityMultiplier!! + quantityStep!!
                    quantityTimes++
                    notifyDataSetChanged()

                }

            }

            minus.setOnClickListener {

                if (quantityTimes > 1) {

                    quantityMultiplier = quantityMultiplier!! - quantityStep!!
                    quantityTimes--
                    notifyDataSetChanged()

                }

            }
        }

    }
}
