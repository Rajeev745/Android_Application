package com.example.assignmentapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.assignmentapplication.models.UserInfoData

@Dao
interface UserDataTableDao {

    @Insert
    suspend fun insert(userData: UserDataEntity)

    @Query("SELECT * FROM user_data_table")
    fun getUserDataList(): LiveData<List<UserDataEntity>>

}