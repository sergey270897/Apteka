package ru.app.pharmacy.ui.adapters

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import ru.app.pharmacy.BuildConfig

@BindingAdapter("app:url", "app:errorImage")
fun loadImage(image: ImageView, url: String, errorImage: Drawable) {
    val newUrl = if (url.contains("http")) url else BuildConfig.URL + url
    Picasso.get().load(newUrl).error(errorImage).into(image)
}