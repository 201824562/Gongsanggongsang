package com.example.gongsanggongsang.Second

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

class CommunalViewModel : ViewModel() {
    val database = FirebaseFirestore.getInstance()

    val usingLiveData = MutableLiveData<List<UsingDataClass>>()
    val LiveData = MutableLiveData<List<EquipmentDataClass>>()

    private val user_using_data = arrayListOf<UsingDataClass>()
    private val data = arrayListOf<EquipmentDataClass>()

    fun get_equipmentdata(){

        lateinit var document_name : String
        lateinit var using : String

        database.collection("COMMUNAL_Equipment")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                data.clear()
                for (document in value!!) {
                    document_name = document.id
                    if(document.contains("name")){
                        using = "using"
                    }else{
                        using = "no_using"
                    }
                    data.add(EquipmentDataClass(document_name, using, 0))
                }
                LiveData.value = data
//                binding.equipmentRecyclerView.adapter?.notifyDataSetChanged()
            }
    }

    fun get_UsingEquipmentdata(){

        lateinit var document_name : String
        lateinit var name : String
        lateinit var start_time : String
        lateinit var end_time : String

        database.collection("COMMUNAL_Equipment")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                user_using_data.clear()
                for (document in value!!) {
                    document_name = document.id
                    if(document.getString("name") != null) {
                        name = document.getString("name") ?: " "
                        start_time = document.get("start") as String
                        end_time = document.get("end") as String

                        if (name == "kijung" ) { //userdata
                            user_using_data.add(UsingDataClass(document_name, name, start_time, end_time))
                        }
                    }
                }
                usingLiveData.value = user_using_data
//                binding.equipmentUsingRecyclerView.adapter?.notifyDataSetChanged()
            }
    }

    fun add_use(EquipmentDataClass : EquipmentDataClass) {
        var map1 = mutableMapOf<String, Any>()
        var map2 = mutableMapOf<String, Any>()
        var map3 = mutableMapOf<String, Any>()

        map1["start"] = LocalDateTime.now().toString()
        map2["end"] = LocalDateTime.now().plusMinutes(70).toString()
        map3["name"] = "kijung" // name을 맨마지막으로 지우지 않으면 nullpointerexception 발생

        database.collection("COMMUNAL_Equipment").document(EquipmentDataClass.document_name).update(map1)
        database.collection("COMMUNAL_Equipment").document(EquipmentDataClass.document_name).update(map2)
        database.collection("COMMUNAL_Equipment").document(EquipmentDataClass.document_name).update(map3)

        LiveData.value = data
    }

    fun end_use(UsingDataClass : UsingDataClass) {
        var map1 = mutableMapOf<String, Any>()
        var map2 = mutableMapOf<String, Any>()
        var map3 = mutableMapOf<String, Any>()

        map1["name"] = FieldValue.delete()
        map2["start"] = FieldValue.delete()
        map3["end"] = FieldValue.delete()

        database.collection("COMMUNAL_Equipment").document(UsingDataClass.document_name).update(map1)
        database.collection("COMMUNAL_Equipment").document(UsingDataClass.document_name).update(map2)
        database.collection("COMMUNAL_Equipment").document(UsingDataClass.document_name).update(map3)

        usingLiveData.value = user_using_data
    }

}