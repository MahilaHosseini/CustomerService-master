package com.customerService.app.facade;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MapTo {
    Class targetEntity() default void.class;

}
