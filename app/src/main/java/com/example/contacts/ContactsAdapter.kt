package com.example.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.databinding.ItemContactBinding

class ContactsAdapter(
    private val onDeleteListener: ((Long) -> Unit) = {},
    private val onAddListener: ((Long) -> Unit) = {},
) : ListAdapter<Contact, ContactsAdapter.HolderMovie>(DiffCallback()) {

    class HolderMovie(binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvName
        val rvPhones = binding.rvPhones
        val image = binding.ivAvatar
        val btnDelete = binding.cvDelete
        val btnAdd = binding.cvAdd
    }

    class DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMovie {
        val binding = ItemContactBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )
        return HolderMovie(binding)
    }

    override fun onBindViewHolder(holder: HolderMovie, position: Int) {
        val contact = getItem(position)

        holder.apply {
            name.text = contact.name
            image.setImageURI(contact.photoUri)
            btnAdd.setOnClickListener { onAddListener.invoke(contact.id) }
            btnDelete.setOnClickListener { onDeleteListener.invoke(contact.id) }
        }
    }
}