package com.example.contacts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.R
import com.example.contacts.databinding.ItemContactBinding
import com.example.contacts.model.Contact
import com.example.contacts.utils.hide
import com.example.contacts.utils.show

class ContactsAdapter(
    private val contactList: List<Contact>,
    private val onDeleteListener: ((Contact) -> Unit) = {},
    private val onAddListener: ((Contact) -> Unit) = {},
) : ListAdapter<Contact, ContactsAdapter.HolderMovie>(DiffCallback()), Filterable {

    private var filter: FilterContacts? = null
    private var originalList: List<Contact> = contactList
    inner class HolderMovie(binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvName
        val image = binding.ivAvatar
        val btnDelete = binding.cvDelete
        val btnAdd = binding.cvAdd
        val phoneAdapter = PhoneAdapter()

        init {
            binding.apply {
                rvPhones.adapter = phoneAdapter
                btnAdd.setOnClickListener {
                    onAddListener.invoke(getItem(absoluteAdapterPosition))
                    currentList[absoluteAdapterPosition].changeChecked()
                    notifyItemChanged(absoluteAdapterPosition)
                }
                btnDelete.setOnClickListener {
                    onDeleteListener.invoke(getItem(absoluteAdapterPosition))
                    currentList[absoluteAdapterPosition].changeChecked()
                    notifyItemChanged(absoluteAdapterPosition)
                }
            }
        }
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
            if (contact.avatarUri != null ) {
                image.setImageURI(contact.avatarUri )
            } else {
                image.setImageResource(R.drawable.baseline_person_24)
            }
            phoneAdapter.submitList(contact.phoneNumbers)
            if (contact.isSelected){
                btnDelete.show()
                btnAdd.hide()
            } else {
                btnDelete.hide()
                btnAdd.show()
            }
        }
    }

    override fun getFilter(): Filter {
        return filter ?: FilterContacts(originalList, this)
    }
}