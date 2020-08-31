package au.net.tech.app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("id")
    @Expose
    var id: String = ""

    @SerializedName("firstname")
    @Expose
    var firstname: String= ""

    @SerializedName("lastname")
    @Expose
    var lastname: String= ""

    @SerializedName("email")
    @Expose
    var email: String = ""

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("phonenumber")
    @Expose
    var phonenumber: String? = null

    @SerializedName("private_key")
    @Expose
    var privateKey: String? = null

    @SerializedName("api_token_name")
    @Expose
    var apiTokenName: String? = null

    @SerializedName("api_token_key")
    @Expose
    var apiTokenKey: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("authtoken")
    @Expose
    var authtoken: String? = null

    @SerializedName("mesibo_id")
    @Expose
    var mesibo_id: String? = null

    @SerializedName("mesibo_token")
    @Expose
    var mesibo_token: String? = null

}