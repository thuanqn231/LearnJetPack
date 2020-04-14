package com.example.learnjetpack.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.learnjetpack.R

fun getProgressDrawable(context: Context):CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius= 50f
        start()
    }
}
fun ImageView.loadImage(uir:String?,progressDrawable: CircularProgressDrawable){
    val option = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_launcher_foreground)
    Glide.with(context)
        .setDefaultRequestOptions(option)
        .load(uir)
        .into(this)
}