package com.zj.im.emotionboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import androidx.annotation.NonNull;
import com.zj.im.R;
import com.zj.im.emotionboard.adpater.EmoticonPacksAdapter;
import com.zj.im.emotionboard.data.EmoticonPack;
import com.zj.im.emotionboard.interfaces.EmoticonsIndicator;
import com.zj.im.emotionboard.interfaces.EmoticonsToolBar;
import com.zj.im.emotionboard.interfaces.OnToolBarItemClickListener;
import com.zj.im.emotionboard.utils.EmoticonsKeyboardUtils;
import com.zj.im.emotionboard.widget.EmoticonsFuncView;

@SuppressWarnings("unused")
public class EmoticonsKeyBoardPopWindow extends PopupWindow implements EmoticonsFuncView.EmoticonsFuncListener, OnToolBarItemClickListener {

    private Context mContext;
    private EmoticonsFuncView emoticonsFuncView;
    private EmoticonsIndicator emoticonsIndicator;
    private EmoticonsToolBar emoticonsToolBar;

    public EmoticonsKeyBoardPopWindow(Context context) {
        super(context, null);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.view_func_emoticon, null);
        setContentView(contentView);
        setWidth(EmoticonsKeyboardUtils.getDisplayWidthPixels(mContext));
        setHeight(EmoticonsKeyboardUtils.getDefKeyboardHeight(mContext));
        setAnimationStyle(R.style.PopupAnimation);
        setOutsideTouchable(true);
        update();
        ColorDrawable dw = new ColorDrawable(0);
        setBackgroundDrawable(dw);

        updateView(contentView);
    }

    private void updateView(View view) {
        emoticonsFuncView = view.findViewById(R.id.view_epv);
        emoticonsIndicator = view.findViewById(R.id.view_eiv);
        emoticonsToolBar = view.findViewById(R.id.view_etv);
        emoticonsFuncView.setListener(this);
        emoticonsToolBar.setToolBarItemClickListener(this);
    }

    public void setAdapter(EmoticonPacksAdapter adapter) {
        if (adapter != null) {
            emoticonsToolBar.setPackList(adapter.getPackList());
            emoticonsFuncView.setAdapter(adapter);
        }

    }

    public void showPopupWindow() {
        View rootView = EmoticonsKeyboardUtils.getRootView((Activity) mContext);
        if (this.isShowing()) {
            this.dismiss();
        } else {
            EmoticonsKeyboardUtils.closeSoftKeyboard(mContext);
            this.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onCurrentEmoticonPackChanged(EmoticonPack<?> currentPack) {
        emoticonsToolBar.selectEmotionPack(currentPack);
    }

    @Override
    public void playTo(int position, EmoticonPack pack) {
        emoticonsIndicator.playTo(position, pack);
    }

    @Override
    public void onToolBarItemClick(@NonNull EmoticonPack<?> pack) {
        emoticonsFuncView.setCurrentPageSet(pack);
    }
}
