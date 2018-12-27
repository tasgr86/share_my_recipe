package tasos.grigoris.sharemyrecipe.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ThePosotites(@field:SerializedName("posotita_id") val posotitaID: Int? = null,
                    @field:SerializedName("posotita_label_single") val posotitaLabelSingle: String? = null,
                    @field:SerializedName("posotita_label_plural") val posotitaLabelPlural: String? = null) : Parcelable