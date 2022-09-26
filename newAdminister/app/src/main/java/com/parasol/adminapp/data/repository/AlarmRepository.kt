package com.parasol.adminapp.data.repository

import android.content.ContentValues
import android.util.Log
import com.parasol.adminapp.data.AppDatabase
import com.parasol.adminapp.data.model.AlarmItem
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable

class AlarmRepository (appDatabase: AppDatabase) {
    companion object {
        private var sInstance: AlarmRepository? = null
        fun getInstance(database: AppDatabase): AlarmRepository {
            return sInstance
                ?: synchronized(this) {
                    val instance = AlarmRepository(database)
                    sInstance = instance
                    instance
                }
        }
        private val FIRESTORE_RESERVATION = "RESERVATION"
        private val FIRESTORE_RESERVATION_LOG = "LOG"
        private val FIRESTORE_ALARM ="ALARM"
    }


    private val fireStore = FirebaseFirestore.getInstance()


    //TODO : 승인 관련 로직으로 바꾸기.
    fun makeReservationLogUsing (agency: String, documentId: String){
        fireStore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_LOG)
            .document(documentId).update("reservationState", "사용중")
            .addOnSuccessListener {  Log.e("checking", "Succeed updating RESERVATION_LOG OF USING") }
            .addOnFailureListener { Log.e("checking", "Error updating RESERVATION_LOG OF USING") }
    }
    fun makeReservationLogCancel(agency: String, documentId: String){
        fireStore.collection(agency).document(FIRESTORE_RESERVATION).collection(FIRESTORE_RESERVATION_LOG)
            .document(documentId).update("reservationState", "예약취소")
            .addOnSuccessListener {  Log.e("checking", "Succeed updating RESERVATION_LOG OF CANCEL") }
            .addOnFailureListener { Log.e("checking", "Error updating RESERVATION_LOG OF CANCEL") }
    }


    fun registerAlarmData(agency: String, toToken : String, documentId: String, alarmData : AlarmItem) : Completable {
        return Completable.create { emitter ->
            fireStore.collection(agency).document(FIRESTORE_ALARM).collection(toToken)
                .document(documentId).set(alarmData)
                .addOnSuccessListener {
                    Log.e("checking", "Succeed registering ALARM_DATA")
                    emitter.onComplete() }
                .addOnFailureListener {
                    Log.e("checking", "Error registering ALARM_DATA")
                    emitter.onError(Throwable("Error registering ALARM_DATA")) }
        }
    }

    fun makeAlarmDataClickUnable(agency: String, myToken : String, documentId: String) {
        fireStore.collection(agency).document(FIRESTORE_ALARM).collection(myToken).document(documentId)
            .update("clicked", true)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "update Alarm Clicked Succeed!!") }
            .addOnFailureListener { exception -> Log.w(ContentValues.TAG, "Error update Alarm Clicked: ", exception) }
    }


}