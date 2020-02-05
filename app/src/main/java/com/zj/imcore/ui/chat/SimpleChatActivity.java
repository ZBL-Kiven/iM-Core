//package com.zj.imcore.ui.chat;
//
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.view.*;
//import android.widget.*;
//import androidx.appcompat.app.AppCompatActivity;
//import com.zj.ui.emotionboard.CusEmoticonsLayout;
//import com.zj.ui.emotionboard.adpater.EmoticonPacksAdapter;
//import com.zj.ui.emotionboard.data.Emoticon;
//import com.zj.ui.emotionboard.interfaces.OnEmoticonClickListener;
//import com.zj.ui.emotionboard.widget.EmoticonsEditText;
//import com.zj.ui.emotionboard.widget.FuncLayout;
//import com.zj.imcore.R;
//
//
//abstract class BaseChatActivity extends AppCompatActivity implements FuncLayout.FuncKeyBoardListener {
//
//    CusEmoticonsLayout ekBar;
//
//    private EmoticonPacksAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(getContentView());
//        initView();
//        init();
//    }
//
//    abstract int getContentView();
//
//    abstract void init();
//
//    private void initView() {
//        initEmoticonsKeyBoardBar();
//        initListView();
//    }
//
//    protected void initEmoticonsKeyBoardBar() {
//        SimpleCommonUtils.initEmoticonsEditText(ekBar.getEtChat());
//
//        adapter = AdapterUtils.INSTANCE.getCommonAdapter(this, onEmoticonClickListener);
//
//        ekBar.setAdapter(adapter);
//        ekBar.addOnFuncKeyBoardListener(this);
//        ekBar.addFuncView(new SimpleAppsGridView(this));
//
//        ekBar.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
//            @Override
//            public void onSizeChanged(int w, int h, int oldw, int oldh) {
//                scrollToBottom();
//            }
//        });
//        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendBtnClick(ekBar.getEtChat().getText().toString());
//                ekBar.getEtChat().setText("");
//            }
//        });
//
//
//        int width = (int) getResources().getDimension(com.zj.ui.emotionboard.R.dimen.bar_tool_btn_width);
//
//        View leftView = LayoutInflater.from(this).inflate(com.zj.ui.emotionboard.R.layout.left_toolbtn, null);
//        ImageView iv_icon = (ImageView) leftView.findViewById(com.zj.ui.emotionboard.R.id.iv_icon);
//        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(width, RelativeLayout.LayoutParams.MATCH_PARENT);
//        iv_icon.setLayoutParams(imgParams);
//        iv_icon.setImageResource(R.mipmap.icon_add);
//        leftView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SimpleChatActivity.this, "ADD", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        ekBar.getEmoticonsToolBarView().addFixedToolItemView(leftView, false);
//
//        View rightView = LayoutInflater.from(this).inflate(com.zj.ui.emotionboard.R.layout.right_toolbtn, null);
//        iv_icon = (ImageView) rightView.findViewById(com.zj.ui.emotionboard.R.id.iv_icon);
//        iv_icon.setImageResource(R.mipmap.icon_setting);
//        iv_icon.setLayoutParams(imgParams);
//        rightView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SimpleChatActivity.this, "SETTING", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        ekBar.getEmoticonsToolBarView().addFixedToolItemView(rightView, true);
//    }
//
//    OnEmoticonClickListener<Emoticon> onEmoticonClickListener = new OnEmoticonClickListener<Emoticon>() {
//        @Override
//        public void onEmoticonClick(Emoticon emoticon) {
//
//            if (emoticon == null) {
//                return;
//            }
//
//            if (emoticon instanceof DeleteEmoticon) {
//                SimpleCommonUtils.delClick(ekBar.getEtChat());
//            } else if (emoticon instanceof PlaceHoldEmoticon) {
//                // do nothing
//            } else if (emoticon instanceof BigEmoticon) {
//                sendImage(emoticon.getUri());
//            } else {
//                String content = emoticon.getCode();
//                if (TextUtils.isEmpty(content)) {
//                    return;
//                }
//
//                int index = ekBar.getEtChat().getSelectionStart();
//                Editable editable = ekBar.getEtChat().getText();
//                editable.insert(index, content);
//            }
//        }
//    };
//
//
//
//
//    protected void sendBtnClick(String msg) {
//        if (!TextUtils.isEmpty(msg)) {
//            ImMsgBean bean = new ImMsgBean();
//            bean.setMsgType(ImMsgBean.CHAT_MSGTYPE_TEXT);
//            bean.setContent(msg);
//            sendMsg(bean);
//        }
//    }
//
//    private void sendMsg(ImMsgBean bean) {
//        chattingListAdapter.addData(bean, true, false);
//        scrollToBottom();
//    }
//
//    private void sendImage(String image) {
//        if (!TextUtils.isEmpty(image)) {
//            ImMsgBean bean = new ImMsgBean();
//            bean.setMsgType(ImMsgBean.CHAT_MSGTYPE_IMG);
//            bean.setImage(image);
//            sendMsg(bean);
//        }
//    }
//
//    protected void scrollToBottom() {
//        lvChat.requestLayout();
//        lvChat.post(new Runnable() {
//            @Override
//            public void run() {
//                lvChat.setSelection(lvChat.getBottom());
//            }
//        });
//    }
//
//    @Override
//    public void onFuncPop(int height) {
//        scrollToBottom();
//    }
//
//    @Override
//    public void onFuncClose() {
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        ekBar.reset();
//    }
//}
