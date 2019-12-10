package com.zj.imcore.mod;

import androidx.annotation.Nullable;
import com.zj.list.multiable.MultiAbleData;

import java.util.UUID;

public class MsgInfo implements MultiAbleData<MsgInfo> {

    private String id = UUID.randomUUID().toString();
    public String data;
    public Long createTs;
    public boolean isSelf = false;

    @Override
    public int compareTo(@Nullable MsgInfo o) {
        if (o == null) return -1;
        if (createTs > o.createTs) return 1;
        if (createTs < o.createTs) return -1;
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MsgInfo)) return false;
        return id.equals(((MsgInfo) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
