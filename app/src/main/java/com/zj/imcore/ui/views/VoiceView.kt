package com.zj.imcore.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.lang.IllegalArgumentException

/**
 * Created by ZJJ on 19/12/16
 *
 * Custom voice playback controls, which can achieve dynamic effects based on incoming types.
 *
 * Define its configuration in XML
 *
 * Includes size and color
 * */
@Suppress("unused")
class VoiceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    companion object {
        const val ORIENTATION_LEFT = 0
        const val ORIENTATION_RIGHT = 1
    }

    private var path: Path? = null
    private var levelPath: Path? = null
    private val paint = Paint()
    private val levelPaint = Paint()
    private val porterDuffPaint = Paint()
    private var mWidth: Float = 0f
    private var mHeight: Float = 0f

    private var mArcStrokeWidth = 2f
        get() = dpToPx(context, field) * 1.0f
    private var mArcStrokeMargin = 1.5f
        get() = dpToPx(context, field) * 1.0f

    private var orientation = ORIENTATION_RIGHT

    private var mArcCount = 5
    private var mAngleStart = 325f
    private var mAngleSweep = 70f
    private var defaultColor = Color.GRAY
    private var renderColor = Color.DKGRAY

    private var curLevel = 0

    private var porterDuff = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)

    private var levelBmp: Bitmap? = null


    private fun getSingleArcRect(i: Int): RectF {
        val left: Float
        val top: Float
        val right: Float
        val bottom: Float

        when (orientation) {
            ORIENTATION_RIGHT -> {
                val len = i * (mArcStrokeWidth + mArcStrokeMargin) + mArcStrokeWidth
                left = -len
                top = mHeight / 2f - len
                right = left + 2f * len
                bottom = top + 2f * len
            }
            ORIENTATION_LEFT -> {
                val len = i * (mArcStrokeWidth + mArcStrokeMargin) + mArcStrokeMargin * 1.5f
                val lenF = (mArcCount - 1 - i) * (mArcStrokeWidth + mArcStrokeMargin) + mArcStrokeMargin * 1.5f
                left = lenF
                top = mHeight / 2f - len
                right = left + 2f * len
                bottom = top + 2f * len
            }
            else -> throw IllegalArgumentException("orientation just support the one of ORIENTATION_LEFT ,ORIENTATION_RIGHT")
        }
        return RectF(left, top, right, bottom)
    }

    private fun getLevelArcRect(w: Float, h: Float): RectF {
        val curPercent = curLevel / 100f
        val offsetW = w * curPercent
        return RectF(-w / 2f - mArcStrokeWidth, 0f, offsetW, h)
    }

    private fun initPaint(): Paint {
        paint.reset()
        paint.flags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
        paint.style = Paint.Style.STROKE
        paint.color = defaultColor
        paint.strokeWidth = mArcStrokeWidth
        return paint
    }

    private fun levelPaint(): Paint {
        levelPaint.reset()
        levelPaint.flags = Paint.ANTI_ALIAS_FLAG
        levelPaint.style = Paint.Style.FILL_AND_STROKE
        levelPaint.color = renderColor
        return levelPaint
    }

    private fun getXFerPaint(): Paint {
        porterDuffPaint.reset()
        porterDuffPaint.flags = Paint.ANTI_ALIAS_FLAG
        porterDuffPaint.xfermode = porterDuff
        return porterDuffPaint
    }

    override fun postInvalidate() {
        initData()
        super.postInvalidate()
    }

    override fun invalidate() {
        initData()
        super.invalidate()
    }

    private fun initData() {
        if (path == null) path = Path()
        path?.reset()
        if (levelPath == null) levelPath = Path()
        levelPath?.reset()

        val range = if (orientation == ORIENTATION_RIGHT) 0 until mArcCount else (mArcCount - 1).downTo(0)
        for (i in range) {
            val r = getSingleArcRect(i)
            val angleStart = mAngleStart - (if (orientation == ORIENTATION_RIGHT) 0 else 180)
            path?.addArc(r, angleStart, mAngleSweep)
        }
        if (curLevel > 0) {
            val levelRect = getLevelArcRect(mWidth * 1.0f, mHeight * 1.0f)
            val w = (levelRect.width()).toInt()
            val h = (levelRect.height()).toInt()
            if (w > 0 && h > 0) {
                levelBmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)?.let {
                    val canvas = Canvas(it)
                    val p = if (orientation == ORIENTATION_RIGHT) PointF(0f, levelRect.centerY())
                    else PointF(mWidth, levelRect.centerY())
                    canvas.drawCircle(p.x, p.y, curLevel / 100f * (w * .6f), levelPaint())
                    it
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.mWidth = width * 1.0f
        this.mHeight = height * 1.0f
        initData()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sw = getSingleArcRect(mArcCount - 1)
        val w = (sw.width() / 2f + mArcStrokeWidth).toInt()
        val h = (sw.height() / 2f + mArcStrokeWidth * (mArcCount / 2f + 0.5f)).toInt()
        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        val rc = canvas?.saveLayer(0f, 0f, mWidth * 1.0f, mHeight * 1.0f, initPaint())
        this.path?.let {
            canvas?.drawPath(it, initPaint())
        }
        canvas?.save()
        levelBmp?.let {
            canvas?.drawBitmap(it, 0f, 0f, getXFerPaint())
        }

        canvas?.restoreToCount(rc ?: 0)
    }


    fun getOrientation(): Int {
        return orientation
    }

    fun getLevel(): Int {
        return curLevel
    }

    fun setLevel(progress: Int) {
        curLevel = progress
        postInvalidate()
    }

    fun setOrientation(orientation: Int) {
        this.orientation = orientation
        postInvalidate()
    }

    private fun dpToPx(context: Context, dipValue: Float): Int {
        val scale = context.applicationContext.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}