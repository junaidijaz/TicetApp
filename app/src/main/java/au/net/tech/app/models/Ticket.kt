@file:Suppress("SpellCheckingInspection")

package au.net.tech.app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Ticket  {
    @SerializedName("ticketid")
    @Expose
    var ticketid: String? = null

    @SerializedName("adminreplying")
    @Expose
    var adminreplying: String? = null

    @SerializedName("mesibo_group_id")
    @Expose
    var mesibo_group_id: String? = null

    @SerializedName("userid")
    @Expose
    var userid: String? = null

    @SerializedName("contactid")
    @Expose
    var contactid: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("department")
    @Expose
    var department: String? = null

    @SerializedName("priority")
    @Expose
    var priority: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("service")
    @Expose
    var service: Any? = null

    @SerializedName("ticketkey")
    @Expose
    var ticketkey: String? = null

    @SerializedName("cc")
    @Expose
    var cc: Any? = null

    @SerializedName("subject")
    @Expose
    var subject: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("admin")
    @Expose
    var admin: Any? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("project_id")
    @Expose
    var projectId: String? = null

    @SerializedName("lastreply")
    @Expose
    var lastreply: Any? = null

    @SerializedName("clientread")
    @Expose
    var clientread: String? = null

    @SerializedName("adminread")
    @Expose
    var adminread: String? = null

    @SerializedName("ip")
    @Expose
    var ip: String? = null

    @SerializedName("assigned")
    @Expose
    var assigned: String? = null

    @SerializedName("departmentid")
    @Expose
    var departmentid: String? = null

    @SerializedName("imap_username")
    @Expose
    var imapUsername: String? = null

    @SerializedName("email_from_header")
    @Expose
    var emailFromHeader: String? = null

    @SerializedName("host")
    @Expose
    var host: String? = null

    @SerializedName("password")
    @Expose
    var password: Any? = null

    @SerializedName("encryption")
    @Expose
    var encryption: String? = null

    @SerializedName("delete_after_import")
    @Expose
    var deleteAfterImport: String? = null

    @SerializedName("calendar_id")
    @Expose
    var calendarId: String? = null

    @SerializedName("hidefromclient")
    @Expose
    var hidefromclient: String? = null

    @SerializedName("ticketstatusid")
    @Expose
    var ticketstatusid: String? = null

    @SerializedName("isdefault")
    @Expose
    var isdefault: String? = null

    @SerializedName("statuscolor")
    @Expose
    var statuscolor: String? = null

    @SerializedName("statusorder")
    @Expose
    var statusorder: String? = null

    @SerializedName("serviceid")
    @Expose
    var serviceid: Any? = null

    @SerializedName("company")
    @Expose
    var company: String? = null

    @SerializedName("vat")
    @Expose
    var vat: String? = null

    @SerializedName("phonenumber")
    @Expose
    var phonenumber: Any? = null

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
    var datecreated: Any? = null

    @SerializedName("active")
    @Expose
    var active: Any? = null

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
    var defaultLanguage: Any? = null

    @SerializedName("default_currency")
    @Expose
    var defaultCurrency: String? = null

    @SerializedName("show_primary_contact")
    @Expose
    var showPrimaryContact: String? = null

    @SerializedName("addedfrom")
    @Expose
    var addedfrom: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("is_primary")
    @Expose
    var isPrimary: String? = null

    @SerializedName("firstname")
    @Expose
    var firstname: Any? = null

    @SerializedName("lastname")
    @Expose
    var lastname: Any? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("new_pass_key")
    @Expose
    var newPassKey: Any? = null

    @SerializedName("new_pass_key_requested")
    @Expose
    var newPassKeyRequested: Any? = null

    @SerializedName("last_ip")
    @Expose
    var lastIp: Any? = null

    @SerializedName("last_login")
    @Expose
    var lastLogin: Any? = null

    @SerializedName("last_password_change")
    @Expose
    var lastPasswordChange: Any? = null

    @SerializedName("profile_image")
    @Expose
    var profileImage: Any? = null

    @SerializedName("direction")
    @Expose
    var direction: Any? = null

    @SerializedName("invoice_emails")
    @Expose
    var invoiceEmails: String? = null

    @SerializedName("estimate_emails")
    @Expose
    var estimateEmails: String? = null

    @SerializedName("credit_note_emails")
    @Expose
    var creditNoteEmails: String? = null

    @SerializedName("contract_emails")
    @Expose
    var contractEmails: String? = null

    @SerializedName("task_emails")
    @Expose
    var taskEmails: String? = null

    @SerializedName("project_emails")
    @Expose
    var projectEmails: String? = null

    @SerializedName("private_key")
    @Expose
    var privateKey: Any? = null

    @SerializedName("staffid")
    @Expose
    var staffid: Any? = null

    @SerializedName("facebook")
    @Expose
    var facebook: Any? = null

    @SerializedName("linkedin")
    @Expose
    var linkedin: Any? = null

    @SerializedName("skype")
    @Expose
    var skype: Any? = null

    @SerializedName("last_activity")
    @Expose
    var lastActivity: Any? = null

    @SerializedName("role")
    @Expose
    var role: Any? = null

    @SerializedName("media_path_slug")
    @Expose
    var mediaPathSlug: Any? = null

    @SerializedName("is_not_staff")
    @Expose
    var isNotStaff: Any? = null

    @SerializedName("hourly_rate")
    @Expose
    var hourlyRate: Any? = null

    @SerializedName("two_factor_auth_enabled")
    @Expose
    var twoFactorAuthEnabled: Any? = null

    @SerializedName("two_factor_auth_code")
    @Expose
    var twoFactorAuthCode: Any? = null

    @SerializedName("two_factor_auth_code_requested")
    @Expose
    var twoFactorAuthCodeRequested: Any? = null

    @SerializedName("email_signature")
    @Expose
    var emailSignature: Any? = null

    @SerializedName("dashboard_widgets_order")
    @Expose
    var dashboardWidgetsOrder: Any? = null

    @SerializedName("dashboard_widgets_visibility")
    @Expose
    var dashboardWidgetsVisibility: Any? = null

    @SerializedName("priorityid")
    @Expose
    var priorityid: String? = null

    @SerializedName("from_name")
    @Expose
    var fromName: Any? = null

    @SerializedName("ticket_email")
    @Expose
    var ticketEmail: Any? = null

    @SerializedName("department_name")
    @Expose
    var departmentName: String? = null

    @SerializedName("priority_name")
    @Expose
    var priorityName: String? = null

    @SerializedName("service_name")
    @Expose
    var serviceName: Any? = null

    @SerializedName("status_name")
    @Expose
    var statusName: String? = null

    @SerializedName("user_firstname")
    @Expose
    var userFirstname: String? = null

    @SerializedName("user_lastname")
    @Expose
    var userLastname: String? = null

    @SerializedName("staff_firstname")
    @Expose
    var staffFirstname: Any? = null

    @SerializedName("staff_lastname")
    @Expose
    var staffLastname: Any? = null

}