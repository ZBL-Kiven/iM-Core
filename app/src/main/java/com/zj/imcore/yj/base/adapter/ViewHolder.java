package com.zj.imcore.yj.base.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.zj.list.holders.BaseViewHolder;

/**
 * ViewHolder 工具类
 *
 * @author yangji
 */
public class ViewHolder extends BaseViewHolder {

    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public ViewHolder(Context context, BaseAdapter<?> adapter, View itemView) {
        super(adapter, itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public static ViewHolder createViewHolder(BaseAdapter<?> adapter, View itemView) {
        return createViewHolder(itemView.getContext(), adapter, itemView);
    }

    public static ViewHolder createViewHolder(Context context, BaseAdapter<?> adapter, View itemView) {
        return new ViewHolder(context, adapter, itemView);
    }

    public static ViewHolder createViewHolder(BaseAdapter<?> adapter,
                                              ViewGroup parent, int layoutId) {
        return createViewHolder(parent.getContext(), adapter, parent, layoutId);
    }

    public static ViewHolder createViewHolder(Context context, BaseAdapter<?> adapter,
                                              ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        return new ViewHolder(context, adapter, itemView);
    }

    /**
     * 通过idRes获取控件
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int idRes) {
        View view = mViews.get(idRes);
        if (view == null) {
            view = mConvertView.findViewById(idRes);
            mViews.put(idRes, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }


    /**
     * 设置TextView的值
     *
     * @return
     */
    public ViewHolder setHolderText(@IdRes int idRes, String text) {
        TextView tv = getView(idRes);
        tv.setText(text);
        return this;
    }

    public ViewHolder setHolderImageResource(@IdRes int idRes, int resId) {
        ImageView view = getView(idRes);
        view.setImageResource(resId);
        return this;
    }

    public ViewHolder setHolderImageBitmap(@IdRes int idRes, Bitmap bitmap) {
        ImageView view = getView(idRes);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setHolderImageDrawable(@IdRes int idRes, Drawable drawable) {
        ImageView view = getView(idRes);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setHolderBackgroundColor(@IdRes int idRes, int color) {
        View view = getView(idRes);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setHolderBackgroundRes(@IdRes int idRes, int backgroundRes) {
        View view = getView(idRes);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setHolderTextColor(@IdRes int idRes, int textColor) {
        TextView view = getView(idRes);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolder setTextColorRes(@IdRes int idRes, int textColorRes) {
        TextView view = getView(idRes);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public ViewHolder setAlpha(@IdRes int idRes, float value) {
        getView(idRes).setAlpha(value);
        return this;
    }

    public ViewHolder setVisible(@IdRes int idRes, boolean visible) {
        View view = getView(idRes);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolder linkify(@IdRes int idRes) {
        TextView view = getView(idRes);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, int... idRess) {
        for (@IdRes int idRes : idRess) {
            TextView view = getView(idRes);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ViewHolder setProgress(@IdRes int idRes, int progress) {
        ProgressBar view = getView(idRes);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setProgress(@IdRes int idRes, int progress, int max) {
        ProgressBar view = getView(idRes);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setMax(@IdRes int idRes, int max) {
        ProgressBar view = getView(idRes);
        view.setMax(max);
        return this;
    }

    public ViewHolder setRating(@IdRes int idRes, float rating) {
        RatingBar view = getView(idRes);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setRating(@IdRes int idRes, float rating, int max) {
        RatingBar view = getView(idRes);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setTag(@IdRes int idRes, Object tag) {
        View view = getView(idRes);
        view.setTag(tag);
        return this;
    }

    public ViewHolder setTag(@IdRes int idRes, int key, Object tag) {
        View view = getView(idRes);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolder setChecked(@IdRes int idRes, boolean checked) {
        Checkable view = (Checkable) getView(idRes);
        view.setChecked(checked);
        return this;
    }

    /**
     * 关于事件的
     */
    public ViewHolder setOnClickListener(@IdRes int idRes,
                                         View.OnClickListener listener) {
        View view = getView(idRes);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(@IdRes int idRes,
                                         View.OnTouchListener listener) {
        View view = getView(idRes);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(@IdRes int idRes,
                                             View.OnLongClickListener listener) {
        View view = getView(idRes);
        view.setOnLongClickListener(listener);
        return this;
    }


}
