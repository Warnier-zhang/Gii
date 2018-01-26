package org.mybatis.gii;

import java.util.HashMap;
import java.util.Map;

public class Config {
    /**
     * 项目根目录；
     */
    public static String APP_BASE = "";

    /**
     * 类文件路径；
     */
    public static final String CLS_BASE = "target/classes";

    /**
     * 配置文件；
     */
    public static final String CFG_NAME = "gii.properties";

    /**
     * 模板路径；
     */
    public static final String TPL_PATH = "/tpls";

    /**
     * MyBatis 3 JDBC类型；
     */
    public static Map<String, String> JDBCTYPES;

    /**
     * Java属性类型；
     */
    public static Map<String, String> JAVATYPES;

    static {
        // Config类绝对路径；
//        String path = Config.class.getClassLoader().getResource("/").getPath();

        // 项目根目录；
//        APP_BASE = path.substring(0, path.indexOf(CLS_BASE));

        // MyBatis 3 JDBC类型；
        JDBCTYPES = new HashMap<String, String>();
        JDBCTYPES.put("VARCHAR2", "VARCHAR");
        JDBCTYPES.put("DATE", "TIMESTAMP");
        JDBCTYPES.put("NUMBER", "DECIMAL");
        JDBCTYPES.put("BLOB", "BLOB");

        // Java属性类型；
        JAVATYPES = new HashMap<String, String>();
        JAVATYPES.put("DATE-0", "Date");
        JAVATYPES.put("VARCHAR2-0", "String");
        JAVATYPES.put("BLOB-0", "byte[]");
        JAVATYPES.put("CHAR-0", "String");
        JAVATYPES.put("NUMBER-0", "Long");
        JAVATYPES.put("NUMBER-1", "Double");
    }
}
