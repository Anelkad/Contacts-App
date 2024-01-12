package com.example.contacts

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Contact(
    val id: Long,
    val name: String,
    val phoneNumbers: List<String>,
    var isSelected: Boolean = false
) : Parcelable {
    fun changeChecked() {
        isSelected = !isSelected
    }
}

