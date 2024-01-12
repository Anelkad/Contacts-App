package com.example.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.example.contacts.databinding.ActivityMainBinding
import com.example.contacts.model.Contact
import com.example.contacts.utils.CONTACTS_LIST

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var contactList = arrayListOf<Contact>()
    private val contactsFragment = ContactListFragment()

    private val requestContactPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            contactList = getContacts()
            showContactsFragment()
        }
    }
    private fun requestContactPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestContactPermissionLauncher.launch(
                Manifest.permission.READ_CONTACTS
            )
        } else {
            contactList = getContacts()
            showContactsFragment()
        }
    }

    private fun getContacts(): ArrayList<Contact> {
        val contactList = arrayListOf<Contact>()

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts.PHOTO_URI
        )
        val sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val contactName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)) == 1
                val contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val avatarUri = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI))

                if (hasPhoneNumber) {
                    contactList.add(
                        Contact(
                            id = contactId,
                            name = contactName,
                            phoneNumbers = getContactPhones(contactId),
                            avatarUri = if (avatarUri != null) Uri.parse(avatarUri) else null
                        )
                    )
                }
            }
            cursor.close()
        }
        return contactList
    }

    private fun getContactPhones(contactId: Long): List<String> {
        val phoneProjection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val phoneSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?"

        val phoneCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            phoneProjection,
            phoneSelection,
            arrayOf(contactId.toString()),
            null
        )

        val phoneNumbers = mutableListOf<String>()
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                phoneNumbers.add(phoneCursor.getString(
                    phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                )
            }
            phoneCursor.close()
        }
        return phoneNumbers
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        requestContactPermissions()

        binding?.btnContacts?.setOnClickListener { showContactsFragment() }
    }

    private fun showContactsFragment(){
        contactsFragment.arguments = bundleOf(CONTACTS_LIST to contactList)
        contactsFragment.show(supportFragmentManager, contactsFragment.tag)
    }
}