package com.cf.im.db.funcations;


public interface Fun {

    interface Fun1<T1, R> {
        R apply(T1 t1) throws Exception;
    }

    interface Fun2<T1, T2, R> {
        R apply(T1 t1, T2 t2) throws Exception;
    }

    interface Fun3<T1, T2, T3, R> {
        R apply(T1 t1, T2 t2, T3 t3) throws Exception;
    }

    interface Fun4<T1, T2, T3, T4, R> {
        R apply(T1 t1, T2 t2, T3 t3, T4 t4) throws Exception;
    }
}

