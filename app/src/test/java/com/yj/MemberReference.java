package com.yj;

import com.cf.im.db.domain.MemberBean;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class MemberReference extends WeakReference<MemberBean> {

    private final int uid;

    public MemberReference(MemberBean referent, ReferenceQueue<? super MemberBean> q) {
        super(referent, q);
        this.uid = referent.uid;
    }

    public int getUid() {
        return uid;
    }
}
