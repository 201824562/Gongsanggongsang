package com.example.userapp.data.paging

import android.content.ContentValues
import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.userapp.data.dto.UserModel
import com.example.userapp.data.model.Agency
import com.example.userapp.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


@ExperimentalStdlibApi
class AgencyDataSource(private val firestore: FirebaseFirestore, private val keyWord: String) : PageKeyedDataSource<Int, Agency>() {

    companion object {
        const val PAGE_SIZE = 10
        const val FIRST_PAGE = 1
        const val RESULT_TYPE = "json"
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Agency>) {
        /* Auto Mode 일 때와 Search Mode 일 때 부르는 API Service 다르게 */
        val call = addressService.callPagedAutoCompleteJuSoList(
            word = keyWord,
            countPerPage = PAGE_SIZE,
            resultType = RESULT_TYPE,
            currentPage = FIRST_PAGE)
        try{
            val response = call.execute()
            if (response.isSuccessful ){
                response.body()?.results?.let { addressResult->
                    val addressList = buildList {
                        addressResult.juso.forEach{
                            this.add(Agency(token = userToken, address = it.roadAddr, addressJiBun = it.jibunAddr, addressDetail = ""))
                        }
                    }
                    callback.onResult(addressList, null, FIRST_PAGE + (if (agencyType == String.AUTO) 0 else 1) )
                }

            }
        }catch (e:Exception){

        }
    }

    private fun getAgencyInfo(callback: LoadInitialCallback<Int, Agency>) : Observable<Agency>{
        //TODO : Searching하는 기능 찾기.
        firestore.collection("SIGN_UP_AGENCY").whereArrayContainsAny("location", listOf(keyWord)).whereArrayContainsAny("name", listOf(keyWord))
            .get()
            .addOnSuccessListener { documents ->
                val agencyList = buildList<Agency> {
                    for (document in documents){
                        Log.e("checking","${document.data["name"]}")
                        this.add(Agency(name = document["name"].toString(), document["location"].toString()))
                    }
                }
                callback.onResult(agencyList, params.key + 1 )
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Agency>) {
        val call = addressService.callPagedResultJuSoList(
            word = keyWord,
            countPerPage = PAGE_SIZE,
            resultType = RESULT_TYPE,
            currentPage = params.key)
        try{
            val response = call.execute()
            if (response.isSuccessful ){
                response.body()?.results?.let { addressResult->
                    val addressList = buildList {
                        addressResult.juso.forEach{
                            this.add(Agency(token = userToken, address = it.roadAddr, addressJiBun = it.jibunAddr, addressDetail = ""))
                        }
                    }
                    callback.onResult(addressList, params.key + 1 )
                }

            }
        }catch (e:Exception){

        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Agency>) {}
}


class AgencyDataSourceFactory (private val firestore: FirebaseFirestore, private val keyword: String) : DataSource.Factory<Int, Agency>() {
    @ExperimentalStdlibApi
    override fun create(): DataSource<Int, Agency> {
        return AgencyDataSource(firestore = firestore, keyWord = keyword)
    }
}
