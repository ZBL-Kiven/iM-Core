package com.zj.imcore.ui.main.contact.group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zj.base.view.BaseTitleView;
import com.zj.imcore.R;
import com.zj.imcore.apis.group.GroupApi;
import com.zj.imcore.base.FCActivity;
import com.zj.imcore.base.FCApplication;
import com.zj.imcore.ui.main.contact.UserEventMenuDialog;
import com.zj.imcore.ui.main.contact.group.adapter.UserGroupAdapter;
import com.zj.imcore.ui.users.EditTextActivity;
import com.zj.imcore.ui.users.PhotoMenuFragment;
import com.zj.list.listeners.ItemClickListener;
import com.zj.loading.BaseLoadingView;
import com.zj.model.chat.TeamMembers;
import com.zj.model.interfaces.DialogIn;

import java.util.List;

/**
 * 讨论组详情
 *
 * @author yangji
 */
public class GroupInfoActivity extends FCActivity {

    private ImageView ivUserAvatar;
    private TextView tvName;
    private TextView tvTheme;
    private TextView tvUserCount;
    private TextView tvExit;
    private TextView tvDissolve;
    private TextView tvSendMsg;
    private RecyclerView rvUsers;
    private BaseTitleView titleView;

    //
    private BaseLoadingView loadingView;

    //
    private UserGroupAdapter adapter;

    private DialogIn mDialogBean;

    public static void startActivity(Context context, String dialogId) {
        context.startActivity(new Intent(context, GroupInfoActivity.class)
                .putExtra("dialogId", dialogId));
    }

    @Override
    protected int getContentId() {
        return R.layout.app_act_contact_group_info;
    }

    @Override
    public void initView() {
        this.titleView = findViewById(R.id.app_act_contact_group_info_titleView);
        this.ivUserAvatar = findViewById(R.id.app_act_contact_group_info_iv_user_avatar);
        this.tvName = findViewById(R.id.app_act_contact_group_info_tv_name);
        this.tvTheme = findViewById(R.id.app_act_contact_group_info_tv_theme);
        this.tvUserCount = findViewById(R.id.app_act_contact_group_info_tv_user_count);
        this.rvUsers = findViewById(R.id.app_act_contact_group_info_rv_users);
        this.tvExit = findViewById(R.id.app_act_contact_group_info_tv_exit);
        this.tvDissolve = findViewById(R.id.app_act_contact_group_info_tv_dissolve);
        this.tvSendMsg = findViewById(R.id.app_act_contact_group_info_tv_send_msg);
        //
        this.loadingView = findViewById(R.id.app_act_contact_group_info_blv);
    }

    @Override
    public void initData() {
        adapter = new UserGroupAdapter();

        String dialogId = getIntent().getStringExtra("dialogId");

        queryDialogByDialogId(dialogId);
    }

    @Override
    public void initListener() {
        titleView.setLeftClickListener(v -> {
            onBackPressed();
        });

        ivUserAvatar.setOnClickListener(v -> {
            selectUserAvatar();
        });

        // 编辑 讨论组名称
        tvName.setOnClickListener(v -> {
            EditTextActivity.startActivity(this,
                    mDialogBean.dialogId(),
                    getString(R.string.app_act_contact_group_info_name_hint),
                    tvName.getText().toString(),
                    100,
                    EditTextActivity.TYPE_GROUP_NAME);
        });

        // 编辑 讨论组主题
        tvTheme.setOnClickListener(v -> {
            EditTextActivity.startActivity(this,
                    mDialogBean.dialogId(),
                    getString(R.string.app_act_contact_group_info_theme_hint),
                    tvTheme.getText().toString(),
                    100,
                    EditTextActivity.TYPE_GROUP_THEME);
        });

        //点击 用户处理
        adapter.setOnItemClickListener(new ItemClickListener<TeamMembers>() {
            @Override
            public void onItemClick(int position, View v, @Nullable TeamMembers bean) {
                if (adapter.isAddItem(position)) {
                    //跳转到 邀请 新成员界面
                    CreateGroupActivity.Companion.start(GroupInfoActivity.this, 1, 29, null);
                } else {
                    //处理当前用户界面
                    execClickUser(bean);
                }
            }
        });

        tvExit.setOnClickListener(v -> {
            commitExitGroup();
        });

        tvDissolve.setOnClickListener(v -> {
            commitDissolve();
        });

        tvSendMsg.setOnClickListener(v -> {
            FCApplication.Companion.showToast("发送消息？");
        });

    }

    /**
     * 加载 讨论组用户
     */
    private void loadUsers() {
        adapter.add(mDialogBean.getTeamMembers(null));
        rvUsers.setAdapter(adapter);
        tvUserCount.setText(getString(R.string.app_act_contact_group_info_user_count, adapter.getCount()));
    }

    /**
     * 头像选择
     */
    private void selectUserAvatar() {
        ivUserAvatar.setOnClickListener(v -> {
            new PhotoMenuFragment()
                    .setListener(file -> {
                        Glide.with(ivUserAvatar).load(file).into(ivUserAvatar);
                    })
                    .show(getSupportFragmentManager(), "PhotoMenuFragment");
        });
    }

    private void execClickUser(TeamMembers bean) {
        // 判断 当前用户是否是管理员
        // UserInfoActivity.Companion.start(this, 1L, false);
        UserEventMenuDialog.createDialog(mDialogBean.dialogId(), bean.getTmid())
                .setRemoveMemberListener((dialogId, tmId) -> {
                    List<TeamMembers> list = adapter.getData();
                    // int index = list.indexOf(bean);
                    list.remove(bean);
                    adapter.notifyDataSetChanged();
                })
                .show(getSupportFragmentManager(), "userEventMenuDialog");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            return;
        }

        switch (requestCode) {
            case EditTextActivity.TYPE_GROUP_NAME:
                tvName.setText(EditTextActivity.getIntentContent(data));
                break;
            case EditTextActivity.TYPE_GROUP_THEME:
                tvTheme.setText(EditTextActivity.getIntentContent(data));
                break;
            default:
        }
    }


    /**
     * 提交解散讨论组
     */
    private void commitDissolve() {
        loadingView.setMode(BaseLoadingView.DisplayMode.LOADING, getString(R.string.app_act_contact_group_info_dissolve_hint), true);
    }

    /**
     * 提交退出讨论组
     */
    private void commitExitGroup() {
        loadingView.setMode(BaseLoadingView.DisplayMode.LOADING, getString(R.string.app_act_contact_group_info_exit_hint), true);
        GroupApi.INSTANCE.editUserToDialog(mDialogBean.dialogId(), "111", (success, content, err) -> {
            if (success) {
                FCApplication.Companion.showToast("退出成功");
                finish();
            } else {
                FCApplication.Companion.showToast("退出失败");
            }
            loadingView.setMode(BaseLoadingView.DisplayMode.NONE);
            return null;
        });
    }

    private void queryDialogByDialogId(String dialogId) {
        GroupApi.INSTANCE.queryDialog(dialogId, (aBoolean, dialogBean, e) -> {
            loadDialogBean(dialogBean);
            return null;
        });
    }

    private void loadDialogBean(DialogIn dialogBean) {
        if (dialogBean == null) {
            FCApplication.Companion.showToast("未查询到Dialog");
            finish();
            return;
        }
        this.mDialogBean = dialogBean;

        titleView.setTitle(mDialogBean.name());

        Glide.with(ivUserAvatar).load(mDialogBean.avatar()).into(ivUserAvatar);

        tvName.setText(mDialogBean.name());
        tvTheme.setText(mDialogBean.topic());
        loadUsers();
    }

}
