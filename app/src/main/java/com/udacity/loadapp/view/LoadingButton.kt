package com.udacity.loadapp.view

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.udacity.R
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val cornerPath = Path()
    private val cornerRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        4F,
        Resources.getSystem().displayMetrics
    )
    private var textColor = Color.WHITE
    private var primaryBackgroundColor = context.getColor(R.color.colorPrimary)
    private var secondaryBackgroundColor = context.getColor(R.color.colorPrimaryDark)
    private var circularProgressColor = context.getColor(R.color.colorAccent)

    private var widthSize = 0
    private var heightSize = 0

    private var valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(2000)


    private var textToDraw = "";
    private var progress = 0
    private var loadingState = 0;
    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { _, old, new ->
        when (new) {
            ButtonState.Loading -> {
                textToDraw = resources.getString(R.string.button_loading)
                valueAnimator.start()
            }
            ButtonState.Completed -> {
                textToDraw = resources.getString(R.string.button_name)
                valueAnimator.cancel()

                progress = 0
            }
        }

        invalidate()
    }

    init {
        buttonState = ButtonState.Completed
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            defStyleAttr,
            0
        )

        with(typedArray) {
            textColor = getColor(
                R.styleable.LoadingButton_textColor,
                textColor
            )
            primaryBackgroundColor = getColor(
                R.styleable.LoadingButton_primaryBackgroundColor,
                primaryBackgroundColor
            )
            secondaryBackgroundColor = getColor(
                R.styleable.LoadingButton_secondaryBackgroundColor,
                secondaryBackgroundColor
            )
            circularProgressColor = getColor(
                R.styleable.LoadingButton_circularProgressColor,
                circularProgressColor
            )
        }

        typedArray.recycle()
        // setup animation
        valueAnimator.apply {
            addUpdateListener {
                progress = it.animatedValue as Int
                invalidate()
            }
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0F
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // button background
        paint.color = primaryBackgroundColor
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        // loading button
        paint.color = secondaryBackgroundColor
        canvas?.drawRect(0f, 0f, widthSize * progress / 360f, heightSize.toFloat(), paint)

        // text
        paint.color = textColor
        canvas?.drawText(textToDraw, widthSize / 2.0f, heightSize / 2.0f + 30.0f, paint)

        // circle
        paint.color = circularProgressColor
        canvas?.drawArc(
            widthSize - 200f,
            50f,
            widthSize - 100f,
            150f,
            0f,
            progress.toFloat(),
            true,
            paint
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cornerPath.reset()
        cornerPath.addRoundRect(
            0f,
            0f,
            w.toFloat(),
            h.toFloat(),
            cornerRadius,
            cornerRadius,
            Path.Direction.CW
        )
        cornerPath.close()
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

}