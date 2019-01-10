package tasos.grigoris.sharemyrecipe.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
class TheRecipeToSend(@field:SerializedName("file1") val file1: File? = null,
                      @field:SerializedName("file2") val file2: File? = null,
                      @field:SerializedName("title") val title: String? = null,
                      @field:SerializedName("steps") val steps: ArrayList<TheSteps>? = null,
                      @field:SerializedName("ingredients") val ingredients: ArrayList<String>? = null,
                      @field:SerializedName("shortDesc") val shortDesc: String? = null,
                      @field:SerializedName("posotitaID") val posotitaID: String? = null,
                      @field:SerializedName("posotitaLabel") val posotitaLabel: String? = null,
                      @field:SerializedName("categoryID") val categoryID: String? = null,
                      @field:SerializedName("prepTime") val prepTime: String? = null,
                      @field:SerializedName("nationality") val nationality: String? = null,
                      @field:SerializedName("difficulty") val difficulty: String? = null,
                      @field:SerializedName("userID") val userID: String? = null) : Parcelable