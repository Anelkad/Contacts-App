package com.example.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contacts.databinding.ContactListFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ContactListFragment: BottomSheetDialogFragment() {

    private var binding: ContactListFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ContactListFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }
}