package com.winhong.mango;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析yaml配置文件的类
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/17
 */
public class YamlUtil {
    private static final YamlUtil instance = new YamlUtil();
    private final Map<String, Object> datas = new HashMap<String, Object>();
    private Map<String, String> I18N = new HashMap<String, String>();
    // 测试账号名称
    public static String USER_NAME;
    // 测试账号的密码
    public static String USER_PASSWORD = "111111";

    public static YamlUtil getInstance() {
        return instance;
    }

    public YamlUtil() {
        URL configpath = Thread.currentThread().getContextClassLoader().getResource("config/");
        File file = new File(configpath.getFile());
        fillData(datas, file);
        // 设置国际化配置
        if ("zh".equals(((Map) datas.get("config")).get("language"))) {
            I18N = (Map<String, String>)datas.get("messages.zh");
        } else {
            I18N = (Map<String, String>)datas.get("messages.en");
        }
        System.out.println(I18N.get("create"));
    }

    private void fillData(Map<String, Object> dmap, File root) {
        try {
            File[] files = root.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        fillData(dmap, f);
                    } else {
                        InputStream input = new FileInputStream(f);
                        Yaml yaml = new Yaml();
                        Object data = yaml.load(input);
                        input.close();
                        dmap.put(f.getName().substring(0, f.getName().length() - 4), data);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 获取List数据
     * @param filename
     * @return
     */
    public static List getListData(String filename) {
        Object result = getInstance().datas.get(filename);
        return result == null ? null : (List) result;
    }

    /**
     * 获取Map数据
     * @param filename
     * @return
     */
    public static Map getMapData(String filename) {
        Object result = getInstance().datas.get(filename);
        return result == null ? null : (Map) result;
    }

    /**
     * 获取国际化消息
     * @param key
     * @return
     */
    public static String getI18n(String key) {
        return getInstance().I18N.get(key);
    }

    /**
     * config.yml公共配置项
     * @param filename
     * @param key
     * @return
     */
    public static String getCommonStr(String key) {
        return (String) getMapData("config").get(key);
    }

    /**
     * config.yml公共配置项
     * @param filename
     * @param key
     * @return
     */
    public static int getCommonInt(String key) {
        return (Integer) getMapData("config").get(key);
    }

    /**
     * 各个自定义普通配置项
     * @param filename
     * @param key
     * @return
     */
    public static String getCustom(String filename, String key) {
        return (String) getMapData(filename).get(key);
    }

    /**
     * 获取配置中某个配置列表
     * @param filename
     * @param key
     * @return
     */
    public static List<String> getStrList(String filename, String key) {
        return (List<String>) getMapData(filename).get(key);
    }

    /**
     * 获取整个配置的列表
     * @param filename
     * @return
     */
    public static List<Map<String, String>> getMapList(String filename) {
        return getListData(filename);
    }

    public static void main(String[] args) {
        // 先加载测试数据的yaml配置文件
        YamlUtil data = YamlUtil.getInstance();
        // 先产生随机用户名
        String randomName = new RandomString(8).randomEmail();
        // 使用随机用户名
        data.getMapData("config").put("username", randomName);
        System.out.println(data.getMapData("config").get("username"));
    }
}
