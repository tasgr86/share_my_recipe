package tasos.grigoris.sharemyrecipe.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TheIngredients (@field:SerializedName("recipe_id") var recipeID: Int? = null,
                      @field:SerializedName("ingredient_id") var ingredientsID: Int? = null,
                      @field:SerializedName("name") var name: String? = null,
                      @field:SerializedName("name_pl") var namePL: String? = null,
                      @field:SerializedName("posotita") var posotita: Double? = null,
                      @field:SerializedName("monada") var monada: String? = null,
                      @field:SerializedName("monada_plural") var monadaPl: String? = null) : Parcelable
