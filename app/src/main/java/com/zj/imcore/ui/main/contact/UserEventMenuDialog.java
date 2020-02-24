package com.zj.imcore.ui.main.contact;

import android.widget.Toast;

import com.zj.imcore.R;
import com.zj.imcore.apis.group.GroupApi;
import com.zj.imcore.ui.users.UserInfoActivity;
import com.zj.imcore.yj.base.ui.BaseDialog;

/**
 * @author yangji
 */
public class UserEventMenuDialog extends BaseDialog {

    public interface RemoveMemberListener {
        /**
         * 移除成员监听
         *
         * @param dialogId 讨论组编号
         * @param tmId     成员编号
         */
        void success(String dialogId, String tmId);
    }

    private RemoveMemberListener removeMemberListener;

    public UserEventMenuDialog setRemoveMemberListener(RemoveMemberListener removeMemberListener) {
        this.removeMemberListener = removeMemberListener;
        return this;
    }

    private String dialogId;

    private String tmId;

    public static UserEventMenuDialog createDialog(String dialogId, String userId) {
        UserEventMenuDialog dialog = new UserEventMenuDialog();
        dialog.setDialogId(dialogId);
        dialog.setUserId(userId);
        return dialog;
    }

    private UserEventMenuDialog() {
    }

    private void setUserId(String tmId) {
        this.tmId = tmId;
    }

    private void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.app_dialog_contact_group_info_user_menu;
    }

    @Override
    protected void initListener() {

        findViewById(R.id.app_dialog_contact_group_info_user_menu_tv_user)
                .setOnClickListener(v -> {
                    execOpenUserActivity();
                });

        findViewById(R.id.app_dialog_contact_group_info_user_menu_tv_set_admin)
                .setOnClickListener(v -> {
                    commitSetAdmin();
                });

        findViewById(R.id.app_dialog_contact_group_info_user_menu_tv_transfer_group)
                .setOnClickListener(v -> {
                    commitTransfer();
                });

        findViewById(R.id.app_dialog_contact_group_info_user_menu_tv_remove)
                .setOnClickListener(v -> {
                    commitRemoveUser();
                });

        findViewById(R.id.app_dialog_contact_group_info_user_menu_tv_un_admin)
                .setOnClickListener(v -> {
                    commitSetUnAdmin();
                });

        findViewById(R.id.app_dialog_contact_group_info_user_menu_tv_dismiss)
                .setOnClickListener(v -> {
                    dismissAllowingStateLoss();
                });
    }

    /**
     * 取消当前 用户的管理员权限
     */
    private void commitSetUnAdmin() {
        Toast.makeText(getContext(), "取消当前 用户的管理员权限？", Toast.LENGTH_SHORT).show();
    }

    /**
     * 从当前 讨论组 移除 当前选中用户
     */
    private void commitRemoveUser() {
        GroupApi.INSTANCE.removeUserToDialog(dialogId, tmId, (success, s, e) -> {
            if (success) {
                if (removeMemberListener != null) {
                    removeMemberListener.success(dialogId, tmId);
                }
                Toast.makeText(getContext(), "移除讨论组成功", Toast.LENGTH_SHORT).show();
                dismissAllowingStateLoss();
            } else {
                Toast.makeText(getContext(), "移除讨论组失败", Toast.LENGTH_SHORT).show();
            }
            return null;
        });
    }

    /**
     * 转移 讨论组
     */
    private void commitTransfer() {
        Toast.makeText(getContext(), "转移 讨论组？", Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置 当前用户 为 管理员
     */
    private void commitSetAdmin() {
        Toast.makeText(getContext(), "设置管理员？", Toast.LENGTH_SHORT).show();
    }

    private void execOpenUserActivity() {
        UserInfoActivity.Companion.start(getContext(), tmId, false);
        dismissAllowingStateLoss();
    }


    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

}
