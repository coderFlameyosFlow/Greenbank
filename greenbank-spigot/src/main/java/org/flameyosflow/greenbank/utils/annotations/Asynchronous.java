package org.flameyosflow.greenbank.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this if a (class/method) (is/should be) 100% made asynchronously
 * or part asynchronous
 *
 * @author FlameyosFlow
 * @since 1.0.0 BUILD 7
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Asynchronous {}