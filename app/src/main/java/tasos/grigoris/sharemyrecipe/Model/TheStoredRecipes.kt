package tasos.grigoris.sharemyrecipe.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TheStoredRecipes (var recipe : TheRecipes, var steps : ArrayList<TheSteps>, var ingredients: ArrayList<TheIngredients>,
                             var isFavorite : Boolean) : Parcelable