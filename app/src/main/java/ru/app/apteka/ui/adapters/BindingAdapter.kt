package ru.app.apteka.ui.adapters

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("app:url", "app:errorImage")
fun loadImage(image: ImageView, url: String, errorImage: Drawable) {
    Picasso.get().load(url).error(errorImage).into(image)
}