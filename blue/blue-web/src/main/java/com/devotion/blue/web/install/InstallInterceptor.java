package com.devotion.blue.web.install;

import com.devotion.blue.web.core.JPress;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 用于拦截web如果已经安装，则不让其访问InstallController
 *
 * @created 2016年2月1日
 */
public class InstallInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        if (JPress.isInstalled()) {
            inv.getController().redirect("/");
        } else {
            inv.invoke();
        }
    }

}
