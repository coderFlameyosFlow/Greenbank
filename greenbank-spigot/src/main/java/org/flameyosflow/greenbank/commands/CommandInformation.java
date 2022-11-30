package org.flameyosflow.greenbank.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInformation {
    String name();

    String description() default "";

    String usage() default "";

    String permission() default "";
    String permissionMessage() default "";

    String[] aliases() default {};
}
