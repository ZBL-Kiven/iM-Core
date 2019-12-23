package com.zj.list.divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings({"unused", "WeakerAccess"})
public class RecyclerViewDivider extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private int mPadding = 0;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mOrientation;

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};


    public RecyclerViewDivider(Context context, @RecyclerView.Orientation int orientation) {
        this.mDividerHeight = 2;
        if (orientation != 1 && orientation != 0) {
            throw new IllegalArgumentException("param only supported RecyclerView.Orientation");
        } else {
            this.mOrientation = orientation;
            TypedArray a = context.obtainStyledAttributes(ATTRS);
            this.mDivider = a.getDrawable(0);
            a.recycle();
        }
    }

    public RecyclerViewDivider(Context context, int orientation, int drawableId) {
        this(context, orientation);
        this.mDivider = ContextCompat.getDrawable(context, drawableId);
        if (mDivider != null) {
            this.mDividerHeight = this.mDivider.getIntrinsicHeight();
        }
    }

    public RecyclerViewDivider(Context context, int orientation, int dividerHeight, int dividerColor) {
        this(context, orientation);
        this.mDividerHeight = dividerHeight;
        this.mPaint = new Paint(1);
        this.mPaint.setColor(dividerColor);
        this.mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, this.mDividerHeight);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (this.mOrientation == 1) {
            this.drawVertical(c, parent);
        } else {
            this.drawHorizontal(c, parent);
        }
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + layoutParams.bottomMargin + Math.round(child.getTranslationY());
            int bottom = top + this.mDividerHeight;
            if (this.mDivider != null) {
                this.mDivider.setBounds(left, top, right, bottom);
                this.mDivider.draw(canvas);
            }

            if (this.mPaint != null) {
                canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, this.mPaint);
            }
        }
        canvas.restore();
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        int childSize = parent.getChildCount();

        for (int i = 0; i < childSize; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + layoutParams.rightMargin + Math.round(child.getTranslationX());
            int right = left + this.mDividerHeight;
            if (this.mDivider != null) {
                this.mDivider.setBounds(left, top, right, bottom);
                this.mDivider.draw(canvas);
            }

            if (this.mPaint != null) {
                canvas.drawRect((float) left, (float) top, (float) right, (float) bottom, this.mPaint);
            }
        }
        canvas.restore();
    }
}