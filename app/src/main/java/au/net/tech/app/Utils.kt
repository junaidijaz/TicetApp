package au.net.tech.app

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun isStaff() = (AppSharedPrefs.getUser()?.type ?: "").equals("staff", true)

    fun convertTimeStamp(timestamp: Long) : String{
        val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestamp
        return   DateFormat.format("hh:mm a", cal).toString()
    }


    fun dateFunction(timestamp: Long, isToday: Boolean): String {
        var sDate = ""
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val c = Calendar.getInstance()
        val netDate: Date?
        try {
            netDate = Date(timestamp)
            sdf.format(netDate)
            sDate = sdf.format(netDate)
            val currentDateTimeString: String = sdf.format(c.time)
            c.add(Calendar.DATE, -1)
            val yesterdayDateTimeString: String = sdf.format(c.time)
            if (currentDateTimeString == sDate && isToday) {
                sDate = "Today"
            } else if (yesterdayDateTimeString == sDate && isToday) {
                sDate = "Yesterday"
            }
        } catch (e: Exception) {
            System.err.println("There's an error in the Date!")
        }
        return sDate + " ${convertTimeStamp(timestamp)}"
    }

}