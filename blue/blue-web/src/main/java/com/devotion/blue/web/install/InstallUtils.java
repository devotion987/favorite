package com.devotion.blue.web.install;

import com.alibaba.druid.filter.stat.StatFilter;
import com.devotion.blue.utils.DateUtils;
import com.devotion.blue.utils.FileUtils;
import com.devotion.blue.web.core.JPressConfig;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.FreeMarkerRender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

//import io.jpress.core.db.MysqlDialect;
public class InstallUtils {

    private static final Log log = Log.getLog(InstallUtils.class);
    private static String dbHost;
    private static String dbHostPort;
    private static String dbName;
    private static String dbUser;
    private static String dbPassword;
    public static String dbTablePrefix;

    public static void init(String db_host, String db_host_port, String db_name, String db_user,
                            String db_password, String db_tablePrefix) {
        dbHost = db_host;
        dbHostPort = db_host_port;
        dbName = db_name;
        dbUser = db_user;
        dbPassword = db_password;
        dbTablePrefix = db_tablePrefix;
    }

//    public static boolean createDbProperties() {
//        Properties p = new Properties();
//        p.put("db_host", dbHost);
//        p.put("db_host_port", dbHostPort);
//        p.put("db_name", dbName);
//        p.put("db_user", dbUser);
//        p.put("db_password", dbPassword);
//        p.put("db_tablePrefix", dbTablePrefix);
//        File pFile = new File(PathKit.getRootClassPath(), JPressConfig.configFile);
//        return save(p, pFile);
//    }

//    public static boolean createJPressProperties() {
//        Properties p = PropKit.use(JPressConfig.configFile).getProperties();
//        p.put("dev_mode", p.getProperty("dev_mode"));
//        p.put("encrypt_key", UUID.randomUUID().toString());
//        File pFile = new File(PathKit.getRootClassPath(), JPressConfig.configFile);
//        return save(p, pFile);
//    }

//    private static boolean save(Properties p, File pFile) {
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(pFile);
//            p.store(fos, "Auto create by JPress");
//        } catch (Exception e) {
//            log.warn("InstallUtils save error", e);
//            return false;
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    log.error("close FileOutputStream error:" + e);
//                }
//            }
//        }
//        return true;
//    }

    public static List<String> getTableList() throws SQLException {
        DruidPlugin dp = createDruidPlugin();
        Connection conn = dp.getDataSource().getConnection();
        List<String> tableList = query(conn, forShowTable());
        conn.close();
        dp.stop();
        return tableList;
    }

    public static void createJPressDatabase() throws SQLException {
        String installSql = forInstall(dbTablePrefix);
        DruidPlugin dp = createDruidPlugin();
        Connection conn = dp.getDataSource().getConnection();
        executeBatchSql(conn, installSql);
        conn.close();
        dp.stop();

    }

    public static void setWebName(String webName) throws SQLException {
        executeSQL(forInsertWebName(dbTablePrefix), webName);
    }

    public static void setWebFirstUser(String username, String password, String salt) throws SQLException {
        executeSQL(forInsertFirstUser(dbTablePrefix), username, password, salt, "administrator", "activited",
                DateUtils.now());
    }

