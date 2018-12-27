package tasos.grigoris.sharemyrecipe.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TheSteps(@field:SerializedName("recipe_id") var recipe_id: Int? = null,
                    @field:SerializedName("step_id") var step_id: Int? = null,
                    @field:SerializedName("steps") var step: String? = null,
                    @field:SerializedName("step_type") var step_type: Int? = null,
                    @field:SerializedName("section") var section: String? = null) : Parcelable