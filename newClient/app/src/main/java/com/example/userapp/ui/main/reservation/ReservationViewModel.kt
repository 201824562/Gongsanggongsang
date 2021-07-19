package com.example.userapp.ui.main.reservation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.model.ReservationEquipment
import com.example.userapp.data.model.ReservationFacility
import com.example.userapp.data.model.ReservationUseEquipment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import java.time.LocalDateTime

class ReservationViewModel : BaseViewModel() {
    val database = FirebaseFirestore.getInstance()

    val UseEquipmentLiveData = MutableLiveData<List<ReservationUseEquipment>>()
    val EquipmentLiveData = MutableLiveData<List<ReservationEquipment>>()
    val FacilityLiveData = MutableLiveData<List<ReservationFacility>>()

    private val UseEquipmentData = arrayListOf<ReservationUseEquipment>()
    private val EquipmentData = arrayListOf<ReservationEquipment>()
    private val FacilityData = arrayListOf<ReservationFacility>()

    //뷰모델스코프를 이용해서 제어
    var viewmodelscope = viewModelScope

    fun cancelViewModelScope(){
        viewmodelscope.cancel()
    }

    //파이어베이스에서 데이터 가져오는 함수 getEquipmentData, getUseEquipmentData, getFacilityData 통일해야함
    fun getEquipmentData() {
        lateinit var document_name: String
        lateinit var using: String
        lateinit var end_time: String
        lateinit var category: String

        database.collection("COMMUNAL_Equipment")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                EquipmentData.clear()
                for (document in value!!) {
                    document_name = document.id
                    category = document.get("category") as String
                    if (document.contains("name")) {
                        using = "using"
                        end_time = document.get("end") as String
                    } else {
                        using = "no_using"
                        end_time = "no_endtime"
                    }

                    EquipmentData.add(
                        ReservationEquipment(
                            document_name,
                            using,
                            end_time,
                            category
                        )
                    )
                }
                EquipmentLiveData.value = EquipmentData
            }
    }

    fun getUseEquipmentData() {
        lateinit var document_name: String
        lateinit var name: String
        lateinit var start_time: String
        lateinit var end_time: String
        lateinit var category: String
        var coroutine : CoroutineScope = CoroutineScope(Dispatchers.Main)

        database.collection("COMMUNAL_Equipment")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                UseEquipmentData.clear()
                for (document in value!!) {
                    document_name = document.id
                    if (document.getString("name") != null) {
                        name = document.getString("name") ?: " "
                        start_time = document.get("start") as String
                        end_time = document.get("end") as String
                        category = document.get("category") as String

                        if (name == "kijung") { //userdata
                            UseEquipmentData.add(
                                ReservationUseEquipment(
                                    document_name,
                                    name,
                                    start_time,
                                    end_time,
                                    0,
                                    category,
                                    coroutine
                                )
                            )
                        }
                    }
                }
                UseEquipmentLiveData.value = UseEquipmentData
            }
    }

    fun getFacilityData() {
        lateinit var document_name: String
        lateinit var category: String
        var max_time: Long  //long타입은 lateinit이 안돼나?
        var time_interval: Long

        database.collection("COMMUNAL_Facility")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                FacilityData.clear()
                for (document in value!!) {
                    document_name = document.id
                    category = document.get("category") as String
                    max_time = document.get("max_time") as Long
                    time_interval = document.get("time_interval") as Long

                    FacilityData.add(
                        ReservationFacility(
                            document_name,
                            category,
                            max_time,
                            time_interval
                        )
                    )
                }
                FacilityLiveData.value = FacilityData
            }
    }

    fun add_use(ReservationEquipment: ReservationEquipment, usingtime: Int) {
        var map1 = mutableMapOf<String, Any>()
        var map2 = mutableMapOf<String, Any>()
        var map3 = mutableMapOf<String, Any>()

        map1["start"] = LocalDateTime.now().toString()
        map2["end"] = LocalDateTime.now().plusMinutes(usingtime.toLong()).toString()
        map3["name"] = "kijung" // name을 맨마지막으로 지우지 않으면 nullpointerexception 발생

        database.collection("COMMUNAL_Equipment").document(ReservationEquipment.document_name)
            .update(map1)
        database.collection("COMMUNAL_Equipment").document(ReservationEquipment.document_name)
            .update(map2)
        database.collection("COMMUNAL_Equipment").document(ReservationEquipment.document_name)
            .update(map3)

        EquipmentLiveData.value = EquipmentData
    }

    fun end_use(ReservationUseEquipment: ReservationUseEquipment) {
        ReservationUseEquipment.coroutine.cancel()
        var map1 = mutableMapOf<String, Any>()
        var map2 = mutableMapOf<String, Any>()
        var map3 = mutableMapOf<String, Any>()

        map1["name"] = FieldValue.delete()
        map2["start"] = FieldValue.delete()
        map3["end"] = FieldValue.delete()

        database.collection("COMMUNAL_Equipment").document(ReservationUseEquipment.document_name)
            .update(map1)
        database.collection("COMMUNAL_Equipment").document(ReservationUseEquipment.document_name)
            .update(map2)
        database.collection("COMMUNAL_Equipment").document(ReservationUseEquipment.document_name)
            .update(map3)


        UseEquipmentLiveData.value = UseEquipmentData
    }
}