package com.bakkac.squaremagelib.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.util.TypedValue

fun Int.dp(): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()
}

fun Int.px(): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this.toFloat(), Resources.getSystem().displayMetrics).toInt()
}

// Extension function to resize bitmap using new width value by keeping aspect ratio
fun Bitmap.resizeByWidth(width:Int):Bitmap{
    val ratio:Float = this.width.toFloat() / this.height.toFloat()
    val height:Int = Math.round(width / ratio)

    return Bitmap.createScaledBitmap(
        this,
        width,
        height,
        false
    )
}


// Extension function to resize bitmap using new height value by keeping aspect ratio
fun Bitmap.resizeByHeight(height:Int):Bitmap{
    val ratio:Float = this.height.toFloat() / this.width.toFloat()
    val width:Int = Math.round(height / ratio)

    return Bitmap.createScaledBitmap(
        this,
        width,
        height,
        false
    )
}