package com.weirm.myapplication

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnRepeat
import androidx.core.graphics.drawable.toBitmap
import java.util.*
import kotlin.math.atan2
import kotlin.random.Random

/**
 * 乘风破浪的小船
 */
class WaveBoatView(context: Context, attrs: AttributeSet) : View(context, attrs) {


    //一个波浪长，相当于两个二阶贝塞尔曲线的长度
    var mWaveLength: Float = 0f

    //一个界面里，有几个波浪
    var mWaveNumber = 3

    //起始坐标点
    var mStartX = 0.0f
    var mStartY = 0.0f

    //Path移动的坐标点
    var dx = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    //保存随机浪
    @Volatile
    var heightList = LinkedList<Float>()

    //小船
    lateinit var boat: Bitmap

    init {
        for (index in 0 until mWaveNumber+10) {
            heightList.add(Random.nextInt(500).toFloat())
        }
        boat = resources.getDrawable(R.drawable.ic_boat).toBitmap()
    }

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
        startAnim()

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //每次初始化Path
        mPath!!.reset()
        val halfWaveLen = mWaveLength / 2 //半个波长，即一个贝塞尔曲线长度
        mPath!!.moveTo(-mWaveLength + dx, mStartY) //波浪的开始位置
        //每一次for循环添加一个波浪的长度到path中，根据view的宽度来计算一共可以添加多少个波浪长度
        var i = -mWaveLength
        //现在是第几个浪
        var waveIndex = 0
        while (i <= width.toFloat()) {
            mPath!!.rQuadTo(
                halfWaveLen / 2.toFloat(),
                //使用随机的波浪高度
                -heightList.get(waveIndex),
                halfWaveLen.toFloat(),
                0f
            )
            mPath!!.rQuadTo(
                halfWaveLen / 2.toFloat(),
                //使用随机的波浪高度
                heightList.get(waveIndex),
                halfWaveLen.toFloat(),
                0f
            )
            i += mWaveLength
            waveIndex++
        }

        mPath!!.lineTo(width.toFloat(), height.toFloat())
        mPath!!.lineTo(0f, height.toFloat())

        mPath!!.close() //封闭path路径

        canvas!!.drawPath(mPath!!, mPaint!!)

        //这里我默认小船居中
        //微积分的概念，来获取切线角度
        /*var x = width.toFloat() / 2
        var region = Region()
        var region2 = Region()
        var clip = Region((x - 0.1).toInt(), 0, x.toInt(), height)
        var clip2 = Region((x - 10).toInt(), 0, (x - 9).toInt(), height)
        region.setPath(mPath!!, clip)
        region2.setPath(mPath!!, clip2)

        var rect = region.getBounds()
        var rect2 = region2.getBounds()

        val fl =
            -atan2(-rect.top.toFloat() + rect2.top.toFloat(), 9.5f) * 180 / Math.PI.toFloat()

        canvas.save()

        canvas.rotate(
            fl, rect.right.toFloat(),
            rect.top.toFloat()
        )
        canvas.drawBitmap(
            boat,
            rect.right.toFloat() - boat.width / 2,
            rect.top.toFloat() - boat.height / 4 * 3,
            mPaint
        )
        canvas.restore()*/

    }

    fun startAnim() {
        //根据一个动画不断得到0~mItemWaveLength的值dx，通过dx的增加不断去改变波浪开始的位置，dx的变化范围刚好是一个波浪的长度，
        //所以可以形成一个完整的波浪动画，假如dx最大小于mItemWaveLength的话， 就会不会画出一个完整的波浪形状
        var anim: ObjectAnimator =
            ObjectAnimator.ofFloat(WaveBoatView@ this, "dx", 0f, mWaveLength)
        anim.duration = 1000

        anim.start()
        anim.interpolator = LinearInterpolator()
        anim.doOnEnd {
            handler.post {
                heightList.addFirst(Random.nextInt(500).toFloat())
                heightList.removeAt(10)
                anim.start()//放在这里可以重复执行动画
            }
        }
        anim.start()
    }

}