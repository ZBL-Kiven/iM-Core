package com.cf.im.db;

import com.cf.im.db.databases.BaseSingleton;
import com.cf.im.db.domain.MemberBean;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemberService {

    public static BaseSingleton<MemberService> singleton = new BaseSingleton<MemberService>() {
        @Override
        protected MemberService create() {
            return new MemberService();
        }
    };

    static class MemberReference extends WeakReference<MemberBean> {

        private int userId;

        public MemberReference(int uid, MemberBean bean, ReferenceQueue<MemberBean> queue) {
            super(bean, queue);
            this.userId = uid;
        }

        public int getUserId() {
            return userId;
        }
    }

    private Map<Integer, MemberReference> members;
    private ReferenceQueue<MemberBean> queue;

    public MemberService() {
        members = new ConcurrentHashMap<>();
    }

    public MemberBean getMembers(int userId) {
        gc();
        MemberReference bean = members.get(userId);
        if (bean != null) {
            return bean.get();
        }
        return null;
    }

    public void addMember(MemberBean bean) {
        gc();
        int uid = bean.uid;
        members.put(uid, new MemberReference(uid, bean, queue));
    }

    public void addMembers(List<MemberBean> beans) {
        gc();

    }

    public void gc() {
        MemberReference reference = null;
        do {
            reference = (MemberReference) queue.poll();
            if (reference != null) {
                //已经回收了
                members.remove(reference.getUserId());
            }
        } while (reference != null);
    }
}
