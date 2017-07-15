package com.eusecom.attendance.dagger.components;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;

/**
 * For new Dagger2 Subcomponent Builder
 */

@MapKey
@Target({ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface SubcomponentKey {
    Class<?> value();
}