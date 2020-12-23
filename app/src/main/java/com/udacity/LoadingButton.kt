package com.udacity

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.ButtonBarLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import kotlin.math.min
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var radius= 0.0f
    private lateinit var buttonText: String
    var width:Float= 0F
    var sweepAngle:Float=0F


    private var valueAnimator = ValueAnimator()
    private var circleAnimator=ValueAnimator()
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
       if(new==ButtonState.Loading) {
           buttonText = "We are loading"
           valueAnimator = ValueAnimator.ofFloat(0F, measuredWidth.toFloat()).apply {
               duration = 1000
               addUpdateListener { animation ->
                   width = animation.animatedValue as Float
                   animation.repeatCount = ValueAnimator.INFINITE
                   animation.repeatMode = ValueAnimator.REVERSE
                   invalidate()
               }
               start()
           }
           circleAnimator = ValueAnimator.ofFloat(0F, 360F).apply {
               duration = 1000
               addUpdateListener { animation ->
                   sweepAngle = animation.animatedValue as Float
                   animation.repeatCount = ValueAnimator.INFINITE
                   invalidate()
               }
               start()
           }

       }
        else if(new==ButtonState.Completed){
            buttonText="Download"
           width=0F;
           valueAnimator.removeAllListeners()
           valueAnimator.end()
           circleAnimator.removeAllListeners()
           circleAnimator.end()
       }

    }

    private val paint= Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style=Paint.Style.FILL
        textAlign=Paint.Align.CENTER
        textSize=55.0f
        typeface= Typeface.create("",Typeface.BOLD)
    }


    init {
        buttonText="Download"
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius= (min(w,h) / 2 * 0.5).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color=getColor(context,R.color.colorPrimary)
        canvas!!.drawColor(paint.color)
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
        paint.color=Color.WHITE
        paint.textSize=50.0F
        paint.textAlign=Paint.Align.CENTER
        canvas.drawText(buttonText,measuredWidth.toFloat()/2,measuredHeight.toFloat()/2,paint)
        paint.color=getColor(context,R.color.colorPrimaryDark)
        canvas.drawRect(0f,0f,width,measuredHeight.toFloat(),paint)
        paint.color=Color.WHITE
        canvas.drawText(buttonText,measuredWidth.toFloat()/2,measuredHeight.toFloat()/2,paint)
        paint.color=Color.YELLOW
        canvas.drawArc( (widthSize - 90f),
                (heightSize / 2) - 50f,
                (widthSize-50f ),
                (heightSize / 2) + 50f,
                0F,sweepAngle,
                true,paint)
        if(buttonState==ButtonState.Completed){
            paint.color=getColor(context,R.color.colorPrimary)
            canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
            paint.color=Color.WHITE
            paint.textSize=50.0F
            paint.textAlign=Paint.Align.CENTER
            canvas.drawText(buttonText,measuredWidth.toFloat()/2,measuredHeight.toFloat()/2,paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
     fun setLoadingState(state:ButtonState){
        buttonState=state
    }

}