package com.winhong.mango;

import static com.winhong.mango.YamlUtil.getCommonStr;

/**
 * Description of this file.
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/5/8
 */
public class CommonUtil {
    /**
     * 获取文件夹的全路径
     * @param dir
     * @return
     */
    public static String getDirPath(String dir) {
        String fullpath = Thread.currentThread().getContextClassLoader().getResource(dir).getFile();
        if ("windows".equals(getCommonStr("os_type"))) {
            fullpath = fullpath.substring(1);
        }
        return fullpath;
    }
}
