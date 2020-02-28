//package com.yj;
//
//
//import com.cf.im.db.domain.MemberBean;
//import com.cf.im.db.listener.DBListener;
//
//import org.junit.Test;
//
//public class ListTest {
//
//    @Test
//    public void test() {
//        MemberManager manager = new MemberManager();
//        MemberBean bean = new MemberBean();
//        bean.tmId = 1;
//        manager.putMember(bean);
//        bean = null;
//        System.gc();
//        System.out.println();
//        manager.getMember(1, it -> {
//            System.out.println(it.toString());
//        });
//
//    }
//
//}
