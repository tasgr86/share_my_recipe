package tasos.grigoris.sharemyrecipe.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TheStepsAndIngredients(@field:SerializedName("steps") val steps: ArrayList<TheSteps>? = null,
                             @field:SerializedName("ingredients") val ingredients: ArrayList<TheIngredients>? = null,
                             @field:SerializedName("myRating") val myRating: Int? = null,
                             @field:SerializedName("avg") val avg: Double? = null) : Parcelable