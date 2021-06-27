package com.example.userapp.ui.mainhome.reservation

import androidx.lifecycle.MutableLiveData
import com.example.userapp.base.BaseViewModel
import com.example.userapp.data.model.ReservationEquipment
import com.example.userapp.data.model.ReservationUseEquipment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

class ReservationViewModel : BaseViewModel() {
    val database = FirebaseFirestore.getInstance()

    val UseEquipmentLiveData = MutableLiveData<List<ReservationUseEquipment>>()
    val EquipmentLiveData = MutableLiveData<List<ReservationEquipment>>()

    private val UseEquipmentData = arrayListOf<ReservationUseEquipment>()
    private val EquipmentData = arrayListOf<ReservationEquipment>()

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
                    EquipmentData.add(ReservationEquipment(document_name, using, end_time, category))
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
                                    category
                                )
                            )
                        }
                    }
                }
                UseEquipmentLiveData.value = UseEquipmentData
            }
    }

    fun add_use(ReservationEquipment: ReservationEquipment) {
        var map1 = mutableMapOf<String, Any>()
        var map2 = mutableMapOf<String, Any>()
        var map3 = mutableMapOf<String, Any>()

        map1["start"] = LocalDateTime.now().toString()
        map2["end"] = LocalDateTime.now().plusMinutes(70).toString()
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