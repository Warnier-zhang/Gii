package org.warnier.zhang.gii;

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

    static {
        // Config类绝对路径；
//        String path = Config.class.getClassLoader().getResource("/").getPath();

        // 项目根目录；
//        APP_BASE = path.substring(0, path.indexOf(CLS_BASE));
    }
}
