package com.example.streetcat.services
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFcmService : FirebaseMessagingService()
{
    override fun onNewToken(token: String)
    {
        Log.d("MyFcmService", "New token :: $token")
        sendTokenToServer(token)
    }
    private fun sendTokenToServer(token: String)
    {
// TOKEN 값 서버에 저장
}

    override fun onMessageReceived(remoteMessage: RemoteMessage)
    {
        //전달받은 리모트 메시지를 처리
          Log.d("MyFcmService", "Notification Title :: ${remoteMessage.notification?.title}")
          Log.d("MyFcmService", "Notification Body :: ${remoteMessage.notification?.body}")
          Log.d("MyFcmService", "Notification Data :: ${remoteMessage.data}")
    }
/*
    private fun showNotification(notification: RemoteMessage.Notification)
    {
        val intent = Intent(this, NoticeActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = getString(R.string.my_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setContentIntent(pIntent)

        getSystemService(NotificationManager::class.java).run
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                val channel = NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
                createNotificationChannel(channel)
            }
            notify(Date().time.toInt(), notificationBuilder.build())
        }
    }
*/
}
