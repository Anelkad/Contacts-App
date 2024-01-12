package com.example.contacts

import android.widget.Filter

class FilterContacts(
    private val filterList: List<Contact>,
    private val adapter: ContactsAdapter
) : Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val results = FilterResults()

        if (!constraint.isNullOrEmpty()) {
            val filteredLanguages = filterList.filter {
                it.phoneNumbers.joinToString("").contains(constraint) || it.name.contains(constraint, ignoreCase = true)
            }
            results.count = filteredLanguages.size
            results.values = filteredLanguages
        } else {
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        adapter.submitList(results?.values as? List<Contact> ?: emptyList())
    }
}