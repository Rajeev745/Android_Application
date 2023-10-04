package com.example.assignmentapplication.fragments

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
import com.example.assignmentapplication.databinding.FragmentSigninBinding
import com.example.assignmentapplication.utils.Resource
import com.example.assignmentapplication.viewmodel.SigninViewmodel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SigninFragment : Fragment() {

    var binding: FragmentSigninBinding? = null
    private val signinViewmodel by viewModels<SigninViewmodel>()

    @Inject lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Getting the user if the user is signed in than redirect to home fragment
        val user = firebaseAuth.currentUser
        if(user != null) {
            findNavController().navigate(R.id.action_signinFragment_to_homeFragment)
        }

        binding?.buttonSignIn?.setOnClickListener {
            startSignIn()
        }

        // Observing the signin result
        lifecycleScope.launch {
            signinViewmodel.login.collect() {
                when(it) {
                    is Resource.Success -> {
                        findNavController().navigate(R.id.action_signinFragment_to_homeFragment)
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

        binding?.signUpText?.setOnClickListener {
            findNavController().navigate(R.id.action_signinFragment_to_signUpFragment)
        }
    }

    /**
     * Intiating the sign in process.
     */
    private fun startSignIn() {
        val email = binding?.editTextEmail?.text.toString().trim()
        val password = binding?.editTextPassword?.text.toString().trim()

        signinViewmodel.signingWithEmailAndPassword(email, password)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}