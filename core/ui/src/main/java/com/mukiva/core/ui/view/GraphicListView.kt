package com.mukiva.core.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.mukiva.core.ui.R

class GraphicListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var mGraphicViewPort: Rect = Rect(0,0,0,0)
    private val mGraphicLineSeparator = Paint().apply {
        strokeWidth = DEFAULT_LINE_WIDTH
        color = Color.WHITE
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 30f)
        alpha = 50

    }
    private val mGraphicLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 5f
        color = Color.BLUE
        style = Paint.Style.FILL_AND_STROKE
    }
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 50f
        color = Color.WHITE
    }
    private var mNormalizedValues: FloatArray = floatArrayOf()
    private var mLinePath: Path = Path()
    private val mTmpDots = mutableListOf<Int>()

    init {
        attrs?.let {
            context.withStyledAttributes(attrs, R.styleable.GraphicListView, defStyleAttr) {
                mGraphicLineSeparator.color =
                    getColor(R.styleable.GraphicListView_graphic_grid_color, DEFAULT_GRID_COLOR)
                mGraphicLinePaint.color =
                    getColor(R.styleable.GraphicListView_graphic_line_color, DEFAULT_LINE_COLOR)
                mTextPaint.color =
                    getColor(R.styleable.GraphicListView_graphic_text_color, DEFAULT_TEXT_COLOR)
                mTextPaint.textSize =
                    getDimension(R.styleable.GraphicListView_graphic_text_size, DEFAULT_TEXT_SIZE)
            }
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        (adapter as? IValueProvider)?.let { provider ->
            provider.registerOnListSubmitted {
                mNormalizedValues = getNormalizedValues()
                invalidate()
            }
        }
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)

        val height = measuredHeight + DEFAULT_GRAPHIC_SIZE
        val width = measuredWidth * 2

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mGraphicViewPort = Rect(
            paddingLeft,
            measuredHeight - DEFAULT_GRAPHIC_SIZE - paddingBottom,
            w - paddingRight,
            measuredHeight - paddingBottom
        )

        updatePadding(
            left = w / 4,
            right = w / 4
        )
    }

    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        if (mNormalizedValues.isNotEmpty())
            generateLinePath()
        c.drawGrid()
        c.drawPath(mLinePath, mGraphicLinePaint)
        c.drawDotLabels()
    }

    private fun Canvas.drawDotLabels() {
        val provider = (adapter as? IValueProvider) ?: return
        (0..<childCount).forEach {
            val item = getChildAt(it)
            val pos = getChildAdapterPosition(item)
            val text = provider.getFormattedValue(pos)

            val width = mTextPaint.measureText(text)

            val x = mTmpDots[it * 2].toFloat() - width / 2
            val y = mTmpDots[it * 2 + 1].toFloat() - 25

            drawText(text, x, y, mTextPaint)
        }
    }

    private fun Canvas.drawGrid() {
        (0..<childCount).forEach {
            val item = getChildAt(it)
            item?.run {
                val itemCenter = item.left + item.width / 2f
                drawLine(
                    itemCenter,
                    item.height.toFloat(),
                    itemCenter,
                    (mGraphicViewPort.height() + item.height).toFloat(),
                    mGraphicLineSeparator
                )
            }
        }
    }

    private fun getNormalizedValues(): FloatArray {
        val result = mutableListOf<Float>()
        val itemCount = adapter?.itemCount ?: 0
        val provider = (adapter as? IValueProvider)
            ?: return result.toFloatArray()
        val maxValue = provider.getMaxValue()
        val minValue = provider.getMinValue()
        val diff = maxValue - minValue
        (0..<itemCount).forEach {
            val value = provider.getValueByPos(it)
            result.add(
                1f - (value - minValue) / diff
            )
        }
        return result.toFloatArray()
    }

    private fun generateLinePath() {
        val viewPortHeight = mGraphicViewPort.height()
        mLinePath.apply {
            rewind()
            mTmpDots.clear()
            if (childCount == 0) return
            val startItem = getChildAt(0)
            val startX = startItem.left + startItem.width / 2
            val startY = startItem.height + viewPortHeight
            moveTo(startX.toFloat(), startY.toFloat())
            (0..<childCount).forEach {
                val item = getChildAt(it)
                val adapterIndex = getChildAdapterPosition(item)
                item?.let { _ ->
                    val x = item.left + item.width * 0.5f
                    val y = (viewPortHeight - DEFAULT_GRAPHIC_VERTICAL_PADDING * 2) * mNormalizedValues[adapterIndex] + item.height
                    mTmpDots.add(x.toInt())
                    mTmpDots.add((y + DEFAULT_GRAPHIC_VERTICAL_PADDING).toInt())
                    lineTo(x, y + DEFAULT_GRAPHIC_VERTICAL_PADDING)
                    addCircle(x, y + DEFAULT_GRAPHIC_VERTICAL_PADDING, DEFAULT_DOT_RADIUS, Path.Direction.CW)
                    moveTo(x, y + DEFAULT_GRAPHIC_VERTICAL_PADDING)
                }
            }
            val lastItem = getChildAt(childCount - 1)
            val lastX = lastItem.left + lastItem.width / 2
            val lastY = lastItem.height + viewPortHeight
            lineTo(lastX.toFloat(), lastY.toFloat())
        }
    }

    companion object {
        private const val DEFAULT_GRAPHIC_SIZE = 500
        private const val DEFAULT_LINE_WIDTH = 1.5f
        private const val DEFAULT_DOT_RADIUS = 10f
        private const val DEFAULT_GRAPHIC_VERTICAL_PADDING = 50f
        private const val DEFAULT_TEXT_SIZE = 50f
        private const val DEFAULT_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_GRID_COLOR = Color.WHITE
        private const val DEFAULT_LINE_COLOR = Color.BLUE
    }
}