    public static void executeSQL(String sql, Object... params) throws SQLException {
        DruidPlugin dp = createDruidPlugin();
        Connection conn = dp.getDataSource().getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            if (null != params && params.length > 0) {
                int i = 0;
                for (Object param : params) {
                    pstmt.setString(++i, param.toString());
                }
            }
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.warn("InstallUtils executeSQL error", e);
        } finally {
            pstmt.close();
            conn.close();
            dp.stop();
        }
    }

    private static void executeBatchSql(Connection conn, String batchSql) throws SQLException {
        Statement pst = conn.createStatement();
        if (null == batchSql) {
            throw new SQLException("SQL IS NULL");
        }

        if (batchSql.contains(";")) {
            String sqls[] = batchSql.split(";");
            for (String sql : sqls) {
                if (null != sql && !"".equals(sql.trim()))
                    pst.addBatch(sql);
            }
        } else {
            pst.addBatch(batchSql);
        }
        pst.executeBatch();
        closeStatement(pst);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T> List<T> query(Connection conn, String sql) throws SQLException {
        List result = new ArrayList();
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        int colAmount = rs.getMetaData().getColumnCount();
        if (colAmount > 1) {
            while (rs.next()) {
                Object[] temp = new Object[colAmount];
                for (int i = 0; i < colAmount; i++) {
                    temp[i] = rs.getObject(i + 1);
                }
                result.add(temp);
            }
        } else if (colAmount == 1) {
            while (rs.next()) {
                result.add(rs.getObject(1));
            }
        }
        closeStatement(pst);
        closeResultSet(rs);
        return result;
    }

    private static void closeResultSet(ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("close ResultSet error:" + e);
            }
        }
    }

    private static void closeStatement(Statement st) {
        if (null != st) {
            try {
                st.close();
            } catch (SQLException e) {
                log.error("close Statement error:" + e);
            }
        }
    }

    private static DruidPlugin createDruidPlugin() {
        DruidPlugin plugin = createDruidPlugin(dbHost, dbHostPort, dbName, dbUser, dbPassword);
        plugin.start();
        return plugin;
    }

    public static void renderInstallFinished(HttpServletRequest request, HttpServletResponse response,
                                             boolean[] isHandled) {
        isHandled[0] = true;
        // 这里不能用 JFreeMarkerRender，否则出现安装完成后缓存安装页面。
        new FreeMarkerRender("/WEB-INF/install/finished.html").setContext(request, response).render();
    }


    private static String forShowTable() {
        return "show tables;";
    }

    private static String forInstall(String tablePrefix) {
        String sqlFilePath = PathKit.getWebRootPath() + "/WEB-INF/install/sqls/mysql.sql";
        String sql_text = FileUtils.readString(new File(sqlFilePath)).replace("{table_prefix}", tablePrefix)
                .replace("{charset}", "utf8mb4");
        return sql_text;
    }

    private static String forInsertWebName(String tablePrefix) {
        return "INSERT INTO `" + tablePrefix + "option` (option_key, option_value) VALUES ('web_name', ? )";
    }

    private static String forInsertFirstUser(String tablePrefix) {
        return "INSERT INTO `" + tablePrefix + "user` (username, password, salt, role, status, created) "
                + "VALUES (?,?,?,?,?,?)";
    }

    private static DruidPlugin createDruidPlugin(String dbHost, String dbHostPort, String dbName, String dbUser,
                                                 String dbPassword) {
        String jdbc_url = "jdbc:mysql://" + dbHost + ":" + dbHostPort + "/" + dbName + "?" + "useUnicode=true&"
                + "characterEncoding=utf8&" + "zeroDateTimeBehavior=convertToNull";

        DruidPlugin druidPlugin = new DruidPlugin(jdbc_url, dbUser, dbPassword);
        druidPlugin.addFilter(new StatFilter());
        return druidPlugin;
    }

    public static void updPropertiesAfterInstalled() throws Exception {
        Properties p = PropKit.use(JPressConfig.configFile).getProperties();
        p.put("db_host", dbHost);
        p.put("db_host_port", dbHostPort);
        p.put("db_name", dbName);
        p.put("db_user", dbUser);
        p.put("db_password", dbPassword);
        p.put("db_tablePrefix", dbTablePrefix);
        p.put("encrypt_key", UUID.randomUUID().toString());
        p.put("isInstalled", "true");
        try {
            File pFile = new File(PathKit.getRootClassPath(), JPressConfig.configFile);
            OutputStream outputStream = new FileOutputStream(pFile);
            p.store(outputStream, "updated after installed:" + DateUtils.now());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("update blue.properties error:" + e);
            throw new RuntimeException(e);
        }
    }
}
