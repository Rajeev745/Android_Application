package com.example.assignmentapplication.database

import androidx.lifecycle.LiveData
import javax.inject.Inject

class UserDataRepository @Inject constructor(val userDataTableDao: UserDataTableDao) {

    val allData: LiveData<List<UserDataEntity>> = userDataTableDao.getUserDataList()

    suspend fun insert(userData: UserDataEntity) {
        userDataTableDao.insert(userData)
    }

}