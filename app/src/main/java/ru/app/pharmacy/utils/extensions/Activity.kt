package ru.app.pharmacy.utils.extensions

import android.app.Activity
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.app.pharmacy.R

fun AppCompatActivity.startFragment(
    container: Int,
    fragment: Fragment,
    tag: String,
    addToBackStack: Boolean = true,
    setAnim: Boolean = false
) {
    val fr = this.supportFragmentManager.findFragmentByTag(tag)
    with(this.supportFragmentManager.beginTransaction()) {
        setCustomAnimations(
            R.anim.slide_from_right,
            R.anim.slide_to_left,
            R.anim.slide_from_left,
            R.anim.slide_to_right
        )
        if (fr == null) replace(container, fragment, tag) else replace(container, fr, tag)
        if (addToBackStack) addToBackStack(tag)
        commit()
    }
}

fun AppCompatActivity.hideKeyboard() {
    val view = currentFocus ?: View(this)
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}