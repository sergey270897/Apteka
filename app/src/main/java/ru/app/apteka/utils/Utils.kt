package ru.app.apteka.utils

import android.content.Context

fun dpToPx(context: Context, value: Int): Int {
    val scale = context.resources.displayMetrics.density
    return (value * scale + 0.5f).toInt()
}
