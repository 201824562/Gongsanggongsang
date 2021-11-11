package com.parasol.userapp.service

import android.content.ContentValues
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Single
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class Constants {
    companion object{
        const val BASE_URL = "https://fcm.googleapis.com/"
        const val SERVER_KEY = "AAAAK8zuF9U:APA91bF_XAiGyEbB2bKw-q2m_68G7Ic3TDwEQDZqiyddDN6mtggukXJUyPCAgELJabdV4aPFUCAYZKUIJnsVPkVLcPPcUd99tl8q3VldNRPywdEDzB1zCJbXx0CgJNV13eU6Rxy0T7ZV"
        const val CONTENT_TYPE = "application/json"
    }
}

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build() }
        val api by lazy {
            retrofit.create(NotificationAPI::class.java)
        }
    }
}

data class NotificationData(val title: String, val message: String)

data class PushNotification(val data: NotificationData, val to: String)

interface NotificationAPI {
    //서버 키와 보낼 형식을 헤더에 넣는다. (json)
    @Headers("Authorization: key=${Constants.SERVER_KEY}", "Content-Type:${Constants.CONTENT_TYPE}")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}

fun getFCMToken() : Single<String> {
    return Single.create { emitter ->
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful && !emitter.isDisposed) {
                task.result?.let { emitter.onSuccess(it) }
            } else {
                emitter.onError(Throwable("fail"))
            }
        }
    }
}

fun sendFireStoreNotification(title : String, msg : String, token : String) = CoroutineScope(Dispatchers.IO).launch {
    try {
        val notification = PushNotification(NotificationData(title, msg), token)
        val response = RetrofitInstance.api.postNotification(notification)
        if(response.isSuccessful) {
            //Log.d(TAG, "Response: ${Gson().toJson(response)}")
        } else {
            Log.e(ContentValues.TAG, response.errorBody().toString())
        }
    } catch(e: Exception) {
        Log.e(ContentValues.TAG, e.toString())
    }
}
