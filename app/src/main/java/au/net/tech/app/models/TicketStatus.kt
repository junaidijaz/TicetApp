@file:Suppress("SpellCheckingInspection")

package au.net.tech.app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TicketStatus {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("statuscolor")
    @Expose
    var statuscolor: String? = null

    @SerializedName("order")
    @Expose
    var order: String? = null

}