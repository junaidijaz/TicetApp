package au.net.tech.app.networking

import au.net.tech.app.AppConstants
import au.net.tech.app.AppSharedPrefs
import au.net.tech.app.models.*
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

interface NetworkService {

    @FormUrlEncoded
    @POST("authentication/login")
    fun loginUser(
        @Field("api_token_name") apiTokenName: String = AppConstants.API_TOKEN_NAME,
        @Field("api_token_key") apiTokenKey: String = AppConstants.API_TOKEN_KEY,
        @Field("email") email: String,
        @Field("password") password: String
    ): Single<User>


    @GET("tickets")
    fun getTickets(
        @Header("authtoken") authToken: String = AppSharedPrefs.getUser()?.authtoken?.trim() ?: ""
    ): Single<ArrayList<Ticket>>


    @GET("statuses")
    fun getTicketStatuses(
        @Header("authtoken") authToken: String = AppSharedPrefs.getUser()?.authtoken ?: ""
    ): Single<ArrayList<TicketStatus>>


    @GET("departments")
    fun getDepartments(
        @Header("authtoken") authToken: String = AppSharedPrefs.getUser()?.authtoken ?: ""
    ): Single<ArrayList<DepartmentDto>>


    @GET("priorities")
    fun getPriorities(
        @Header("authtoken") authToken: String = AppSharedPrefs.getUser()?.authtoken ?: ""
    ): Single<ArrayList<PrioritiesDto>>


    @FormUrlEncoded
    @POST("tickets")
    fun openTicket(
        @Header("authtoken") authToken: String = AppSharedPrefs.getUser()?.authtoken ?: "",
        @Field("department") department: String,
        @Field("priority") priority: String,
        @Field("subject") subject: String,
        @Field("message") message: String,
        @Field("contactid") contactid: String?
    ): Single<TicketSuccessDto>


    @Multipart
    @POST("tickets")
    fun openTicketWithImage(
        @Header("authtoken") authToken: String = AppSharedPrefs.getUser()?.authtoken!!,
        @Part("department") department: RequestBody,
        @Part("priority") priority: RequestBody,
        @Part("subject") subject: RequestBody,
        @Part("message") message: RequestBody,
        @Part("contactid") contactid: RequestBody?,
        @Part attachment: MultipartBody.Part
    ): Single<TicketSuccessDto>


//    @FormUrlEncoded
//    @POST("authentication/get_relation_data")
//    fun searchContact(
//        @Header("authtoken") authToken : String = "api_token_name=c464334cb8e11686f753ce85d51ec00c&api_token_key=b456e9c39c539002544b5fadf55c5f04&private_key=4f5d26c4b86f0d09088822eb7f4e1079&type=Staff&user_id=16&company_id=",
//        @Field("type") type: String = "contact",
//        @Field("q") q: String = "rahat@gmail.com",
//        @Field("tickets_contacts") tickets_contacts: String = "true"
//    ): Single<ArrayList<ContactDto>>


    @FormUrlEncoded
    @POST("authentication/get_relation_data")
    fun searchContact(
        @Header("authtoken") authToken: String = AppSharedPrefs.getUser()?.authtoken ?: "",
        @Field("type") type: String = "contact",
        @Field("q") q: String = "rahat",
        @Field("tickets_contacts") tickets_contacts: String = "true"

    ): Single<ArrayList<ContactDto>>


//
//    @FormUrlEncoded
//    @POST("authentication/get_relation_data")
//    fun searchContact(
//        @Header("authtoken") authToken: String = AppSharedPrefs.getUser()?.authtoken ?: "",
//        @Field("type") type: String = "contact",
//        @Field("q") q: String = "a",
//        @Field("tickets_contacts") tickets_contacts: String = "true"
//    ): Single<ArrayList<ContactDto>>


}