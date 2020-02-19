package com.zj.imcore.ui.users;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.IntDef;

import com.cf.im.db.domain.MemberBean;
import com.cf.im.db.repositorys.MemberRepository;
import com.zj.base.utils.storage.sp.SPUtils_Proxy;
import com.zj.base.view.BaseTitleView;
import com.zj.imcore.R;
import com.zj.imcore.apis.user.UserApi;
import com.zj.imcore.base.FCActivity;
import com.zj.imcore.utils.KeyboardUtils;
import com.zj.loading.BaseLoadingView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

public class EditTextActivity extends FCActivity {


    @IntDef(value = {
            TYPE_USER_NAME,
            TYPE_USER_NICK_NAME,
            TYPE_USER_DESCRIBE
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Type {
    }

    public static final int TYPE_USER_NAME = 0x100;
    public static final int TYPE_USER_NICK_NAME = 0x101;
    public static final int TYPE_USER_DESCRIBE = 0x102;

    public static final String KEY_CONTENT = "content";
    private static final String KEY_SIZE = "size";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";

    private int mType;

    public static void startActivity(Activity activity, String title, String content, int maxContent, @Type int commitType) {
        Intent intent = new Intent(activity, EditTextActivity.class)
                .putExtra(KEY_TITLE, title)
                .putExtra(KEY_CONTENT, content)
                .putExtra(KEY_SIZE, maxContent)
                .putExtra(KEY_TYPE, commitType);
        activity.startActivityForResult(intent, commitType);
    }

    private boolean commitIng = false;
    private EditText etEditData;
    private BaseTitleView baseTitleView;
    private BaseLoadingView baseLoadingView;

    @Override
    protected int getContentId() {
        return R.layout.app_act_edit_text;
    }

    @Override
    public void initView() {
        etEditData = find(R.id.app_act_edit_text_et_data);
        baseTitleView = find(R.id.app_act_edit_text_title);
        baseLoadingView = find(R.id.app_act_edit_text_blv);
    }

    @Override
    public void initData() {
        String title = getIntent().getStringExtra(KEY_TITLE);
        String content = getIntent().getStringExtra(KEY_CONTENT);
        int size = getIntent().getIntExtra(KEY_SIZE, 0);
        this.mType = getIntent().getIntExtra(KEY_TYPE, 0);
        baseTitleView.setTitle(title);
//        baseTitleView.setRightIcon(R.mipmap.commit);
        baseTitleView.setRightVisibility(true);
        baseTitleView.setRightTextColor(R.color.pg_color_white);
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

        baseTitleView.setLeftClickListener(v -> onBackPressed());

        baseTitleView.setRightClickListener(v -> {
            //提交信息
            commit();
        });
    }

    private void commit() {
        commitIng = true;
        KeyboardUtils.closeKeyBoard(this);
        baseLoadingView.setMode(BaseLoadingView.DisplayMode.LOADING, getString(R.string.app_act_user_edit_text_updating), true);
        execMember();
    }

    private void execMember() {
        //获取用户信息
        long userId = SPUtils_Proxy.getUserId(0L);
        MemberRepository.queryMembersByUserId(userId, memberBean1 -> {
            Map<String, Object> request = new HashMap<>();
            Map<String, String> profile = memberBean1.getProfile();
            request.put("profile", profile);

            String content = etEditData.getText().toString();

            switch (mType) {
                case TYPE_USER_NICK_NAME:
                    profile.put("nickname", content);
                    break;
                case TYPE_USER_DESCRIBE:
                    profile.put("describe", content);
                    break;
                default:
            }

            commitUserInfo(request, memberBean1);
        });
    }

    private void commitUserInfo(Map<String, Object> request, MemberBean memberBean1) {
        UserApi.INSTANCE.updateUser(request, (isSuccess, memberBean, e) -> {
            if (isSuccess) {
                if (mType == TYPE_USER_DESCRIBE) {
                    memberBean1.describe = memberBean.describe;
                }
                if (mType == TYPE_USER_NICK_NAME) {
                    memberBean1.nickname = memberBean.nickname;
                }
                MemberRepository.insertOrUpdate(memberBean1, memberBean2 -> runOnUiThread(() -> {
                    Intent intent = new Intent();
                    if (mType == TYPE_USER_DESCRIBE) {
                        intent.putExtra(KEY_CONTENT, memberBean2.describe);
                    }
                    if (mType == TYPE_USER_NICK_NAME) {
                        intent.putExtra(KEY_CONTENT, memberBean2.nickname);
                    }
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }));
            } else {
                baseLoadingView.setMode(BaseLoadingView.DisplayMode.NONE, true);
                Toast.makeText(this, R.string.app_common_network_error, Toast.LENGTH_SHORT).show();
            }
            commitIng = false;
            return null;
        });

    }

    @Override
    public void onBackPressed() {
        if (commitIng) {
            return;
        }
        super.onBackPressed();
    }
}
