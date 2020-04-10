package ru.app.apteka.utils.extensions

import android.content.SharedPreferences

inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}

fun SharedPreferences.setValue(key: String, value: Any?) {
    when (value) {
        is String? -> edit { it.putString(key, value) }
        is Int -> edit { it.putInt(key, value) }
        is Float -> edit { it.putFloat(key, value) }
        is Long -> edit { it.putLong(key, value) }
        is Boolean -> edit { it.putBoolean(key, value) }
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}
