package au.net.tech.app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Company {
    @SerializedName("userid")
    @Expose
    var userid: String? = null

    @SerializedName("company")
    @Expose
    var company: String? = null

    @SerializedName("vat")
    @Expose
    var vat: String? = null

    @SerializedName("phonenumber")
    @Expose
    var phonenumber: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("zip")
    @Expose
    var zip: String? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("website")
    @Expose
    var website: String? = null

    @SerializedName("datecreated")
    @Expose
    var datecreated: String? = null

    @SerializedName("active")
    @Expose
    var active: String? = null

    @SerializedName("leadid")
    @Expose
    var leadid: Any? = null

    @SerializedName("billing_street")
    @Expose
    var billingStreet: String? = null

    @SerializedName("billing_city")
    @Expose
    var billingCity: String? = null

    @SerializedName("billing_state")
    @Expose
    var billingState: String? = null

    @SerializedName("billing_zip")
    @Expose
    var billingZip: String? = null

    @SerializedName("billing_country")
    @Expose
    var billingCountry: String? = null

    @SerializedName("shipping_street")
    @Expose
    var shippingStreet: String? = null

    @SerializedName("shipping_city")
    @Expose
    var shippingCity: String? = null

    @SerializedName("shipping_state")
    @Expose
    var shippingState: String? = null

    @SerializedName("shipping_zip")
    @Expose
    var shippingZip: String? = null

    @SerializedName("shipping_country")
    @Expose
    var shippingCountry: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: Any? = null

    @SerializedName("latitude")
    @Expose
    var latitude: Any? = null

    @SerializedName("default_language")
    @Expose
    var defaultLanguage: String? = null

    @SerializedName("default_currency")
    @Expose
    var defaultCurrency: String? = null

    @SerializedName("show_primary_contact")
    @Expose
    var showPrimaryContact: String? = null

    @SerializedName("stripe_id")
    @Expose
    var stripeId: Any? = null

    @SerializedName("registration_confirmed")
    @Expose
    var registrationConfirmed: String? = null

    @SerializedName("addedfrom")
    @Expose
    var addedfrom: String? = null
}