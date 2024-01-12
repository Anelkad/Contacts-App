package com.example.contacts

import android.net.Uri
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Contact(
    val id: Long,
    val name: String,
    val phoneNumbers: List<String>,
    val photoUri: Uri?
) : Parcelable

