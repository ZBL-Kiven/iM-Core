package com.zj.imcore.ui.chat.func;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import com.zj.base.utils.DPUtils;
import com.zj.imcore.R;

import java.util.ArrayList;

public class FuncGridView extends GridView {

    public static int FUNC_ITEM_ID_PIC = 0;
    public static int FUNC_ITEM_ID_TAKE_PIC = 1;
    public static int FUNC_ITEM_ID_AUDIO = 2;
    public static int FUNC_ITEM_ID_FILE = 3;

    public FuncGridView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public FuncGridView(Context context, FuncsAdapter.OnItemClickListener listener) {
        super(context);
        setNumColumns(4);
        setBackgroundColor(context.getResources().getColor(R.color.color_primary_color_mediu_light));
        setOverScrollMode(OVER_SCROLL_NEVER);
        setVerticalSpacing(DPUtils.dp2px(30));
        int pading = DPUtils.dp2px(10);
        setHorizontalSpacing(pading);
        setVerticalScrollBarEnabled(false);
        setPadding(pading, pading * 3, pading, pading * 3);
        init(listener);
    }

    protected void init(FuncsAdapter.OnItemClickListener listener) {
        ArrayList<AppBean> mAppBeanList = new ArrayList<>();
        mAppBeanList.add(new AppBean(FUNC_ITEM_ID_PIC, R.mipmap.app_emo_func_icon_photo, "图片"));
        mAppBeanList.add(new AppBean(FUNC_ITEM_ID_TAKE_PIC, R.mipmap.app_emo_func_icon_camera, "拍照"));
        mAppBeanList.add(new AppBean(FUNC_ITEM_ID_AUDIO, R.mipmap.app_emo_func_icon_audio, "视频"));
        mAppBeanList.add(new AppBean(FUNC_ITEM_ID_FILE, R.mipmap.app_emo_func_icon_file, "文件"));
        FuncsAdapter adapter = new FuncsAdapter(getContext(), mAppBeanList, listener);
        setAdapter(adapter);
    }
}
