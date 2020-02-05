package com.zj.imcore.ui.users;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.zj.imcore.R;
import com.zj.imcore.base.FCActivity;

public class EditTextActivity extends FCActivity {

    public static void startActivity(Activity activity, String title, String content, int maxContent, int commitState) {
        activity.startActivity(new Intent(activity, EditTextActivity.class)
                .putExtra("title", title)
                .putExtra("content", content)
                .putExtra("size", maxContent)
                .putExtra("state", commitState)
        );
    }

    private EditText etEditData;

    @Override
    protected int getContentId() {
        return R.layout.app_act_edit_text;
    }

    @Override
    public void initView() {
        showTitleBar(true);
        etEditData = findViewById(R.id.etEditData);
    }

    @Override
    public void initData() {
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        int size = getIntent().getIntExtra("size", 0);
        int state = getIntent().getIntExtra("state", 0);

        getBaseTitleView().setLeftIcon(R.mipmap.back);
        getBaseTitleView().setTitle(title);
        getBaseTitleView().setRightTxt(getString(R.string.app_act_user_edit_text_commit));

        etEditData.setText(content);
    }

    @Override
    public void initListener() {
        etEditData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getBaseTitleView().setLeftClickListener(v -> {
            onBackPressed();
        });

        getBaseTitleView().setRightClickListener(v -> {
            //提交信息
            commit();
        });
    }

    private void commit() {

    }
}
