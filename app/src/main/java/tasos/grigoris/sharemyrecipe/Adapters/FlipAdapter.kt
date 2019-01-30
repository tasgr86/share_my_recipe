package tasos.grigoris.sharemyrecipe.Adapters

import java.util.ArrayList
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import tasos.grigoris.sharemyrecipe.Model.TheRecipes
import tasos.grigoris.sharemyrecipe.R
import tasos.grigoris.sharemyrecipe.ui.main.ShowRecipe

class FlipAdapter(var context: Context, var recipes: ArrayList<TheRecipes>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var pic_url = "https://simplebudget.eu/recipes/pictures/recipes/"

    override fun getCount(): Int {
        return recipes.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return recipes[position].id.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {

            holder = ViewHolder()
            convertView = inflater.inflate(R.layout.recipe_row, parent, false)

            holder.title = convertView!!.findViewById<View>(R.id.recipe_row_title) as TextView
            holder.category = convertView.findViewById<View>(R.id.recipe_row_category) as TextView
            holder.preparatioTime = convertView.findViewById<View>(R.id.recipe_row_preparation_time) as TextView
            holder.image = convertView.findViewById(R.id.recipe_row_feature) as ImageView

            convertView.tag = holder

        } else {

            holder = convertView.tag as ViewHolder

        }

        holder.title!!.text = recipes[position].title
        holder.category!!.text = recipes[position].categoryName
        holder.preparatioTime!!.text = recipes[position].preparationTime.toString().plus(" ").plus(context.getString(R.string.minutes_cap))

        Picasso.get().load(pic_url.plus(recipes[position].photoFeature)).into(holder.image)

        convertView.setOnClickListener {

            val intent = Intent(context, ShowRecipe::class.java).apply {

                putExtra("recipe", recipes[position])
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            }
            context.startActivity(intent)

        }

        return convertView

    }

    internal class ViewHolder {

        var title: TextView? = null
        var preparatioTime: TextView? = null
        var category: TextView? = null
        var image: ImageView? = null

    }

}