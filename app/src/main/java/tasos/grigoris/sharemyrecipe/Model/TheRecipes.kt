package tasos.grigoris.sharemyrecipe.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TheRecipes(@field:SerializedName("recipe_id") val id: Int,
                 @field:SerializedName("recipe_title") val title: String? = null,
                 @field:SerializedName("date_added") val dateAdded: Int? = null,
                 @field:SerializedName("preparation_time") val preparationTime: Int? = null,
                 @field:SerializedName("short_instructions") val shortInstructions: String? = null,
                 @field:SerializedName("category_id") val categoryID: Int? = null,
                 @field:SerializedName("name") val categoryName: String? = null,
                 @field:SerializedName("nationality_id") val nationalityId: Int? = null,
                 @field:SerializedName("difficulty") val difficulty: Int? = null,
                 @field:SerializedName("url") val url: String? = null,
                 @field:SerializedName("user_id") val userId: String? = null,
                 @field:SerializedName("photo_feature") val photoFeature: String? = null,
                 @field:SerializedName("photo1") val photo1: String? = null,
                 @field:SerializedName("photo2") val photo2: String? = null,
                 @field:SerializedName("posotita") val posotita: Int? = null,
                 @field:SerializedName("posotita_label_single") val posotitaLabelSingle: String? = null,
                 @field:SerializedName("posotita_label_plural") val posotitaLabelPlural: String? = null,
                 @field:SerializedName("avg") val avg: Double? = null) : Parcelable
