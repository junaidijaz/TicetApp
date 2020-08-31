package au.net.tech.app.models

import com.google.gson.annotations.SerializedName

data class TicketSuccessDto(
    @SerializedName("status")
    var status: Boolean = false,

    @SerializedName("ticket_id")
    var ticket_id: String? = null,

    @SerializedName("ticket_group_id")
    var ticket_group_id: String?  = null,

    @SerializedName("message")
    var message: String?  = null

)