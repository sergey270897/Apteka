package ru.app.apteka.ui.adapters

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import ru.app.apteka.BuildConfig

@BindingAdapter("app:url", "app:errorImage")
fun loadImage(image: ImageView, url: String, errorImage: Drawable) {
    Picasso.get().load(BuildConfig.URL + url).error(errorImage).into(image)
}