package com.devotion.blue.model.generator;

import java.util.List;

import javax.sql.DataSource;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.generator.MetaBuilder;
import com.jfinal.plugin.activerecord.generator.TableMeta;
import com.jfinal.plugin.druid.DruidPlugin;

public class JGenerator {


    private final String basePackage;
    private final String dbHost;
    private final String dbName;
    private final String dbUser;
    private final String dbPassword;


    public JGenerator(String basePackage, String dbHost, String dbName,
                      String dbUser, String dbPassword) {

        this.basePackage = basePackage;
        this.dbHost = dbHost;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }


    public void doGenerate() {

        String modelPackage = basePackage + ".model";
        String baseModelPackage = basePackage + ".model.base";
//		String adminControllerPackage = basePackage+".controller.admin";

        String modelDir = PathKit.getWebRootPath() + "/src/main/java/" + modelPackage.replace(".", "/");
        String baseModelDir = PathKit.getWebRootPath() + "/src/main/java/" + baseModelPackage.replace(".", "/");
//		String adminControllerDir = PathKit.getWebRootPath() + "/src/main/java/"+adminControllerPackage.replace(".", "/");

        System.out.println("start generate...");
        System.out.println("Generate dir:" + modelDir);


        List<TableMeta> tableMetaList = new MetaBuilder(getDataSource()).build();

        new JBaseModelGenerator(baseModelPackage, baseModelDir).generate(tableMetaList);
        new JModelGenerator(modelPackage, baseModelPackage, modelDir).generate(tableMetaList);
//		new JControllerGenerator(adminControllerPackage, baseModelPackage, adminControllerDir).generate(tableMetaList);

        System.out.println("Generate finished !!!");

    }


    public DataSource getDataSource() {

        String jdbc_url = "jdbc:mysql://" + dbHost + "/" + dbName;

        DruidPlugin druidPlugin = new DruidPlugin(jdbc_url, dbUser, dbPassword);
        druidPlugin.start();
        return druidPlugin.getDataSource();
    }

}
