package com.example.assignmentapplication.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.assignmentapplication.databinding.FragmentProfileBinding
import com.example.assignmentapplication.viewmodel.UserProfileViewmodel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    var binding: FragmentProfileBinding? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val userViewModel by viewModels<UserProfileViewmodel>()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializing the shared preference obj for showing the data
        sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val email = firebaseAuth.currentUser?.email
        binding?.emailText?.text = email

        // Observing the user data
        userViewModel.nameLiveData.observe(viewLifecycleOwner) { name ->
            binding?.nameText?.text = name
        }

        userViewModel.ageLiveData.observe(viewLifecycleOwner) { age ->
            binding?.ageText?.text = age
        }

        userViewModel.mobileLiveData.observe(viewLifecycleOwner) { mobile ->
            binding?.mobileText?.text = mobile
        }

        // Updating the user data on click of button
        binding?.updateNameBtn?.setOnClickListener {
            val name = binding?.nameEdtxt?.text.toString().trim()
            userViewModel.updateName(name)
        }

        binding?.updateAgeBtn?.setOnClickListener {
            val age = binding?.ageEdtxt?.text.toString().trim()
            userViewModel.updateAge(age)
        }

        binding?.updateMobileBtn?.setOnClickListener {
            val mobile = binding?.mobileEdtxt?.text.toString().trim()
            userViewModel.updateMobile(mobile)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}