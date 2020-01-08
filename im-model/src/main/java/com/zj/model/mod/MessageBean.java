package com.zj.model.mod;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class MessageBean {

    public boolean deleted; // false,
    public String content; // null,
    public String updated; // "2020-01-03T09:38:11Z",
    public int uid; // 4,
    public String created; // "2020-01-03T09:38:11Z",
    public long id = System.currentTimeMillis(); // 991077318262784,
    public int team_id; // 1,
    public long dialog_id; // 8589934596,
    public String subtype; // "normal",
    public String subtypeDetail; // "normal",
    public String text; // "1"
    /**
     * local maintenance fields
     */
    @SerializedName("call_id")
    public String callId;
    public int sendMsgState;
    public long localCreateTs;
}
