package com.example.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.example.contacts.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var contactList = arrayListOf<Contact>()

    private val requestContactPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
            contactList = getContacts()
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
        }
    }

    private fun getContacts(): ArrayList<Contact> {
        val contactList = arrayListOf<Contact>()

        val projection = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts._ID
        )
        val selection = null
        val sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC"

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) == 1
                val contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                if (hasPhoneNumber) {
                    contactList.add(getContactPhones(contactId, contactName))
                }
            }
            cursor.close()
        }
        return contactList
    }

    private fun getContactPhones(contactId: Long, contactName: String) : Contact{
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
                phoneNumbers.add(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
            }
            phoneCursor.close()
        }

        val photoUri = ContactsContract.Contacts.getLookupUri(contactId, "")

        return Contact(contactId, contactName, phoneNumbers, photoUri)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        requestContactPermissions()
        showContactsFragment()

        binding?.btnContacts?.setOnClickListener { showContactsFragment() }
    }

    private fun showContactsFragment(){
        val contactsFragment = ContactListFragment()
        contactsFragment.arguments = bundleOf(CONTACTS_LIST to contactList)
        contactsFragment.show(supportFragmentManager, contactsFragment.tag)
    }
}