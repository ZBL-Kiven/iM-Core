package com.cf.im.db.domain.impl;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.cf.im.db.domain.MemberBean;
import com.cf.im.db.domain.MessageBean;
import com.zj.model.interfaces.MessageIn;

import java.util.Objects;

public class _MessageBeanImpl implements MessageIn {

    @Embedded
    public MessageBean message;

    @Relation(parentColumn = "uid", entityColumn = "uid", entity = MemberBean.class)
    public _MessageMemberBean member;

    //~~~~~~~~~ 实现 UI 接口 ~~~~~~~~~

    @Override
    public String dialogId() {
        return message.dialogId;
    }

    @Override
    public String subType() {
        return message != null ? String.valueOf(message.subtype) : "";
    }

    @Override
    public String subTypeDetail() {
        return message != null ? String.valueOf(message.subTypeDetail) : "";
    }

    @Override
    public String text() {
        return message != null ? String.valueOf(message.text) : "";
    }

    @Override
    public long createdTs() {
        return message != null ? message.localCreateTs : System.currentTimeMillis();
    }

    @Override
    public String uid() {
        return message.uid;
    }

    @Override
    @NonNull
    public String referKey() {
        return "";
    }

    @Override
    public String starId() {
        return "";
    }

    @Override
    public boolean deleted() {
        return message != null && message.deleted;
    }

    @Override
    public long localCreatedTs() {
        return message != null ? message.localCreateTs : System.currentTimeMillis();
    }

    @Override
    public String callId() {
        return message != null ? message.callId : "";
    }

    @Override
    public long key() {
        return message != null ? message.id : -1;
    }

    @Override
    public String getAvatarUrl() {
        return member != null ? member.avatar : "";
    }

    @Override
    public String getName() {
        return member != null ? member.name : "";
    }

    @Override
    public String getStickerUrl() {
        return null;
    }

    @Override
    public int getStickerWidth() {
        return 0;
    }

    @Override
    public int getStickerHeight() {
        return 0;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public int getImageWidth() {
        return 0;
    }

    @Override
    public int getImageHeight() {
        return 0;
    }

    @Override
    public String getVoiceUrl() {
        return null;
    }

    @Override
    public long getVoiceDuration() {
        return 0;
    }

    @Override
    public String getFileUrl() {
        return null;
    }

    @Override
    public long getFileSize() {
        return 0;
    }

    @Override
    public String getVideoUrl() {
        return null;
    }

    @Override
    public String getVideoThumb() {
        return null;
    }

    @Override
    public int getVideoThumbWidth() {
        return 0;
    }

    @Override
    public int getVideoThumbHeight() {
        return 0;
    }

    @Override
    public long getVideoDuration() {
        return 0;
    }

    @Override
    public int sendingState() {
        return message.sendMsgState;
    }

    @Override
    public String localFilePath() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        _MessageBeanImpl that = (_MessageBeanImpl) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(member, that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, member);
    }
}