package com.zbl.processor.handler.processor;

import com.zbl.processor.generate.mod.BaseProxyInfo;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

public class SPProcessor extends BaseProxyInfo {
    @Override
    public String getProxyName() {
        return "Proxy";
    }

    @Override
    public String getSeparator() {
        return "_";
    }

    @Override
    public String generateCode() {
        return "package " + packageName + ";\n" + buildImportCode() + "\n " + getClassCode();
    }

    private String getClassCode() {
        return "public final class " + className + "{\n" +
                getBody()
                + "\n}";
    }

    private String getBody() {
        StringBuilder sb = new StringBuilder();
        List<? extends Element> lst = elementUtils.getAllMembers(typeElement);
        for (Element e : lst) {
            if (e.getKind().equals(ElementKind.FIELD)) {
                if (e instanceof VariableElement) {
                    String propertyName = e.getSimpleName().toString();
                    String modifiers = buildModifiers(e, propertyName);
                    String propertyType = e.asType().toString();
                    generateGetterAndSetter(modifiers, propertyName, propertyType, false, sb.append("\t\n"));
                }
            }
        }
        generateTools(sb.append("\t\n"));
        return sb.toString();
    }

    private String buildModifiers(Element e, String propertyName) {
        StringBuilder sb1 = new StringBuilder();
        ArrayList<Modifier> modifierLst = new ArrayList<>(e.getModifiers());
        QuickSort.sort(modifierLst);
        boolean hasStaticModifier = false;
        boolean isPrivate = false;
        for (Modifier mod : modifierLst) {
            if (mod.name().equalsIgnoreCase("static")) hasStaticModifier = true;
            if (mod.name().equalsIgnoreCase("private")) isPrivate = true;
            sb1.append(mod.name().toLowerCase()).append(" ");
        }
        if (!hasStaticModifier)
            throw new IllegalStateException("Shared preference field must use static modifier");
        if (isPrivate)
            throw new IllegalStateException("the private content \'" + propertyName + "\' cannot be access in future, it could useless on building");
        return sb1.toString();
    }

    private String generateGetterAndSetter(String modifiers, CharSequence name, String type, boolean isSetter, StringBuilder sb) {
        CharSequence sequence = name.subSequence(1, name.length());
        String actualName = (name.charAt(0) + "").toUpperCase() + sequence;
        String returnType = isSetter ? "void" : type;
        String methodName = isSetter ? "set" + actualName : "get" + actualName;
        sb.append("\t");
        sb.append(modifiers).append(" ");
        sb.append(returnType).append(" ");
        sb.append(methodName).append("(");
        if (isSetter) {
            sb.append(type).append(" ").append(name);
        } else {
            sb.append(type).append(" defaultValue");
        }
        sb.append("){ \n");
        sb.append("\t\t");
        if (isSetter) {
            String s = "SPHelper.put(" + buildKey(name) + ", " + name + ");";
            sb.append(s);
        } else {
            String s = "return SPHelper.get(" + buildKey(name) + ", defaultValue);";
            sb.append(s);
        }
        sb.append("\n\t}\n");
        if (!isSetter) return generateGetterAndSetter(modifiers, name, type, true, sb.append("\n"));
        return sb.toString();
    }

    private void generateTools(StringBuilder sb) {
        sb.append("\tpublic static void clear(){\n");
        sb.append("\t\tSPHelper.clear();");
        sb.append("\n\t}\n");
        sb.append("\tpublic static void init(String name, Context context){\n");
        sb.append("\t\tSPHelper.init(name, context);");
        sb.append("\n\t}\n");
    }

    private String buildKey(CharSequence name) {
        return "\"" + proxyFullName.replace(".", "-") + "&" + name + "\"";
    }

    private String buildImportCode() {
        return "import android.content.Context;";
    }
}
