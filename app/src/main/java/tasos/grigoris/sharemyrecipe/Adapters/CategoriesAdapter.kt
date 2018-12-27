package tasos.grigoris.sharemyrecipe.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_row.view.*
import tasos.grigoris.sharemyrecipe.Model.TheCategories
import tasos.grigoris.sharemyrecipe.R
import tasos.grigoris.sharemyrecipe.ui.main.RecipesOfCategory

class CategoriesAdapter (private val context: Context, _array: ArrayList<TheCategories>)
    : RecyclerView.Adapter<CategoriesAdapter.Holder>() {

    var pic_url = "https://simplebudget.eu/recipes/pictures/categories/"
    var array = _array

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {

        return Holder(LayoutInflater.from(context).inflate(R.layout.category_row, p0, false))

    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: Holder, pos: Int) {

        holder.title.text = array[pos].name

        println("bind: ".plus(array[pos].name).plus(" ").plus(array[pos].photo))

        Picasso.get()
            .load(pic_url.plus(array[pos].photo))
            .into(holder.background)

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.category_row_title
        val background = itemView.category_row_background

        init {

            itemView.setOnClickListener {

                println("categories adapter ".plus(array[layoutPosition].id))

                val intent = Intent(context, RecipesOfCategory::class.java)
                intent.putExtra("category_id", array[layoutPosition].id)
                context.startActivity(intent)

            }

        }

    }
}
