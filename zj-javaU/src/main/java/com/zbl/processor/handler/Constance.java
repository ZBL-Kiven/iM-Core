package com.zbl.processor.handler;

import com.zbl.processor.generate.mod.BaseProxyInfo;
import com.zbl.processor.handler.annotate.ProxyBinding;
import com.zbl.processor.handler.annotate.SPBinding;
import com.zbl.processor.handler.processor.BaseProcessor;
import com.zbl.processor.handler.processor.SPProcessor;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

public class Constance {

    public static Class<? extends BaseProxyInfo> newProxyInstance(Class<? extends Annotation> cls) {
        if (cls.equals(ProxyBinding.class)) {
            return BaseProcessor.class;
        }
        if (cls.equals(SPBinding.class)) {
            return SPProcessor.class;
        }
        return null;
    }

    public static Set<Class<? extends Annotation>> supportClasses() {
        Set<Class<? extends Annotation>> supportedClasses = new LinkedHashSet<>();
        supportedClasses.add(SPBinding.class);
        supportedClasses.add(ProxyBinding.class);
        return supportedClasses;
    }

}
