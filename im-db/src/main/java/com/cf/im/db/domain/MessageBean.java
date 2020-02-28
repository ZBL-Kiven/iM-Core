package com.cf.im.db.domain;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.alibaba.fastjson.annotation.JSONField;

@Entity(indices = {@Index("id"), @Index("localCreateTs"), @Index("callId")})
public class MessageBean {

    @PrimaryKey(autoGenerate = true)
    public long kId;

    public String id;//服务器主键

    public String callId;//本地消息

    public boolean deleted; // false

    public String content; // null

    public String updated; // "2020-01-03T09:38:11Z"

    @JSONField(name = "tmid")
    public String tmId; // 4

    public String created; // "2020-01-03T09:38:11Z"

    @JSONField(name = "created_ts")
    public long createdTs;

    @JSONField(name = "team_id")
    public String teamId; // 1

    @JSONField(name = "dialog_id")
    public String dialogId; // 8589934596

    @JSONField(name = "refer_id")
    public String referId; // 8589934596

    public String subtype; // "normal"

    public String subTypeDetail; // "normal"

    public String text; // "1"

    public int sendMsgState;

    public long localCreateTs;

}
