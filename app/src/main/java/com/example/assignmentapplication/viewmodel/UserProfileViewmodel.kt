package com.example.assignmentapplication.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewmodel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    val context: Context
) :
    ViewModel() {

    private val _nameLiveData = MutableLiveData<String>()
    val nameLiveData: LiveData<String> = _nameLiveData

    private val _ageLiveData = MutableLiveData<String>()
    val ageLiveData: LiveData<String> = _ageLiveData

    private val _mobileLiveData = MutableLiveData<String>()
    val mobileLiveData: LiveData<String> = _mobileLiveData

    init {
        updateLiveData()
    }

    fun updateAge(age: String) {
        if (age.isNotEmpty()) {
            val editor = sharedPreferences.edit()
            editor.putString("age", age)
            editor.apply()
            _ageLiveData.value = age
        } else {
            showErrorMessage("Age")
        }
    }

    fun updateMobile(mobile: String) {
        if (mobile.length == 10) {
            val editor = sharedPreferences.edit()
            editor.putString("mobile", mobile)
            editor.apply()
            _mobileLiveData.value = mobile
        } else {
            showErrorMessage("Mobile")
        }
    }

    private fun showErrorMessage(s: String) {
        Toast.makeText(context, "Invalid ${s}", Toast.LENGTH_LONG).show()
    }

    fun updateName(name: String) {
        if (name.isNotEmpty()) {
            val editor = sharedPreferences.edit()
            editor.putString("name", name)
            editor.apply()
            _nameLiveData.value = name
        } else {
            showErrorMessage("name")
        }
    }

    /** Observing the data for the profile fragment. */
    private fun updateLiveData() {
        _nameLiveData.value = sharedPreferences.getString("name", "Name not available")
        _ageLiveData.value = sharedPreferences.getString("age", "Age not available")
        _mobileLiveData.value = sharedPreferences.getString("mobile", "Mobile not available")
    }
}