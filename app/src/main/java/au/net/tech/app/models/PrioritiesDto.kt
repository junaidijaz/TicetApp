@file:Suppress("SpellCheckingInspection")

package au.net.tech.app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PrioritiesDto  {
    @SerializedName("priorityid")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

}