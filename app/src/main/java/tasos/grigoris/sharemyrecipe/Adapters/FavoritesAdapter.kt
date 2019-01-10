package tasos.grigoris.sharemyrecipe.Adapters

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recipe_row_2.view.*
import tasos.grigoris.sharemyrecipe.Model.TheStoredRecipes
import tasos.grigoris.sharemyrecipe.R
import tasos.grigoris.sharemyrecipe.ui.main.ShowRecipe

class FavoritesAdapter (_context: Context, _array: ArrayList<TheStoredRecipes>)
    : RecyclerView.Adapter<FavoritesAdapter.Holder>() {

    var pic_url = "https://simplebudget.eu/recipes/pictures/recipes/"
    var array = _array
    val context = _context

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {

        return Holder(LayoutInflater.from(context).inflate(R.layout.recipe_row_2, p0, false))

    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: Holder, pos: Int) {

        holder.title.text = array[pos].recipe.title

        Picasso.get()
            .load(pic_url.plus(array[pos].recipe.photo1))
            .into(holder.image)

        holder.tint.setBackgroundColor(ContextCompat.getColor(context, R.color.tint_black))

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title = itemView.recipe_row2_title
        val image = itemView.recipe_row2_feature
        val tint = itemView.recipe_row2_info

        init {

            itemView.setOnClickListener {

                val intent = Intent(context, ShowRecipe::class.java)
                intent.putExtra("recipe", array[layoutPosition].recipe)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)

            }

        }
    }

}
