package com.zbl.processor.handler.processor;

import com.zbl.processor.generate.mod.BaseProxyInfo;

public class BaseProcessor extends BaseProxyInfo {
    @Override
    public String getProxyName() {
        return "BS";
    }


    @Override
    public String getSeparator() {
        return "_";
    }

    @Override
    public String generateCode() {
        return "public class " + className + "{\n" +

                "}\n";
    }
}
