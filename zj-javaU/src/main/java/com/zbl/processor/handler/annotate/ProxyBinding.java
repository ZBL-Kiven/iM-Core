package com.zbl.processor.handler.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the annotator base for PIC;
 * <p>
 * annotate for any property in jvm static classes
 **/

@Target(ElementType.TYPE)
@Inherited()
@Retention(RetentionPolicy.CLASS)
public @interface ProxyBinding {

}
