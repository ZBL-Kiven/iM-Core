package com.zj.im.list.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.zj.im.R;


@SuppressWarnings("unused")
public class BubbleLayout extends FrameLayout {
    private Paint mPaint;
    private Path mPath;
    private Look look;
    private int bubblePadding;
    private int mWidth, mHeight;
    private int lookPosition, lookWidth, lookLength;
    private int shadowColor, shadowRadius, shadowX, shadowY;
    private int bubbleRadius, bubbleColor;
    private Region mRegion = new Region();

    /**
     * arrow pointing
     */
    public enum Look {
        LEFT(1), TOP(2), RIGHT(3), BOTTOM(4);
        int value;

        Look(int v) {
            value = v;
        }

        public static Look getType(int value) {
            Look type = Look.BOTTOM;
            switch (value) {
                case 1:
                    type = Look.LEFT;
                    break;
                case 2:
                    type = Look.TOP;
                    break;
                case 3:
                    type = Look.RIGHT;
                    break;
                case 4:
                    type = Look.BOTTOM;
                    break;
            }

            return type;
        }
    }


    public BubbleLayout(Context context) {
        this(context, null);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);
        initAttr(context.obtainStyledAttributes(attrs, R.styleable.BubbleLayout, defStyleAttr, 0));
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
        initPadding();
    }

    public void initPadding() {
        int p = bubblePadding * 2;
        switch (look) {
            case BOTTOM:
                setPadding(p, p, p, lookLength + p);
                break;
            case TOP:
                setPadding(p, p + lookLength, p, p);
                break;
            case LEFT:
                setPadding(p + lookLength, p, p, p);
                break;
            case RIGHT:
                setPadding(p, p, p + lookLength, p);
                break;
        }
    }

    /**
     * initialization parameters
     */
    private void initAttr(TypedArray a) {
        look = Look.getType(a.getInt(R.styleable.BubbleLayout_lookAt, Look.BOTTOM.value));
        lookPosition = a.getDimensionPixelOffset(R.styleable.BubbleLayout_lookPosition, 0);
        lookWidth = a.getDimensionPixelOffset(R.styleable.BubbleLayout_lookWidth, dpToPx(getContext(), 17F));
        lookLength = a.getDimensionPixelOffset(R.styleable.BubbleLayout_lookLength, dpToPx(getContext(), 17F));
        shadowRadius = a.getDimensionPixelOffset(R.styleable.BubbleLayout_shadowRadius, dpToPx(getContext(), 3.3F));
        shadowX = a.getDimensionPixelOffset(R.styleable.BubbleLayout_shadowX, dpToPx(getContext(), 1F));
        shadowY = a.getDimensionPixelOffset(R.styleable.BubbleLayout_shadowY, dpToPx(getContext(), 1F));
        bubbleRadius = a.getDimensionPixelOffset(R.styleable.BubbleLayout_bubbleRadius, dpToPx(getContext(), 7F));
        bubblePadding = a.getDimensionPixelOffset(R.styleable.BubbleLayout_bubblePadding, dpToPx(getContext(), 8));
        shadowColor = a.getColor(R.styleable.BubbleLayout_shadowColor, Color.GRAY);
        bubbleColor = a.getColor(R.styleable.BubbleLayout_bubbleColor, Color.WHITE);
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mWidth = w;
        mHeight = h;
        initData();
    }

    @Override
    public void invalidate() {
        initData();
        super.invalidate();
    }

    @Override
    public void postInvalidate() {
        initData();
        super.postInvalidate();
    }

    private void initData() {
        mPaint.setPathEffect(new CornerPathEffect(bubbleRadius));
        mPaint.setShadowLayer(shadowRadius, shadowX, shadowY, shadowColor);

        int mLeft = bubblePadding + (look == Look.LEFT ? lookLength : 0);
        int mTop = bubblePadding + (look == Look.TOP ? lookLength : 0);
        int mRight = mWidth - bubblePadding - (look == Look.RIGHT ? lookLength : 0);
        int mBottom = mHeight - bubblePadding - (look == Look.BOTTOM ? lookLength : 0);
        mPaint.setColor(bubbleColor);

        mPath.reset();

        int topOffset = (topOffset = lookPosition) + lookLength > mBottom ? mBottom - lookWidth : topOffset;
        topOffset = topOffset > bubblePadding ? topOffset : bubblePadding;
        int leftOffset = (leftOffset = lookPosition) + lookLength > mRight ? mRight - lookWidth : leftOffset;
        leftOffset = leftOffset > bubblePadding ? leftOffset : bubblePadding;

        switch (look) {
            case LEFT:
                mPath.moveTo(mLeft, topOffset);
                mPath.rLineTo(-lookLength, lookWidth / 2f);
                mPath.rLineTo(lookLength, lookWidth / 2f);
                mPath.lineTo(mLeft, mBottom);
                mPath.lineTo(mRight, mBottom);
                mPath.lineTo(mRight, mTop);
                mPath.lineTo(mLeft, mTop);
                break;
            case TOP:
                mPath.moveTo(leftOffset, mTop);
                mPath.rLineTo(lookWidth / 2f, -lookLength);
                mPath.rLineTo(lookWidth / 2f, lookLength);
                mPath.lineTo(mRight, mTop);
                mPath.lineTo(mRight, mBottom);
                mPath.lineTo(mLeft, mBottom);
                mPath.lineTo(mLeft, mTop);
                break;
            case RIGHT:
                mPath.moveTo(mRight, topOffset);
                mPath.rLineTo(lookLength, lookWidth / 2f);
                mPath.rLineTo(-lookLength, lookWidth / 2f);
                mPath.lineTo(mRight, mBottom);
                mPath.lineTo(mLeft, mBottom);
                mPath.lineTo(mLeft, mTop);
                mPath.lineTo(mRight, mTop);
                break;
            case BOTTOM:
                mPath.moveTo(leftOffset, mBottom);
                mPath.rLineTo(lookWidth / 2f, lookLength);
                mPath.rLineTo(lookWidth / 2f, -lookLength);
                mPath.lineTo(mRight, mBottom);
                mPath.lineTo(mRight, mTop);
                mPath.lineTo(mLeft, mTop);
                mPath.lineTo(mLeft, mBottom);
                break;
        }

        mPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    public Paint getPaint() {
        return mPaint;
    }

    public Path getPath() {
        return mPath;
    }

    public Look getLook() {
        return look;
    }

    public int getLookPosition() {
        return lookPosition;
    }

    public int getLookWidth() {
        return lookWidth;
    }

    public int getLookLength() {
        return lookLength;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public int getShadowRadius() {
        return shadowRadius;
    }

    public int getShadowX() {
        return shadowX;
    }

    public int getShadowY() {
        return shadowY;
    }

    public int getBubbleRadius() {
        return bubbleRadius;
    }

    public int getBubbleColor() {
        return bubbleColor;
    }

    public int getBubblePadding() {
        return bubblePadding;
    }

    public void setBubblePadding(float mBubblePadding) {
        this.bubblePadding = dpToPx(getContext(), mBubblePadding);
        initPadding();
    }

    public void setBubbleColor(int bubbleColor) {
        this.bubbleColor = bubbleColor;
    }

    public void setLook(Look look) {
        this.look = look;
        initPadding();
    }

    public void setLookPosition(float lookPosition) {
        this.lookPosition = dpToPx(getContext(), lookPosition);
    }

    public void setLookWidth(float lookWidth) {
        this.lookWidth = dpToPx(getContext(), lookWidth);
    }

    public void setLookLength(float lookLength) {
        this.lookLength = dpToPx(getContext(), lookLength);
        initPadding();
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public void setShadowRadius(float shadowRadius) {
        this.shadowRadius = dpToPx(getContext(), shadowRadius);
    }

    public void setShadowX(float shadowX) {
        this.shadowX = dpToPx(getContext(), shadowX);
    }

    public void setShadowY(float shadowY) {
        this.shadowY = dpToPx(getContext(), shadowY);
    }

    public void setBubbleRadius(float bubbleRadius) {
        this.bubbleRadius = dpToPx(getContext(), bubbleRadius);
    }

    public static int dpToPx(Context context, float dipValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
