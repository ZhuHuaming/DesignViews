package com.weirm.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * 乘风破浪的小船
 */
class WaveBoatView(context: Context, attrs: AttributeSet) : View(context, attrs) {


    //一个波浪长，相当于两个二阶贝塞尔曲线的长度
    var mWaveLength: Float = 0.0f

    //一个界面里，有几个波浪
    var mWaveNumber = 2

    //起始坐标点
    var mStartX = 0.0f
    var mStartY = 0.0f

    //波浪的画笔
    private var mPaint: Paint? = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL_AND_STROKE
        strokeJoin = Paint.Join.ROUND
    }

    private var mPath: Path? = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //这里是根据要显示的数量，获取到一个浪的长度
        mWaveLength = width.toFloat() / mWaveNumber
        //这里选择二分之一的高度，作为起始位置
        mStartY = height.toFloat() / 2


    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //每次初始化Path
        mPath!!.reset()
        val halfWaveLen = mWaveLength / 2 //半个波长，即一个贝塞尔曲线长度
        mPath!!.moveTo(-mWaveLength, mStartY) //波浪的开始位置
        //每一次for循环添加一个波浪的长度到path中，根据view的宽度来计算一共可以添加多少个波浪长度
        var i = -mWaveLength
        var tempPosition = 0

        while (i <= width + mWaveLength ) {
            mPath!!.rQuadTo(
                halfWaveLen / 2.toFloat(),
                -200f,
                halfWaveLen.toFloat(),
                0f
            )
            mPath!!.rQuadTo(
                halfWaveLen / 2.toFloat(),
                200f,
                halfWaveLen.toFloat(),
                0f
            )
            i += mWaveLength
        }

        mPath!!.lineTo(width.toFloat(), height.toFloat())
        mPath!!.lineTo(0f, height.toFloat())

        mPath!!.close() //封闭path路径

        canvas!!.drawPath(mPath!!, mPaint!!)
    }

}