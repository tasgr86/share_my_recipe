package tasos.grigoris.sharemyrecipe.Adapters

import java.util.ArrayList
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import tasos.grigoris.sharemyrecipe.Model.TheRecipes
import tasos.grigoris.sharemyrecipe.R

class FoldingRecipesAdapter(var context: Context, var recipes: ArrayList<TheRecipes>) : BaseAdapter(), View.OnClickListener  {

    override fun onClick(view: View?) {

//        val item = view?.getTag(R.id.list_item_image) as Painting
//        val activity = ContextHelper.asActivity(view.getContext())
//
//        if (activity is UnfoldableDetailsActivity) {
//            (activity as UnfoldableDetailsActivity).openDetails(view, item)
//        } else if (activity is FoldableListActivity) {
//            Toast.makeText(activity, item.getTitle(), Toast.LENGTH_SHORT).show()
//        }


        listener?.invoke(view!!)

     }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var pic_url = "https://simplebudget.eu/recipes/pictures/recipes/"
    var listener: ((view : View)->Unit)? = null

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
            convertView = inflater.inflate(R.layout.recipe_row_2, parent, false)

            holder.title = convertView!!.findViewById<View>(R.id.recipe_row2_title) as TextView
//            holder.category = convertView!!.findViewById<View>(R.id.recipe_row_category) as TextView
//            holder.preparatioTime = convertView!!.findViewById<View>(R.id.recipe_row_preparation_time) as TextView
            holder.image = convertView.findViewById(R.id.recipe_row2_feature) as ImageView

            convertView.tag = holder

        } else {

            holder = convertView.tag as ViewHolder

        }

        holder.title!!.text = recipes[position].title
//        holder.category!!.text = recipes[position].categoryName
//        holder.preparatioTime!!.text = recipes[position].preparationTime.toString().plus(" ").plus(context.getString(R.string.minutes))

        Picasso.get()
            .load(pic_url.plus(recipes[position].photoFeature))
            .into(holder.image)
//
//        convertView.setOnClickListener {
//
////            val intent = Intent(context, ShowRecipe::class.java)
////            intent.putExtra("recipe", recipes[position])
////            intent.putExtra("id", recipes[position].id)
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////            context.startActivity(intent)
//
//            listener?.invoke(it)
//
//        }

        return convertView

    }

    internal class ViewHolder {

        var title: TextView? = null
        var preparatioTime: TextView? = null
        var category: TextView? = null
        var image: ImageView? = null

    }

}