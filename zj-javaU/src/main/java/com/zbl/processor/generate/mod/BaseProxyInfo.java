package com.zbl.processor.generate.mod;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@SuppressWarnings("WeakerAccess")
public abstract class BaseProxyInfo {

    protected TypeElement typeElement;
    protected Element element;
    protected Elements elementUtils;
    protected String className;
    protected String packageName;
    protected String proxyFullName;

    public void init(String className, String packageName, String proxyClassName, TypeElement typeElement, Element element, Elements elements) {
        this.className = className;
        this.packageName = packageName;
        this.proxyFullName = proxyClassName;
        this.typeElement = typeElement;
        this.element = element;
        this.elementUtils = elements;
    }

    public abstract String getProxyName();

    public abstract String getSeparator();

    public abstract String generateCode();

    public void onClear() {
        typeElement = null;
        element = null;
        elementUtils = null;
    }
}
