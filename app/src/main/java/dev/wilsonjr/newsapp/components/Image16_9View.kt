package dev.wilsonjr.newsapp.components

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class Image16_9View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    private val ratio = 16 / 9f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, (measuredWidth / ratio).toInt())
    }
}