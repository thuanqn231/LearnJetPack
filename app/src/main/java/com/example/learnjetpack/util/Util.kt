package com.example.learnjetpack.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.learnjetpack.R


fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(url: String?, progressDrawable: CircularProgressDrawable) {

    //Log.i("===", "=== url :== " + url)

    if(!TextUtils.isEmpty(url)){
        val option = RequestOptions()
            .placeholder(progressDrawable)
            .error(R.drawable.ic_launcher_foreground)
        Glide.with(context)
            .setDefaultRequestOptions(option)
            .load(url)
            .into(this)
    }

}

@BindingAdapter("android.imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url ?: "", getProgressDrawable(view.context))
}


