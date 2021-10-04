package com.examlounge.examloungeapp.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import com.bumptech.glide.RequestManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class HtmlImageGetter(
    private val scope: LifecycleCoroutineScope,
    private val res: Resources,
    private val glide: RequestManager,
    private val htmlTextView: TextView
) : Html.ImageGetter {

    override fun getDrawable(url: String): Drawable {
        val holder = BitmapDrawablePlaceHolder(res, null)

        scope.launch(Dispatchers.IO) {
            runCatching {
                val bitmap = glide
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get()

                val drawable = BitmapDrawable(res, bitmap)

                var scale = 1.00 // This makes the image scale in size.
                var width = (drawable.intrinsicWidth * scale).roundToInt()
                var height = (drawable.intrinsicHeight * scale).roundToInt()

                if(width in 400..799){
                    scale=0.8
                    width = (drawable.intrinsicWidth * scale).roundToInt()
                    height = (drawable.intrinsicHeight * scale).roundToInt()
                }
                if(width>800){
                    scale=0.5
                    width = (drawable.intrinsicWidth * scale).roundToInt()
                    height = (drawable.intrinsicHeight * scale).roundToInt()
                }
                if(width>1200){
                    scale=0.3
                    width = (drawable.intrinsicWidth * scale).roundToInt()
                    height = (drawable.intrinsicHeight * scale).roundToInt()
                }
                Log.d("Intrinsic Width",width.toString())
                drawable.setBounds(0, 0, width, height)

                holder.setDrawable(drawable)
                holder.setBounds(0, 0, width, height)

                withContext(Dispatchers.Main) { htmlTextView.text = htmlTextView.text }
            }
        }

        return holder
    }

    internal class BitmapDrawablePlaceHolder(res: Resources, bitmap: Bitmap?) : BitmapDrawable(res, bitmap) {
        private var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            drawable?.run { draw(canvas) }
        }

        fun setDrawable(drawable: Drawable) {
            this.drawable = drawable
        }
    }
}