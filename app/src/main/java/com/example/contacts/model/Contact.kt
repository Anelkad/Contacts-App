package com.example.contacts.model

import android.net.Uri
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Contact(
    val id: Long,
    val name: String,
    val phoneNumbers: List<String>,
    val avatarUri: Uri?,
    var isSelected: Boolean = false
) : Parcelable {
    fun changeChecked() {
        isSelected = !isSelected
    }
}

