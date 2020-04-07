package ru.app.apteka.utils.extensions

import androidx.appcompat.widget.SearchView

fun SearchView.onTextChanged(action: (q:String?)->Unit){
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(query: String?): Boolean {
            action.invoke(query)
            return true
        }
    })
}