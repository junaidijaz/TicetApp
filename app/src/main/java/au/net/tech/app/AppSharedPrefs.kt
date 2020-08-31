package au.net.tech.app

import android.content.SharedPreferences
import android.preference.PreferenceManager
import au.net.tech.app.models.TicketStatus
import au.net.tech.app.models.User

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object AppSharedPrefs {

    fun saveUser(user: User?) {
        val mPrefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.appContext)

        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString("user", json)
        prefsEditor.apply()
    }

    fun getUser(): User? {
        val mPrefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.appContext)
        val gson = Gson()
        val json: String? = mPrefs.getString("user", "")
        val obj: User? = gson.fromJson<User>(json, User::class.java)

        return obj
    }

    fun setRememberMe(flag: Boolean) {
        val mPrefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.appContext)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.putBoolean("remember", flag)
        prefsEditor.apply()
    }

    fun isRememberMe(): Boolean {
        val mPrefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.appContext)
        return mPrefs.getBoolean("remember", false)
    }

    fun isUserLoggedIn(): Boolean {
        return getUser() != null
    }

    fun saveStatus(list: ArrayList<TicketStatus>) {
        val mPrefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.appContext)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        prefsEditor.putString("statuses", json)
        prefsEditor.apply()
    }

    fun getStatus(): ArrayList<TicketStatus>? {
        val mPrefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.appContext)
        val gson = Gson()
        val json: String? = mPrefs.getString("statuses", null)
        val type: Type = object : TypeToken<java.util.ArrayList<TicketStatus>>() {}.type
        return gson.fromJson(json, type)

    }

    fun setFcmToken(token: String) {
        val mPrefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.appContext)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()

        prefsEditor.putString("fcmtoken", token)
        prefsEditor.apply()
    }

    fun getFcmToken(): String {
        val mPrefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.appContext)
        return mPrefs.getString("fcmtoken", "")!!
    }


    fun isUserLoggedinInMesibo(email: String): Boolean {
        val mPrefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.appContext)
        return mPrefs.getString(email, "")!! != ""
    }

    fun getUserMesiboToken(email: String): String {
        val mPrefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.appContext)
        return mPrefs.getString(email, "")!!
    }


    fun setUserMesiboToken(email: String, token: String) {
        val mPrefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(App.appContext)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.putString(email, token)
        prefsEditor.apply()
    }
}