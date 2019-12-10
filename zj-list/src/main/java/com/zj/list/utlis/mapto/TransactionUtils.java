package com.zj.list.utlis.mapto;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TransactionUtils {

    public static <T, R> List<R> map(List<T> lst, MapTranslators<T, R> translators) {
        List<R> op = new ArrayList<>();
        for (T t : lst) {
            op.add(translators.translate(t));
        }
        return op;
    }

    public static <T, R> void mapTo(List<T> lst, List<R> op, MapTranslators<T, R> translators) {
        for (T t : lst) {
            op.add(translators.translate(t));
        }
    }
}
