package ru.app.apteka.utils.extensions

import androidx.fragment.app.Fragment
import ru.app.apteka.ui.activities.MainActivity

fun MainActivity.startFragment(
    container: Int,
    fragment: Fragment,
    tag: String,
    addToBackStack: Boolean = true
) {
    val fr = this.supportFragmentManager.findFragmentByTag(tag)
    with(this.supportFragmentManager.beginTransaction()) {
        if (fr == null) replace(container, fragment, tag) else replace(container, fr, tag)
        if (addToBackStack) addToBackStack(tag)
        commit()
    }
}