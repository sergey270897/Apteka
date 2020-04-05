package ru.app.apteka.utils.extensions

import androidx.fragment.app.Fragment
import ru.app.apteka.ui.activities.MainActivity

fun MainActivity.startFragment(container: Int, fragment: Fragment, tag: String) {
    this.supportFragmentManager.beginTransaction()
        .replace(container, fragment, tag)
        .addToBackStack(tag)
        .commit()
}