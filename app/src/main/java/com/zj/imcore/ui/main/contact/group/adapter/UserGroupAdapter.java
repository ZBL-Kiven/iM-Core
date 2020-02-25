package com.zj.imcore.ui.main.contact.group.adapter;


import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.zj.imcore.R;
import com.zj.imcore.model.GroupInfo;
import com.zj.imcore.yj.base.adapter.BaseSimpleAdapter;
import com.zj.imcore.yj.base.adapter.ViewHolder;
import com.zj.model.chat.TeamMembers;

/**
 * @author yangji
 */
public class UserGroupAdapter extends BaseSimpleAdapter<TeamMembers> {

    @Override
    public TeamMembers getItem(int position) {
        return isAddItem(position) ? new TeamMembers() : super.getItem(position);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView ivUserAvatar = holder.getView(R.id.app_act_contact_group_info_item_iv_user_avatar);
        TextView tvUserName = holder.getView(R.id.app_act_contact_group_info_item_tv_user_nickname);

        if (isAddItem(position)) {
            tvUserName.setText("");
            ivUserAvatar.setImageResource(R.drawable.app_act_contact_group_user_add);
        } else {

            Glide.with(ivUserAvatar)
                    .load(getItem(position).getAvatar())
                    .placeholder(R.mipmap.app_contact_avatar_default)
                    .into(ivUserAvatar);

            tvUserName.setText(getItem(position).getName());
        }
    }
}
