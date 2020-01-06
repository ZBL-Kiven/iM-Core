package com.zj.model.mod;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class MessageBean {
    public boolean deleted; // false,
    public String content; // null,
    public String updated; // "2020-01-03T09:38:11Z",
    public int uid; // 4,
    public String created; // "2020-01-03T09:38:11Z",
    public long id; // 991077318262784,
    public int team_id; // 1,
    public long dialog_id; // 8589934596,
    public String subtype; // "normal",
    public String text; // "1"

    /**
     * local maintenance fields
     * */
    @Expose(serialize = false)
    public String callId;
    @Expose(serialize = false)
    public int sendMsgState;
    @Expose(serialize = false)
    public long localCreateTs;
}
