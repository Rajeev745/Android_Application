package com.example.assignmentapplication.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.assignmentapplication.R
import com.example.assignmentapplication.databinding.FragmentSignUpBinding
import com.example.assignmentapplication.models.UserInfoData
import com.example.assignmentapplication.utils.Resource
import com.example.assignmentapplication.viewmodel.SignupViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var binding: FragmentSignUpBinding? = null
    private val signupViewmodel by viewModels<SignupViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initiating the sign up logic
        binding?.btnSignup?.setOnClickListener {
            val userObj = getUserInfo()
            val password = binding?.etPassword?.text.toString().trim()
            if (password.length < 6) {
                Toast.makeText(
                    requireContext(),
                    "Password length must be atleast 6 character",
                    Toast.LENGTH_LONG
                ).show()
            } else if (!userObj.email.isNotEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Enter valid email",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                signupViewmodel.registerUser(userObj, password)
            }
        }

        // Observing the result of sign up process
        lifecycleScope.launch {
            signupViewmodel.register.collect() {
                when (it) {
                    is Resource.Success -> {
                        saveUserInfo(it.data!!)
                        findNavController().navigate(R.id.action_signUpFragment_to_signinFragment)
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Something went wrong",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is Resource.Loading -> {}
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    /** Saving the user data locally using shared preference */
    private fun saveUserInfo(userInfoData: UserInfoData) {
        val sharedPreferences =
            requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", userInfoData.name)
        editor.putString("age", userInfoData.age)
        editor.putString("email", userInfoData.email)
        editor.putString("mobile", userInfoData.mobile)
        editor.apply()
    }

    private fun getUserInfo(): UserInfoData {
        val name = binding?.etName?.text.toString().trim()
        val email = binding?.etEmail?.text.toString().trim()
        val age = binding?.etAge?.text.toString().trim()
        val mobile = binding?.etMobile?.text.toString().trim()

        return UserInfoData(name = name, mobile = mobile, age = age, email = email)
    }

}