package com.cf.im.db.domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index("userId"), @Index("latestTs")})
public class DialogBean {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    public String channelID;//: ; get() = impl.getId()

    public String title;//: ; get() = impl.getTitle()

    public String subDetail;//: ; get() = impl.getSubDetail()

    //the conversation last updated Ts
    public long latestTs;// get() = impl.getLatestTs()

    public long selfReadTs;// get() = impl.getSelfReadTs()

    public int unReadCount;//: ; get() = impl.getUnReadCount()

    //last time someone read the conversation
    public long otherReadTs;// get() = impl.getOtherReadTs()

    //user id of the peer during p2p conversation
    public String userId;//: ?; get() = impl.getUserId()

    //are there favorite messages in the conversation
    public boolean hasStar;// get() = impl.hasStar()

    //对话的草稿
    public String draft;//: ?; get() = impl.getDraft()

    //对话是否显示
    public boolean isShown;// get() = impl.isShown()

    public long sortTs;// get() = impl.sortTs()

    public boolean notification;// get() = impl.notification()

    public long hideTs;// get() = impl.hideTs()

    public String thumbUrl;//: ; get() = impl.getThumbUrl()

    public boolean isPin;// get() = impl.isPin()

    public boolean isMute;// get() = impl.isMute()

    public boolean isDelete;// get() = impl.isDelete()
}
