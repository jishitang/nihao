package cn.webank.common.conf;

import cn.webank.common.server.ContextProvider;

import java.util.Properties;

/**
 * Created by junqizhang on 08/06/2017.
 */
public class ConfigUtil {
    private static Properties appProperties = null;

    public ConfigUtil() {
    }

    static void setAppProperties(Properties prop) {
        appProperties = prop;
    }

    public static String getConf(String key, String defaultValue) {
        return appProperties.getProperty(key, defaultValue);
    }

    public static String getConf(String key) {
        return appProperties.getProperty(key, (String) null);
    }

    public static String getFileConf(String key, String fileName, String defaultValue) {
        Properties prop = (Properties) ContextProvider.getBean(trimExt(fileName), Properties.class);
        return null == prop ? appProperties.getProperty(key, defaultValue) : prop.getProperty(key, defaultValue);
    }

    public static String getFileConf(String key, String fileName) {
        return getFileConf(key, fileName, (String) null);
    }

    private static String trimExt(String fileName) {
        String propertyExt = ".properties";
        int idx = fileName.lastIndexOf(propertyExt);
        return idx >= 0 ? fileName.substring(0, idx) : fileName;
    }
}
