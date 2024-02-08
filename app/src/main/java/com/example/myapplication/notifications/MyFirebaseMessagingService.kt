package com.example.myapplication.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pluggableai.smartpush.EntryPoint

class MyFirebaseMessagingService: FirebaseMessagingService() {

    companion object {
        private const val TAG: String = "MyFirebaseMessagingService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Notification received from: ${remoteMessage.from}")
        // Check if message contains a data payload.
        if (remoteMessage.data.containsKey("pluggable_notification_seq_num")) {
            handleNotification(remoteMessage)
        }
    }

    private fun handleNotification(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Message data payload: ${remoteMessage.data}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("default", "default", NotificationManager.IMPORTANCE_HIGH)
            mChannel.description = "default"
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        // Notification meta-data.
        // val notificationChannelId = getString(R.string.notification_channel_id)
        // val notificationChannelName = getString(R.string.notification_channel_name)
        val notificationData = remoteMessage.data
        // Execute the Smartpush SDK.
        // It will automatically deliver the notification
        // when the user is most likely to engage with it.
        val pushUUID = EntryPoint.execute(
            this,
            "default",
            "default",
            notificationData
        )
        Log.d(TAG, "pushUUID: $pushUUID")
        // Save the pushUUID and userID to the database
        // ...
    }

}