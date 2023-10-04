package com.example.assignmentapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentapplication.api.RetrofitApi
import com.example.assignmentapplication.database.UserDataEntity
import com.example.assignmentapplication.database.UserDataRepository
import com.example.assignmentapplication.models.UserData
import com.example.assignmentapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class DataViewmodel @Inject constructor(
    val retrofit: Retrofit,
    val userDataRepository: UserDataRepository
) : ViewModel() {

    private val _getResult = MutableStateFlow<Resource<List<UserData>>>(Resource.Unspecified())
    val getResult = _getResult.asStateFlow()

    val allData: LiveData<List<UserDataEntity>> = userDataRepository.allData

    /** Inserting the data into the database for the first time. */
    suspend fun insert(userData: UserDataEntity) {
        userDataRepository.insert(userData)
    }

    /** fetching the data from the api in case if not available in room.
     * */
    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            _getResult.emit(Resource.Loading())
            try {
                // Fetching the data from the API
                val apiService = retrofit.create(RetrofitApi::class.java)
                val getResponse = apiService.getData()

                getResponse.enqueue(object : Callback<List<UserData>> {
                    override fun onResponse(
                        call: Call<List<UserData>>, response: Response<List<UserData>>
                    ) {
                        if (response.isSuccessful) {
                            viewModelScope.launch {
                                _getResult.emit(Resource.Success(response.body()))
                            }
                        } else {
                            viewModelScope.launch {
                                _getResult.emit(Resource.Error("Error: ${response.code()}"))
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                        t.printStackTrace()
                        viewModelScope.launch {
                            _getResult.emit(Resource.Error(t.message))
                        }
                    }
                })

            } catch (e: Exception) {
                e.printStackTrace()
                _getResult.emit(Resource.Error(e.message))
            }
        }
    }

}