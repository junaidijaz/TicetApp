package au.net.tech.app.networking.mesibo

import au.net.tech.app.AppConstants
import au.net.tech.app.AppSharedPrefs
import au.net.tech.app.models.MesiboUser
import au.net.tech.app.models.User
import io.reactivex.Single
import retrofit2.http.*

interface MesiboNetworkService {

    @POST("api.php?")
    fun refreshMesiboToken(
        @Query("op") op: String = "useradd",
        @Query("token") mesiboAppToken: String = AppConstants.MESIBO_APP_TOKEN,
        @Query("addr") userEmail: String,
        @Query("appid") appid: String = "au.net.tech.app"
    ): Single<MesiboUser>


    @POST("api.php?")
    fun addMemberToGroup(
        @Query("op") op: String = "groupeditmembers",
        @Query("token") mesiboAppToken: String = AppConstants.MESIBO_APP_TOKEN,
        @Query("gid") gid: String,
        @Query("delete") delete: String = "0",
        @Query("m") userToBeAdded: String = AppSharedPrefs.getUser()?.mesibo_id!!,
        @Query("cs") cs: String = "1",
        @Query("cr") cr: String = "1",
        @Query("canpub") canpub: String = "0",
        @Query("cansub") cansub: String = "0",
        @Query("canlist ") canlist: String = "0"

    ): Single<MesiboUser>


}