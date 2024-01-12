package com.example.contacts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.databinding.ItemPhoneBinding

class PhoneAdapter : ListAdapter<String, PhoneAdapter.HolderMovie>(DiffCallback()) {

    class HolderMovie(binding: ItemPhoneBinding) : RecyclerView.ViewHolder(binding.root) {
        val phone = binding.tvPhone
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMovie {
        val binding = ItemPhoneBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
        return HolderMovie(binding)
    }

    override fun onBindViewHolder(holder: HolderMovie, position: Int) {
        holder.phone.text = getItem(position)
    }
}