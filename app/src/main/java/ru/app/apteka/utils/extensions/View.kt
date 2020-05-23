package ru.app.apteka.utils.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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

fun EditText.onTextChanged(action: (s: CharSequence?) -> Unit){
    this.addTextChangedListener(object:TextWatcher{
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            action.invoke(s)
        }
    })
}