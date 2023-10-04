package com.example.assignmentapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentapplication.databinding.UserDataRecyclerViewItemBinding
import com.example.assignmentapplication.models.UserData

// Adapter class for showing the user data from the api
class UserDataRecyclerviewAdapter : RecyclerView.Adapter<UserDataRecyclerviewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: UserDataRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userData: UserData) {
            binding.title.text = userData.title
            binding.content.text = userData.body
        }
    }

    val diffCallback = object : ItemCallback<UserData>() {
        override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            UserDataRecyclerViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = differ.currentList[position]

        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}