package com.zj.imcore.ui.list.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatButton;
import com.zj.imcore.R;

public class VoiceBottom extends AppCompatButton {
 
    //对应三种状态
    //没有操作
    public static final int NOT_VOICE = 1;
    //正在说话
    public static final int CUR_VOICE = 2;
    //取消发送
    public static final int CANCEL_VOICE = 3;
 
    //当前状态
    private int mCurState = NOT_VOICE;
    //是否在录音
    private boolean mIsVoice;

    public VoiceBottom(Context context) {
        this(context, null);
    }

    public VoiceBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //当前坐标
        float x = event.getX();
        float y = event.getY();
        //当前手势
        int action = event.getAction();
        switch (action) {
            //不同手势切换不同的状态
            case MotionEvent.ACTION_DOWN:
                //TODO
                changeState(CUR_VOICE);
                break;
            case MotionEvent.ACTION_MOVE:
                //如果开始录音 才进行坐标判断
                if (mIsVoice) {
                    //根据手指的区域判断是否要取消录音
                    if (isCancel(x, y)) {
                        changeState(CANCEL_VOICE);
                    } else {
                        changeState(CUR_VOICE);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //弹起手指时判断上一个状态进行操作
                if (mCurState == CANCEL_VOICE) {
                    //取消录音时弹起 才进行取消操作
                } else if (mCurState == CUR_VOICE) {
                    //正在录音 弹起时发送语音
                }
                reset();
                break;
        }
        return super.onTouchEvent(event);
 
    }
 
    //恢复标志位为初始属性
    private void reset() {
        changeState(NOT_VOICE);
        mIsVoice = false;
    }
 
    private boolean isCancel(float x, float y) {
        //判断手的位置 是否移动到控件上/下方50的位置 进行取消(微信好像只判断移动到按钮上方)
        return y < getHeight() - 50 || y > getHeight() + 50;

    }
 
    //改变按钮显示
    private void changeState(int curVoice) {
        //当前状态不等于传入的状态才需要改变按钮的
        if (mCurState != curVoice) {
            mCurState = curVoice;
            switch (curVoice) {
                case CUR_VOICE:
                    //此处除了改变文本 还可以改变背景什么的   具体看需求 此处从简
                    //此处模拟正在录音 用于控制显示
                    mIsVoice = true;
                    break;
                case NOT_VOICE:

                    break;
                case CANCEL_VOICE:

                    break;
 
            }
 
        }
    }
}