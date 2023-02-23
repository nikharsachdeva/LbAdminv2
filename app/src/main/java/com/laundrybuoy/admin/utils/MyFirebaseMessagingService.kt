package com.laundrybuoy.admin.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.laundrybuoy.admin.MainActivity
import com.laundrybuoy.admin.R
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


const val CHANNEL_ID = "CHANNEL_ID"

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onNewToken(p0: String) {
        SharedPreferenceManager.setFCMToken(p0)
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("abcdef-->", "onMessageReceived:default ")
        val x  = remoteMessage
        Log.d("abcdef-->", "onMessageReceived: X "+Gson().toJson(x))

        if (remoteMessage.data.containsKey("body") && remoteMessage.data.containsKey("title")) {
            val titleReceived = remoteMessage.data["title"]
            val bodyReceived = remoteMessage.data["body"]
            val imageReceived = remoteMessage.data["image"]
            val destinationReceived = remoteMessage.data["destination"]
            val idReceived = remoteMessage.data["_id"]

            Log.d("abcdef-->", "onMessageReceived:title +${titleReceived} ")
            Log.d("abcdef-->", "onMessageReceived:body +${bodyReceived}")
            Log.d("abcdef-->", "onMessageReceived:image +${imageReceived}")
            Log.d("abcdef-->", "onMessageReceived:destination +${destinationReceived}")
            Log.d("abcdef-->", "onMessageReceived:id +${idReceived}")


            if (imageReceived?.isNotEmpty() == true) {
                fetchImageForNotification(
                    title = titleReceived,
                    body = bodyReceived,
                    image = imageReceived,
                    destination = destinationReceived,
                    id = idReceived
                )
            } else {
                val nullBitmap: Bitmap? = null
                showNotification(
                    title = titleReceived,
                    body = bodyReceived,
                    image = nullBitmap,
                    destination = destinationReceived,
                    id = idReceived
                )
            }

        }else{
            Log.d("xxyyxx4", "onMessageReceived: "+ Gson().toJson(remoteMessage.data))
        }

    }

    @SuppressLint("StaticFieldLeak")
    private fun fetchImageForNotification(
        title: String?,
        body: String?,
        image: String?,
        destination: String?,
        id: String?,
    ) {

        object : AsyncTask<String?, Void?, Bitmap?>() {
            override fun doInBackground(vararg p0: String?): Bitmap? {
                val inputStream: InputStream
                try {
                    val url = URL(p0[0])
                    val connection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    inputStream = connection.inputStream
                    return BitmapFactory.decodeStream(inputStream)
                } catch (ignored: Exception) {
                }
                return null
            }

            override fun onPostExecute(bitmap: Bitmap?) {
                showNotification(
                    title = title,
                    body = body,
                    image = bitmap,
                    destination = destination,
                    id = id
                )
            }
        }.execute(image)

    }

    private fun showNotification(
        title: String?,
        body: String?,
        image: Bitmap?,
        destination: String?,
        id: String?,
    ) {

        createNotificationChannel()

        val date = Date()
        val notificationId = SimpleDateFormat("ddHHmmss", Locale.US).format(date).toInt()

        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent =
            PendingIntent.getActivity(this, 1, mainIntent, PendingIntent.FLAG_IMMUTABLE)

        var notificationBuilder = NotificationCompat.Builder(this, "$CHANNEL_ID")
        notificationBuilder.setSmallIcon(R.drawable.ic_music)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setContentText(body)

        if (image != null) {
            notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(image))
        }

        notificationBuilder.setVibrate(longArrayOf(1000, 1000, 1000, 1000))
        notificationBuilder.setOnlyAlertOnce(true)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.priority = NotificationCompat.PRIORITY_DEFAULT
        notificationBuilder.setContentIntent(pendingIntent)

//        notificationBuilder = notificationBuilder.setContent(getRemoteView(title, body))

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(notificationId, notificationBuilder.build())


    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "MyNotification"
            val description = "My notification channel description"

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationChannel.description = description
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    fun getToken(context: Context): String? {
        return SharedPreferenceManager.getFCMToken()
    }
}