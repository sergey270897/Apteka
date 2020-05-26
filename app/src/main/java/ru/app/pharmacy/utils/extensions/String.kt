package ru.app.pharmacy.utils.extensions

import java.util.regex.Pattern

fun String.getInitials(): String = this[0].toUpperCase().toString()

fun String.simpleValidationEmail(): Boolean {
    val email =
        "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    val pattern = Pattern.compile(email)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}
