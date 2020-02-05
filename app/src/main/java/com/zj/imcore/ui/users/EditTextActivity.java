package com.zj.imcore.ui.users;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.cf.im.db.domain.MemberBean;
import com.cf.im.db.listener.DBListener;
import com.cf.im.db.repositorys.MemberRepository;
import com.zj.base.utils.storage.sp.SPUtils_Proxy;
import com.zj.imcore.R;
import com.zj.imcore.apis.user.UserApi;
import com.zj.imcore.apis.user.UserApiService;
import com.zj.imcore.base.FCActivity;
import com.zj.imcore.utils.KeyboardUtils;
import com.zj.loading.BaseLoadingView;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import retrofit2.HttpException;

public class EditTextActivity extends FCActivity {

    public static final int TYPE_USER_NAME = 0x100;
    public static final int TYPE_USER_NIKE_NAME = 0x101;
    public static final int TYPE_USER_DESCRIBE = 0x102;

    public static final String KEY_CONTENT = "content";
    private static final String KEY_SIZE = "size";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";

    public static void startActivity(Activity activity, String title, String content, int maxContent, int commitType) {
        Intent intent = new Intent(activity, EditTextActivity.class)
                .putExtra(KEY_TITLE, title)
                .putExtra(KEY_CONTENT, content)
                .putExtra(KEY_SIZE, maxContent)
                .putExtra(KEY_TYPE, commitType);
        activity.startActivityForResult(intent, commitType);
    }

    private boolean commitIng = false;
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
        String title = getIntent().getStringExtra(KEY_TITLE);
        String content = getIntent().getStringExtra(KEY_CONTENT);
        int size = getIntent().getIntExtra(KEY_SIZE, 0);
        int type = getIntent().getIntExtra(KEY_TYPE, 0);

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
        commitIng = true;
        KeyboardUtils.closeKeyBoard(this);
        getBlvLoading().setMode(BaseLoadingView.DisplayMode.LOADING, "更新中");
        commitUserInfo();
    }

    private void commitUserInfo() {
        String content = etEditData.getText().toString();
        Map<String, Object> request = new HashMap<>();
        Map<String, String> profile = new HashMap<>();
        profile.put("describe", content);
        request.put("profile", profile);

        UserApi.INSTANCE.updateUser(request, (isSuccess, memberBean, e) -> {
            if (isSuccess) {
                long userId = SPUtils_Proxy.getUserId(0L);
                MemberRepository.queryMembersByUserId(userId, memberBean1 -> {
                    memberBean1.describe = memberBean.describe;
                    MemberRepository.insertOrUpdate(memberBean1, memberBean2 -> {
                        runOnUiThread(() -> {
                            Intent intent = new Intent();
                            intent.putExtra(KEY_CONTENT, memberBean2.describe);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        });
                    });
                });

            } else {
                getBlvLoading().setMode(BaseLoadingView.DisplayMode.NONE);
                Toast.makeText(this, R.string.app_net_network_error, Toast.LENGTH_SHORT).show();
            }

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
