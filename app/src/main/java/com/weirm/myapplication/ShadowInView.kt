package com.weirm.myapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * 怎么在一个View里面设置内阴影
 * 1.普通绘制控件
 * 2.使用setShadowLayer
 * 3.通过path进行Clip
 * 4.绘制内部阴影
 */

class ShadowInView(context: Context, attrs: AttributeSet) : View(context, attrs) {


    val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        // 设置个圆头
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 20f
        //需要使用Stroke，如果使用Fill，会导致内部没有阴影
        style = Paint.Style.STROKE
        //注意这里的阴影偏移量
        setShadowLayer(50f, 0f, 0f, Color.RED)
        color = Color.RED
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        //绘制背景
        drawIndicator(canvas)
    }

    fun drawIndicator(canvas: Canvas?) {
        val rect1 = RectF(100f, 200f, 1000f, 300f)
        val path = Path()

        path.addRoundRect(rect1, 200f, 200f, Path.Direction.CCW)
        canvas!!.clipPath(path)
        canvas.drawPath(path, dotPaint)

    }
}