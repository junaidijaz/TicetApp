package au.net.tech.app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ContactDto {

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("addedfrom")
    @Expose
    var addedfrom: String? = null

    @SerializedName("subtext")
    @Expose
    var subtext: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

}