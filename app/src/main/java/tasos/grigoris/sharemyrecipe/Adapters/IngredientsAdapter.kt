package tasos.grigoris.sharemyrecipe.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
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
    var numUtils                = Utils(context)
    var HEADER_ROW              = 0
    var INGREDIENT_ROW          = 1
    var textSize                = _textSize
    var racionesTextSize        = _racionesTextSize
    var playAnimation           = false

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
        return array.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {

        if (holder is Holder) {

            val _pos = pos - 1

            val labelTitle : String = if (quantityTimes * array[_pos].posotita!! > 1.0 && !array[_pos].namePL.isNullOrEmpty())
                array[_pos].namePL!!
            else
                array[_pos].name!!

            holder.title.text = labelTitle

            val label : String = if (quantityTimes * array[_pos].posotita!! > 1.0 && !array[_pos].monadaPl.isNullOrEmpty())
                array[_pos].monadaPl!!
            else
                array[_pos].monada!!

            holder.posotita.text = numUtils.formatPosotita(quantityTimes * array[_pos].posotita!!)
                .plus(" ").plus(label)

            holder.title.textSize = textSize
            holder.posotita.textSize = textSize

        } else{

            println("TEST BIND ".plus(recipe.posotitaLabelSingle).plus(" ").plus(recipe.posotitaLabelPlural))

            val quantityLabel : String = if (quantityMultiplier == 1)
                recipe.posotitaLabelSingle.toString()
            else
                recipe.posotitaLabelPlural.toString()

            (holder as HolderHeader).quantityNum.text = quantityMultiplier.toString()
            (holder as HolderHeader).quantityLabel.text = quantityLabel

            holder.quantityNum.textSize = racionesTextSize + 4
            holder.quantityLabel.textSize = racionesTextSize

            if (playAnimation)
                runAnimation(holder.quantityNum)

        }
    }

    fun runAnimation(tv : TextView) {

        val a = AnimationUtils.loadAnimation(context, R.anim.txt_animation)
        a.reset()
        tv.clearAnimation()
        tv.startAnimation(a)

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val posotita = itemView.systatika_row_posotita
        val title = itemView.systatika_row_name

    }

    inner class HolderHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val quantityNum = itemView.systatika_header_row_quantity_num
        val quantityLabel = itemView.systatika_header_row_quantity_label
        val plus = itemView.systatika_header_row_plus
        val minus = itemView.systatika_header_row_minus

        init {

            plus.setOnClickListener {

                if (quantityTimes < 10) {

                    quantityMultiplier = quantityMultiplier!! + quantityStep!!
                    quantityTimes++
                    notifyDataSetChanged()
                    playAnimation = true

                }

            }

            minus.setOnClickListener {

                if (quantityTimes > 1) {

                    quantityMultiplier = quantityMultiplier!! - quantityStep!!
                    quantityTimes--
                    notifyDataSetChanged()
                    playAnimation = true

                }

            }


        }

    }
}
