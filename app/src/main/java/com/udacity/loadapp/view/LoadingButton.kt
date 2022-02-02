package com.udacity.loadapp.view

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.udacity.R
import java.util.*
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

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }
    private var textToDraw = context.getString(R.string.download).toUpperCase(Locale.ENGLISH)
    private val textRect = Rect()

    init {
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
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0F
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            // Save canvas
            it.save()

            // Clip canvas corners to form a rounded button
            it.clipPath(cornerPath)

            // Draw button background color
            it.drawColor(primaryBackgroundColor)

            paint.getTextBounds(textToDraw, 0, textToDraw.length, textRect)
            val textX = width / 2f - textRect.width() / 2f
            val textY = height / 2f + textRect.height() / 2f - textRect.bottom
            val textOffset = 0
            // Draw button text
            paint.color = textColor
            it.drawText(textToDraw, textX - textOffset, textY, paint)

            // Restore saved canvas
            it.restore()
        }
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