package com.example.assignmentapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentapplication.R
import com.example.assignmentapplication.adapter.UserDataRecyclerviewAdapter
import com.example.assignmentapplication.database.UserDataEntity
import com.example.assignmentapplication.databinding.FragmentHomeBinding
import com.example.assignmentapplication.models.UserData
import com.example.assignmentapplication.utils.Resource
import com.example.assignmentapplication.viewmodel.DataViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    var binding: FragmentHomeBinding? = null
    val viewmodel by viewModels<DataViewmodel>()
    val userDataRecyclerviewAdapter by lazy {
        UserDataRecyclerviewAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Checking if data is already present in the database if yes than fetch otherwise call data from server
        viewmodel.allData.observe(viewLifecycleOwner) {
            if (it.size == 0) {
                viewmodel.getData()
                getDataFromApi()
            } else {
                userDataRecyclerviewAdapter.differ.submitList(it.toUserDataList())
            }
        }
        setUpRecyclerView()

        // Navigate to record audio fragment
        binding?.recordBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_recordAudioFragment)
        }

        // Navigate to user profile fragment
        binding?.profileBtn?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    // fetching the data from API and caching it to room database and adding it to UI
    private fun getDataFromApi() {
        lifecycleScope.launch {
            viewmodel.getResult.collect() {
                when (it) {
                    is Resource.Success -> {
                        Log.d(TAG, it.data.toString())
                        userDataRecyclerviewAdapter.differ.submitList(it.data)
                        val list = it.data
                        if (list != null) {
                            for (data in list) {
                                val userDataEntity = UserDataEntity(
                                    data.id,
                                    data.userId,
                                    data.title,
                                    data.body
                                )
                                viewmodel.insert(userDataEntity)
                            }
                        }
                    }
                    is Resource.Error -> {
                        Log.d(TAG, it.message.toString())
                        Toast.makeText(requireContext(), "Unable to fetch Data", Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Loading -> {
                        Log.d(TAG, "Loading")
                    }
                    else -> Unit
                }
            }
        }
    }

    /** Setting up the recycler view */
    private fun setUpRecyclerView() {
        binding?.recyclerView?.apply {
            adapter = userDataRecyclerviewAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun List<UserDataEntity>.toUserDataList(): List<UserData?> {
        return this.map { entity ->
            UserData(
                id = entity.id,
                userId = entity.userId,
                title = entity.title,
                body = entity.body
            )
        }
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}