package au.net.tech.app.notification

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log
import androidx.core.app.NotificationCompat
import au.net.tech.app.AppSharedPrefs
import au.net.tech.app.R
import au.net.tech.app.ui.MainActivity


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage


class MessageReceiver : FirebaseMessagingService() {

    val TAG = MessageReceiver.javaClass.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "onMessageReceived: ")
        val title: String = remoteMessage.getData().get("title") ?: ""
        val message: String = remoteMessage.getData().get("body") ?: ""
        showNotifications(title, message)
    }

    override fun onNewToken(p0: String) {
        Log.d(TAG, "onNewToken: ")
        AppSharedPrefs.setFcmToken(p0)
        super.onNewToken(p0)
    }

    private fun showNotifications(title: String, msg: String) {
        val i = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, REQUEST_CODE,
            i, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification: Notification = NotificationCompat.Builder(this)
            .setContentText(msg)
            .setContentTitle(title)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_app_launcher)
            .build()
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        manager!!.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val REQUEST_CODE = 1
        private const val NOTIFICATION_ID = 6578
    }
}