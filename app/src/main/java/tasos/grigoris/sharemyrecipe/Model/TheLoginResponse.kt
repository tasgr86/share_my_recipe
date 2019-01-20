package tasos.grigoris.sharemyrecipe.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TheLoginResponse(@field:SerializedName("status") val status: Int? = null,
                    @field:SerializedName("msg") val msg: String? = null,
                    @field:SerializedName("user_id") val userID: String? = null,
                    @field:SerializedName("given_name") val givenName: String? = null,
                    @field:SerializedName("family_name") val familyName: String? = null,
                    @field:SerializedName("email") val email: String? = null) : Parcelable