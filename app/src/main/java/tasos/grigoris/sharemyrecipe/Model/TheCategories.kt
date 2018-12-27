package tasos.grigoris.sharemyrecipe.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TheCategories(@field:SerializedName("category_id") val id: Int? = null,
                    @field:SerializedName("name") val name: String? = null,
                    @field:SerializedName("photo") val photo: String? = null) : Parcelable