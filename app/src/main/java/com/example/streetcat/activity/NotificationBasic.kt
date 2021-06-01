package com.example.streetcat.activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.streetcat.R
import kotlinx.android.synthetic.main.activity_notificationbasic.*

class NotificationBasic : AppCompatActivity(){

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_notificationbasic)

    button2.setOnClickListener{ view ->
        //var builder = NotificationCompat.Builder(this);
        var builder = getNotificationBuilder1("channel1", "첫번째")
        builder.setTicker("Ticker") //ticker가 위에 표시됨
        builder.setSmallIcon(android.R.drawable.ic_menu_search)
        var bitmap = BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher)
        builder.setLargeIcon(bitmap)
        builder.setNumber(100)
        builder.setAutoCancel(true)
        builder.setContentTitle("Street Cat")
        builder.setContentText("고양이 밥 줄 시간이에요 !")

        var notication = builder.build()
        //매니저 -> 알림객체 관리
        var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mng.notify(10, notication)
        }

    button3.setOnClickListener{ view ->
        //var builder = NotificationCompat.Builder(this);
        var builder = getNotificationBuilder1("channel2", "두번째")
        builder.setTicker("Ticker") //ticker가 위에 표시됨
        builder.setSmallIcon(android.R.drawable.ic_menu_search)
        var bitmap = BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher)
        builder.setLargeIcon(bitmap)
        builder.setNumber(100)
        builder.setAutoCancel(true)
        builder.setContentTitle("Content Title2")
        builder.setContentText("Content Text2")

        var notication = builder.build()
        //매니저 -> 알림객체 관리
        var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mng.notify(20, notication) //아이디 값을 다르게 주어야 따로 뜸
    }

    button4.setOnClickListener{ view ->
        //var builder = NotificationCompat.Builder(this);
        var builder = getNotificationBuilder1("channel3", "세번째")
        builder.setTicker("Ticker") //ticker가 위에 표시됨
        builder.setSmallIcon(android.R.drawable.ic_menu_search)
        var bitmap = BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher)
        builder.setLargeIcon(bitmap)
        builder.setNumber(100)
        builder.setAutoCancel(true)
        builder.setContentTitle("Content Title3")
        builder.setContentText("Content Text3")

        var notication = builder.build()
        //매니저 -> 알림객체 관리
        var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mng.notify(30, notication)
    }

    }

    //관련있는 것들끼리 묶어줌
    fun getNotificationBuilder1(id: String, name:String) : NotificationCompat.Builder{
        var builder:NotificationCompat.Builder? = null

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) //버전 8.0 이상시
        {
            //채널 만들어줌
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH) //알림 중요도
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, id) //채널별로 그룹화해서 알림림
        }
       else
        {
            builder = NotificationCompat.Builder(this)
        }
        return builder
    }
}