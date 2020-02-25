package com.cf.im.db.domain;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index("id"), @Index("localCreateTs"), @Index("callId")})
public class MessageBean {

    @PrimaryKey(autoGenerate = true)
    public long kId;

    public long id;//服务器主键

    public String callId;//本地消息

    public boolean deleted; // false

    public String content; // null

    public String updated; // "2020-01-03T09:38:11Z"

    public String uid; // 4

    public String created; // "2020-01-03T09:38:11Z"

    public int teamId; // 1

    public String dialogId; // 8589934596

    public String subtype; // "normal"

    public String subTypeDetail; // "normal"

    public String text; // "1"

    public int sendMsgState;

    public long localCreateTs;

}
