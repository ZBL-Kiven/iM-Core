package com.zbl.processor.generate.mod;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static com.zbl.processor.handler.Constance.newProxyInstance;


public class ProxyInfo {

    private String className = "";
    private String packageName = "";
    private BaseProxyInfo proxyCls;
    private TypeElement typeElement;

    public ProxyInfo(Elements elementUtils, Element element, Class<? extends Annotation> cls) {
        Annotation annotation = element.getAnnotation(cls);
        Class<? extends Annotation> annotationCls = annotation.annotationType();
        RetentionPolicy clsType = annotationCls.getAnnotation(Retention.class).value();
        switch (clsType) {
            case SOURCE:
            case RUNTIME:
                throw new IllegalStateException("Processor supported type can't improve an either of retention policy type except type of \'CLASS\'");
        }
        ElementType[] targetTypes = annotationCls.getAnnotation(Target.class).value();
        ElementType elementType = null;
        switch (element.getKind()) {
            case ENUM:
            case CLASS:
            case INTERFACE:
                elementType = ElementType.TYPE;
                break;

            case FIELD:
                elementType = ElementType.FIELD;
                break;

            case METHOD:
                elementType = ElementType.METHOD;
                break;

            case CONSTRUCTOR:
                elementType = ElementType.CONSTRUCTOR;
                break;

            case PARAMETER:
                elementType = ElementType.PARAMETER;
                break;
        }
        try {
            if (contains(targetTypes, elementType)) {
                if (elementType == ElementType.TYPE) {
                    typeElement = (TypeElement) element;
                } else {
                    typeElement = getType(element);
                }
                PackageElement packageElement = elementUtils.getPackageOf(typeElement);
                packageName = packageElement.getQualifiedName().toString();
                Class<? extends BaseProxyInfo> pc = newProxyInstance(cls);
                if (pc != null) proxyCls = pc.newInstance();
                Objects.requireNonNull(proxyCls);
                int packageLen = packageName.length() + 1;
                Objects.requireNonNull(typeElement);
                className = typeElement.getQualifiedName().toString().substring(packageLen).replace(".", "");
                proxyCls.init(getProxyClassName(), packageName, getProxyClassFullName(), typeElement, element, elementUtils);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProxyClassFullName() {
        return packageName + "." + getProxyClassName();
    }

    private String getProxyClassName() {
        return className + proxyCls.getSeparator() + proxyCls.getProxyName();
    }

    public String generateCode() {
        return proxyCls.generateCode();
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public void clear() {
        this.proxyCls.onClear();
    }

    private boolean contains(ElementType[] val, ElementType t) {
        if (t == null) return false;
        for (ElementType e : val) {
            if (e.equals(t)) return true;
        }
        return false;
    }

    private TypeElement getType(Element element) {
        try {
            Element e = element.getEnclosingElement();
            if (e instanceof TypeElement) {
                return (TypeElement) e;
            } else return getType(e);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
