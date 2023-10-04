package com.example.assignmentapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignmentapplication.models.UserInfoData
import com.example.assignmentapplication.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewmodel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<UserInfoData>>(Resource.Unspecified())
    val register: Flow<Resource<UserInfoData>> = _register

    fun registerUser(userInfo: UserInfoData, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _register.emit(Resource.Loading())
        }

        viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.createUserWithEmailAndPassword(userInfo.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        viewModelScope.launch {
                            _register.emit(Resource.Success(userInfo))
                        }
                    }
                }
                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                }
        }
    }

}