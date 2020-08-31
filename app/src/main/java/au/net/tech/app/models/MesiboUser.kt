package au.net.tech.app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MesiboUser {
    @SerializedName("app")
    @Expose
    var app: App? = null

    @SerializedName("uts")
    @Expose
    var uts: String? = null

    @SerializedName("disabled")
    @Expose
    var disabled: Int? = null

    @SerializedName("user")
    @Expose
    var user: User? = null

    @SerializedName("op")
    @Expose
    var op: String? = null

    @SerializedName("result")
    @Expose
    var result: Boolean? = null

    inner class App {
        @SerializedName("aid")
        @Expose
        var aid: String? = null

        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("secret")
        @Expose
        var secret: String? = null

        @SerializedName("u_users")
        @Expose
        var uUsers: String? = null

        @SerializedName("u_groups")
        @Expose
        var uGroups: String? = null

        @SerializedName("d_storage")
        @Expose
        var dStorage: String? = null

        @SerializedName("url")
        @Expose
        var url: String? = null

        @SerializedName("server")
        @Expose
        var server: String? = null

        @SerializedName("notify")
        @Expose
        var notify: String? = null

        @SerializedName("nrate")
        @Expose
        var nrate: String? = null

        @SerializedName("ninterval")
        @Expose
        var ninterval: String? = null

        @SerializedName("flag")
        @Expose
        var flag: String? = null

        @SerializedName("f_beta")
        @Expose
        var fBeta: String? = null

        @SerializedName("ts")
        @Expose
        var ts: String? = null

        @SerializedName("uts")
        @Expose
        var uts: String? = null

        @SerializedName("fcm_id")
        @Expose
        var fcmId: String? = null

        @SerializedName("fcm_key")
        @Expose
        var fcmKey: String? = null

        @SerializedName("apn_info")
        @Expose
        var apnInfo: String? = null

        @SerializedName("pushflags")
        @Expose
        var pushflags: String? = null

        @SerializedName("token")
        @Expose
        var token: String? = null

    }

    inner class User {
        @SerializedName("uid")
        @Expose
        var uid: String? = null

        @SerializedName("token")
        @Expose
        var token: String? = null

    }
}