package com.zj.imcore.ui.main.contact.group.adapter;


import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zj.imcore.R;
import com.zj.imcore.model.GroupInfo;
import com.zj.imcore.yj.base.adapter.BaseSimpleAdapter;
import com.zj.imcore.yj.base.adapter.ViewHolder;

/**
 * @author yangji
 */
public class UserGroupAdapter extends BaseSimpleAdapter<GroupInfo.GroupMember> {

    @Override
    public GroupInfo.GroupMember getItem(int position) {
        return isAddItem(position) ? () -> "" : super.getItem(position);
    }

    @Override
    public int getItemCount() {
        return getCount() + 1;
    }

    public int getCount() {
        return getData() == null ? 0 : getData().size();
    }

    public boolean isAddItem(int position) {
        return position > getCount() - 1;
    }

    public UserGroupAdapter() {
        super(R.layout.app_act_contact_group_info_item_user);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView ivUserAvatar = holder.getView(R.id.app_act_contact_group_info_item_iv_user_avatar);
        TextView tvUserName = holder.getView(R.id.app_act_contact_group_info_item_tv_user_nickname);

        if (isAddItem(position)) {
            tvUserName.setText("");
            ivUserAvatar.setImageResource(R.drawable.app_act_contact_group_user_add);
        } else {
            ivUserAvatar.setImageDrawable(new ColorDrawable());
            tvUserName.setText(getItem(position).getTmId());
        }
    }
}
