package com.devotion.blue.web.wechat;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface MessageProcesser {
    String key();
}