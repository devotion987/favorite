package com.devotion.blue.web.core;

import java.io.File;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.List;

import com.alibaba.druid.filter.stat.StatFilter;
import com.devotion.blue.cache.JCachePlugin;
import com.devotion.blue.message.plugin.MessagePlugin;
import com.devotion.blue.model.core.JModelMapping;
import com.devotion.blue.model.core.Table;
import com.devotion.blue.search.SearcherPlugin;
import com.devotion.blue.utils.ClassUtils;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.cache.ActionCacheHandler;
import com.devotion.blue.web.core.interceptor.HookInterceptor;
import com.devotion.blue.web.core.interceptor.JI18nInterceptor;
import com.devotion.blue.web.core.render.JErrorRenderFactory;
import com.devotion.blue.web.core.render.JPressRenderFactory;
import com.devotion.blue.web.interceptor.AdminInterceptor;
import com.devotion.blue.web.interceptor.GlobelInterceptor;
import com.devotion.blue.web.router.RouterMapping;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;

import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.DiskStoreConfiguration;

public abstract class JPressConfig extends JFinalConfig {

    static Log log = Log.getLog(JPressConfig.class);
    public static final String configFile = "blue.properties";

    public void configConstant(Constants constants) {

        log.info("JPress is starting ...");

        PropKit.use(configFile);

        constants.setDevMode(PropKit.getBoolean("dev_mode", false));
        constants.setViewType(ViewType.FREE_MARKER);
        constants.setI18nDefaultBaseName("language");
        constants.setErrorRenderFactory(new JErrorRenderFactory());
        constants.setBaseUploadPath("attachment");
        constants.setEncoding(Consts.CHARTSET_UTF8);
        constants.setMaxPostSize(1024 * 1024 * 200);
        constants.setMainRenderFactory(new JPressRenderFactory());

        // constants.setTokenCache(new JTokenCache());
    }

    @SuppressWarnings("unchecked")
    public void configRoute(Routes routes) {
        List<Class<Controller>> controllerClassList = ClassUtils.scanSubClass(Controller.class);
        if (controllerClassList != null) {
            for (Class<?> clazz : controllerClassList) {
                RouterMapping urlMapping = clazz.getAnnotation(RouterMapping.class);
                if (null != urlMapping && StringUtils.isNotBlank(urlMapping.url())) {
                    if (StrKit.notBlank(urlMapping.viewPath())) {
                        routes.add(urlMapping.url(), (Class<? extends Controller>) clazz, urlMapping.viewPath());
                    } else {
                        routes.add(urlMapping.url(), (Class<? extends Controller>) clazz);
                    }
                }
            }
        }
    }

    public void configPlugin(Plugins plugins) {
        plugins.add(createEhCachePlugin());

        if (JPress.isInstalled()) {

            JCachePlugin leCachePlugin = new JCachePlugin();
            plugins.add(leCachePlugin);

            DruidPlugin druidPlugin = createDruidPlugin();
            plugins.add(druidPlugin);

            ActiveRecordPlugin activeRecordPlugin = createRecordPlugin(druidPlugin);
            activeRecordPlugin.setCache(leCachePlugin.getCache());
            activeRecordPlugin.setShowSql(JFinal.me().getConstants().getDevMode());

            plugins.add(activeRecordPlugin);

            plugins.add(new SearcherPlugin());

            plugins.add(new MessagePlugin());
        }
    }

    public EhCachePlugin createEhCachePlugin() {
        String ehcacheDiskStorePath = PathKit.getWebRootPath();
        File pathFile = new File(ehcacheDiskStorePath, ".ehcache");

        Configuration cfg = ConfigurationFactory.parseConfiguration();
        cfg.addDiskStore(new DiskStoreConfiguration().path(pathFile.getAbsolutePath()));
        return new EhCachePlugin(cfg);
    }

    public DruidPlugin createDruidPlugin() {

        Prop dbProp = PropKit.use(configFile);
        String db_host = dbProp.get("db_host").trim();

        String db_host_port = dbProp.get("db_host_port");
        db_host_port = StringUtils.isNotBlank(db_host_port) ? db_host_port.trim() : "3306";

        String db_name = dbProp.get("db_name").trim();
        String db_user = dbProp.get("db_user").trim();
        String db_password = dbProp.get("db_password").trim();

        String jdbc_url = "jdbc:mysql://" + db_host + ":" + db_host_port + "/" + db_name + "?" + "useUnicode=true&"
                + "characterEncoding=utf8&" + "zeroDateTimeBehavior=convertToNull";

        DruidPlugin druidPlugin = new DruidPlugin(jdbc_url, db_user, db_password);
        druidPlugin.addFilter(new StatFilter());

        return druidPlugin;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public ActiveRecordPlugin createRecordPlugin(IDataSourceProvider dsp) {
        ActiveRecordPlugin arPlugin = new ActiveRecordPlugin(dsp);
        List<Class<Model>> modelClassList = ClassUtils.scanSubClass(Model.class);
        if (modelClassList != null) {
            String tablePrefix = PropKit.use(configFile).get("db_tablePrefix");
            tablePrefix = (StrKit.isBlank(tablePrefix)) ? "" : (tablePrefix.trim());
            for (Class<?> clazz : modelClassList) {
                Table tb = clazz.getAnnotation(Table.class);
                if (tb == null)
                    continue;
                String tName = tablePrefix + tb.tableName();
                if (StringUtils.isNotBlank(tb.primaryKey())) {
                    arPlugin.addMapping(tName, tb.primaryKey(), (Class<? extends Model<?>>) clazz);
                } else {
                    arPlugin.addMapping(tName, (Class<? extends Model<?>>) clazz);
                }
                JModelMapping.me().mapping(clazz.getSimpleName().toLowerCase(), tName);
            }
        }
        return arPlugin;
    }

    public void configInterceptor(Interceptors interceptors) {
        interceptors.add(new JI18nInterceptor());
        interceptors.add(new GlobelInterceptor());
        interceptors.add(new AdminInterceptor());
        interceptors.add(new HookInterceptor());
    }

    public void configHandler(Handlers handlers) {
        handlers.add(new ActionCacheHandler());
        handlers.add(new JHandler());
        handlers.add(new ActionCacheHandler());
        DruidStatViewHandlerExt druidViewHandler = new DruidStatViewHandlerExt();
        handlers.add(druidViewHandler);
    }

    @Override
    public void afterJFinalStart() {
        if (JPress.isInstalled()) {
            JPress.loadFinished();
        }

        JPress.renderImmediately();
        onJPressStarted();

        log.info("JPress is started!");
    }

    @Override
    public void beforeJFinalStop() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        if (drivers != null) {
            while (drivers.hasMoreElements()) {
                try {
                    Driver driver = drivers.nextElement();
                    DriverManager.deregisterDriver(driver);
                } catch (Exception e) {
                    log.error("deRegister Driver error in beforeJFinalStop() method.", e);
                }
            }
        }
    }

    public void onJPressStarted() {
    }

}
