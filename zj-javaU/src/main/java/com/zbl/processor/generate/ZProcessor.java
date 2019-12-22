package com.zbl.processor.generate;

import com.zbl.processor.generate.mod.ProxyInfo;
import com.zbl.processor.handler.Constance;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import static javax.lang.model.element.Modifier.PRIVATE;

@SuppressWarnings("unused")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ZProcessor extends AbstractProcessor {

    private Filer mFileUtils;

    private Elements mElementUtils;

    private Map<String, ProxyInfo> mProxyMap = new HashMap<>();

    private Set<Class<? extends Annotation>> source;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFileUtils = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        source = Constance.supportClasses();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        if (source == null || source.isEmpty()) {
            error(null, "the processor task unable to running within empty source");
            return null;
        }
        Set<String> set = new LinkedHashSet<>();
        for (Class c : source) {
            set.add(c.getCanonicalName());
        }
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (roundEnvironment.processingOver()) return true;
        mProxyMap.clear();
        for (Class<? extends Annotation> in : source)
            if (!saveAnnotation(roundEnvironment, in)) {
                error(null, "task running broken , because a processing annotate was not supported");
                return false;
            }
        for (String key : mProxyMap.keySet()) {
            ProxyInfo proxyInfo = mProxyMap.get(key);
            try {
                JavaFileObject jfo = mFileUtils.createSourceFile(proxyInfo.getProxyClassFullName(), proxyInfo.getTypeElement());
                String body = proxyInfo.generateCode();
                proxyInfo.clear();
                Writer writer = jfo.openWriter();
                writer.write(body);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                error(proxyInfo.getTypeElement(), "Unable to write injector for type %s: %s", proxyInfo.getTypeElement(), e.getMessage());
            }
        }
        return true;
    }

    private boolean saveAnnotation(RoundEnvironment roundEnvironment, Class<? extends Annotation> mClass) {
        try {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(mClass);
            for (Element element : elements) {
                if (!checkAnnotationValid(element)) {
                    return false;
                }
                TypeElement typeElement = (TypeElement) element;
                String qualifiedName = typeElement.getQualifiedName().toString();
                ProxyInfo proxyInfo = mProxyMap.get(qualifiedName);
                if (proxyInfo == null) {
                    proxyInfo = new ProxyInfo(mElementUtils, element, mClass);
                    mProxyMap.put(qualifiedName, proxyInfo);
                }
            }
        } catch (Exception e) {
            error(null, "build error case : %s", e.getMessage());
        }
        return true;
    }

    private TypeElement toElement(TypeMirror typeMirror) {
        Types types = processingEnv.getTypeUtils();
        return (TypeElement) types.asElement(typeMirror);
    }

    private boolean checkAnnotationValid(Element annotatedElement) {
        if (annotatedElement.getModifiers().contains(PRIVATE)) {
            error(annotatedElement, "%s() must can not be private.", annotatedElement.getSimpleName());
            return false;
        }
        return true;
    }


    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private void print(String s, Object... args) {
        if (args.length > 0) {
            s = String.format(s, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "-----> " + s, null);
    }
}
