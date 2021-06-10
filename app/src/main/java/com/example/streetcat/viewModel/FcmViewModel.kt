package com.example.streetcat.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.streetcat.data.NotificationData
import com.example.streetcat.data.PushNotification
import com.example.streetcat.data.RetrofitInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FcmViewModel() : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val TAG = "FcmActivity"
    lateinit var token: String

    //유저의 id를 받아 token으로 변환 후, fcm을 이용해 타입에 맞는 알람을 보냅니다.
    fun sendAlarm(id: String, type: String) {
        val tokenRef = database.getReference("users").child(id).child("token")
        tokenRef.get().addOnSuccessListener {
            token = it.value.toString()
            if(type == "comment") {
                val PushNotification = PushNotification(
                    NotificationData("StreetCat", "내가 쓴 게시글에 댓글이 달렸어요!"),
                    token
                )
                sendNotification(PushNotification)
            }
            else{
                val PushNotification = PushNotification(
                    NotificationData("StreetCat", "고양이가 아파요"),
                    token
                )
                sendNotification(PushNotification)
            }
        }
    }

    fun getUserKeyForAlarm(catId: String, sickName: String, catName: String) {
        database.getReference("cats").child(catId).child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(data: DataSnapshot) {
                    for (user in data.children) {
                        sendAlarm(user.value.toString(), "sick")
                        setNotice(catId, user.value.toString(), sickName, catName)
                    }
                }
            })
    }

    fun setNotice(key: String, authorId: String, context: String, username: String){ // 댓글이 달리면 알림 띄움
        val noticeKey = database.getReference("users").child(authorId).child("notice").push().key.toString()
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("type").setValue("sick")
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("postkey").setValue(key)
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("context").setValue(context)
        database.getReference("users").child(authorId).child("notice").child(noticeKey).child("username").setValue(username)
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
}