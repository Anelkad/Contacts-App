package com.example.contacts

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.contacts.databinding.ContactListFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ContactListFragment : BottomSheetDialogFragment() {

    private var binding: ContactListFragmentBinding? = null
    private var contactsAdapter: ContactsAdapter? = null
    private val selectedContacts = mutableListOf<Contact>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ContactListFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        contactsAdapter = ContactsAdapter(
            onAddListener = ::addContact,
            onDeleteListener = ::deleteContact
        )
        binding?.apply {
            rvContacts.adapter = contactsAdapter
            rvContacts.itemAnimator = null
            btnClose.setOnClickListener { dismiss() }
        }
        contactsAdapter?.submitList(
            arguments?.getParcelableArrayList<Contact>(CONTACTS_LIST)?.toMutableList()
        )
    }

    private fun deleteContact(contact: Contact) {
        selectedContacts.remove(contact)
        Toast.makeText(
            context,
            getString(R.string.contact_deleted, contact.name),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun addContact(contact: Contact) {
        selectedContacts.add(contact)
        Toast.makeText(
            context,
            getString(R.string.contact_added, contact.name),
            Toast.LENGTH_SHORT
        ).show()
    }
}