package com.devotion.blue.web.install;

import java.sql.SQLException;
import java.util.List;

import com.devotion.blue.utils.EncryptUtils;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.JBaseController;
import com.devotion.blue.web.router.RouterMapping;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

@RouterMapping(url = "/install", viewPath = "/WEB-INF/install")
@Before(InstallInterceptor.class)
public class InstallController extends JBaseController {

    private static final Log log = Log.getLog(InstallController.class);

    public void index() {
        render("step1.html");
    }

    public void step2() {
        String db_host = getPara("db_host");
        String db_host_port = getPara("db_host_port");
        db_host_port = StringUtils.isNotBlank(db_host_port) ? db_host_port.trim() : "3306";
        String db_name = getPara("db_name");
        String db_user = getPara("db_user");
        String db_password = getPara("db_password");
        String db_table_prefix = getPara("db_tablePrefix");

        if (!StrKit.notBlank(db_host, db_host_port, db_name, db_user)) {
            render("step2.html");
            return;
        }

        InstallUtils.init(db_host, db_host_port, db_name, db_user, db_password, db_table_prefix);

        try {
            InstallUtils.createJPressDatabase();
            redirect("/install/step3");
//            List<String> tableList = InstallUtils.getTableList();
//            if (null != tableList && tableList.size() > 0) {
//                if (tableList.contains(db_table_prefix + "attachment")
//                        && tableList.contains(db_table_prefix + "comment")
//                        && tableList.contains(db_table_prefix + "content")
//                        && tableList.contains(db_table_prefix + "mapping")
//                        && tableList.contains(db_table_prefix + "metadata")
//                        && tableList.contains(db_table_prefix + "option")
//                        && tableList.contains(db_table_prefix + "taxonomy")
//                        && tableList.contains(db_table_prefix + "user")) {
//                    // createJPressDatabase success
//                    redirect("/install/step3");
//                    return;
//                }
//            }
        } catch (Exception e) {
            log.error("InstallController step2 is error", e);
            redirect("/install/step2_error");
        }
    }

    public void step2_error() {
        render("step2_error.html");
    }

    public void step3() {
        String webName = getPara("webname");
        String username = getPara("username");
        String password = getPara("password");

        if (StrKit.isBlank(webName) || StrKit.isBlank(username) || StrKit.isBlank(password)) {
            keepPara();
            render("step3.html");
            return;
        }

        try {
            InstallUtils.setWebName(webName);
            String salt = EncryptUtils.salt();
            password = EncryptUtils.encryptPassword(password, salt);
            InstallUtils.setWebFirstUser(username, password, salt);
            InstallUtils.updPropertiesAfterInstalled();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("execute sql error:", e);
            render("step2_error.html");
        }
//        InstallUtils.createDbProperties();
//        InstallUtils.createJPressProperties();
        redirect("/");
    }

}
