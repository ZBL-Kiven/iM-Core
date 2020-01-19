//package com.yj;
//
//import com.cf.im.db.domain.MemberBean;
//import com.cf.im.db.listener.DBListener;
//import com.cf.im.db.repositorys.MemberRepository;
//
//import java.lang.ref.ReferenceQueue;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class MemberManager {
//
//    private Map<Integer, MemberReference> members;
//    private ReferenceQueue<Object> queue;
//
//    public MemberManager() {
//        members = new ConcurrentHashMap<>();
//        queue = new ReferenceQueue<>();
//    }
//
//    public void getMember(int uid, DBListener<MemberBean> listener) {
//        clear();
//        MemberReference reference = members.get(uid);
//        MemberBean bean;
//        if (reference != null && (bean = reference.get()) != null) {
//            listener.onSuccess(bean);
//        } else {
//            getMemberByDb(uid, listener);
//        }
//    }
//
//    public void putMember(MemberBean bean) {
//        clear();
//        members.put(bean.uid, new MemberReference(bean, queue));
//    }
//
//    private void clear() {
//        MemberReference reference = (MemberReference) queue.poll();
//        if (reference != null) {
//            int uid = reference.getUid();
//            members.remove(uid);
//        }
//    }
//
//    public void gc() {
//        System.gc();
//    }
//
//    private void getMemberByDb(int uid, DBListener<MemberBean> listener) {
//        MemberRepository.queryMembersByUserId(uid, bean -> {
//            putMember(bean);
//            listener.onSuccess(bean);
//        });
//    }
//}
