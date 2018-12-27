package tasos.grigoris.sharemyrecipe.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TheAddRecipeReq(@field:SerializedName("posotites") val posotites: ArrayList<ThePosotites>? = null,
                      @field:SerializedName("categories") val categories: ArrayList<TheCategories>? = null) : Parcelable