package com.winhong.core;

import com.winhong.core.base.BaseTest;
import com.winhong.mango.AppConfig;
import com.winhong.mango.RandomString;
import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.winhong.mango.CommonUtil.*;
import static com.winhong.mango.YamlUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * 应用测试
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/18
 */
public class ApplicationTest extends BaseTest {
    private static Logger logger = Logger.getLogger(ApplicationTest.class);
    /**
     * 上传应用
     */
    @Test(dataProvider = "uploadDatas")
    public void testUpload(AppConfig config, String key) {
        // 先随机生成一个应用名
        config.setAppname(new RandomString(8).randomName());
        logger.info("+++++++++++++++++++++appname:" + config.getAppname());
        logger.info("+++++++++++++++++++++username:" + USER_NAME);
        logger.info("+++++++++++++++++++++key:" + key);
        // 登录
        login(be, getCommonStr("base_url"), USER_NAME, USER_PASSWORD);
        // 如果有需要先购买服务
        String serviceName = null;
        if (config.getBuyService() != null) {
            clickCheck(be, "//a[@id='af_services']", "//button[@id='slBuyBtn']");
            clickCheck(be, "//button[@id='slBuyBtn']", "//button[@id='sbBuyBtn']");
            be.click(String.format("//a[contains(@onclick, '%s')]", config.getBuyService()));
            isElementPresent(be, "//a[starts-with(@onclick, 'removeplan')]", true);
            be.click("//button[@id='sbBuyBtn']");
            be.pause(10000);
            isElementPresent(be, String.format(
                    "//a[contains(@id, '%s')]", config.getVendorVersion()), true);
            // 获取需要选择的服务名
            serviceName = be.getText(
                    String.format("//h4[@title='%s']", getI18n("man_service_list_name")));
        }
        // 如果有需要先上传远程文件
        String fullpath = Thread.currentThread().getContextClassLoader().getResource("zips/").getFile()
                + config.getUploadFilename();
        if (config.getFiletype() == 1) {
            // 点击文件管理
            be.click("//a[@id='uploadFileLink']");
            // 切换到新窗口
            String current = swithLastWindow(be);
            isElementPresent(be, "//button[@id='largeUploadBtn']", true);
            clickCheck(be, "//button[@id='largeUploadBtn']", "//span[@id='error_tips']");
            executeJS(be, config.getFileVisiable());
            be.pause(1000);
            isElementPresent(be, "//input[@id='fileChoose']", true);
            be.type("//input[@id='fileChoose']", fullpath.substring(1));
            be.pause(500);
            be.click("//a[@id='uploadButton']");
            be.expectElementExistOrNot(true, String.format("//td[text()='%s']", config.getUploadFilename()), 20000);
            // 关闭这个窗口
            be.getBrowserCore().close();
            // 返回原来窗口
            be.selectWindow(current);
        }

        // 点击我的应用
        clickCheck(be, "//a[@id='aapps']", "//button[@id='deployBtn']");
        clickCheck(be, "//button[@id='deployBtn']", "//input[@id='appUploadnameInput']");
        be.type("//input[@id='appUploadnameInput']", config.getAppname());
        be.click(String.format("//input[@name='framework' and @value='%s']", config.getFramework()));
        be.click(String.format("//input[@name='runtime' and @value='%s']", config.getRuntimes()));
        if (serviceName != null) {
            be.click(String.format("//input[@name='serviceName' and @value='%s']", serviceName));
        }
        // 实例数微调
        if (config.getInstanceNum() > 1) {
            for (int i = 1, max = config.getInstanceNum(); i < max; i++) {
                be.click("//div[@id='instancesDiv']/div/button[1]");
            }
        }
        // 选择内存
        be.click(String.format(
                "//input[@name='ram' and contains(@onclick, \"changeMemory('%s')\")]", config.getMemory()));
        // 滚动条到页面底部
        executeJS(be, getCommonStr("scroll_bottom"));
        // 执行文件类型选择的js
        executeJS(be, String.format(getCustom("man.application", "vclick_even"), config.getFiletype()));
        if (config.getFiletype() == 0) {
            // 执行file可见性的js
            executeJS(be, getCustom("man.application", "file_visiable"));
            be.expectElementExistOrNot(true, "//input[@id='arrangelocalfile']", 2000);
            be.type("//input[@id='arrangelocalfile']", fullpath.substring(1));
        } else {
            // 执行展开js
            executeJS(be, getCustom("man.application", "vremote_even"));
            be.click(String.format("//li[text()='%s']", config.getUploadFilename()));
        }
        be.pause(1000);
        be.click("//button[@id='submitButton']");
        be.expectElementExistOrNot(true, "//div[@id='listMydeployTitle']", 50000);

        // 进入应用详情页面
        be.click(String.format("//a[@id='%s_detail_d']", config.getAppname()));
        // 确认url
        assertThat(be.getText("//td[@id='ad_urls']"),
                is(String.format("%s.%s", config.getAppname(), getCommonStr("wingarden_url"))));
        // 实例数
        assertThat(be.getText("//td[@id='ad_insatnces']"), is(String.valueOf(config.getInstanceNum())));
        // 运行语言
        assertThat(be.getText("//td[@id='ad_runtimes']"), is(config.getRuntimes()));
        // 使用框架
        assertThat(be.getText("//td[@id='ad_framework']"), is(config.getFramework()));
        // 内存数
        assertThat(be.getText("//td[@id='ad_memory']"), is(String.format("%s M", config.getMemory())));

        // 开始查看应用的首页内容
        be.click("//button[@id='controlBtn']");
        be.pause(1000);
        if(MD_KEYS.contains(key)) {
            // 点击修改应用
            clickCheck(be, "//button[@id='modifyBtn']", "//textarea[@id='uris']");
            // 点击增加/减少实例微调，1 增加应用实例， 2 减少应用实例
            int diff = config.getModifyInstanceNum() - config.getInstanceNum();
            if (diff > 0) {
                for (int i = 1; i <= diff; i++) {
                    be.click("//div[@id='instancesDiv']/div/button[1]");
                }
            } else if (diff < 0) {
                for (int i = 1; i <= -diff; i++) {
                    be.click("//div[@id='instancesDiv']/div/button[2]");
                }
            }
            // 修改内存
            String mxpath = String.format(
                    "//input[@name='ram' and contains(@onclick, \"changeMemory('%s'\")]", config.getModifyMemory());
            be.click(mxpath);
            // 修改URI
            String realUri = "url" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + config.getModifyUri();
            be.type("//textarea[@id='uris']", realUri);
            // 滚动条到页面底部
            executeJS(be, getCommonStr("scroll_bottom"));
            be.pause(1000);
            be.click("//button[@id='submitButton']");
            be.expectElementExistOrNot(true, "//button[@id='controlBtn']", 8000);
            // 启动应用
            be.click("//button[@id='controlBtn']");
            refreshUntilElementPresent(be, String.format("//td[@id='ad_status']/span[text()='%s']",
                    getI18n("man_application_list_status_run")), getCommonInt("app_start_timeout"));
            // 检查修改后的实例数/内存/URI
            be.expectElementExistOrNot(true, String.format("//td[contains(text(), '%s')]", realUri), 1000);
            assertThat(be.getText("//td[@id='ad_insatnces']"), is(String.valueOf(config.getModifyInstanceNum())));
            assertThat(be.getText("//td[@id='ad_memory']"), is(config.getModifyMemory() + " M"));
            // 修改后开始检查新的uri是否可以使用
            String currentUrl = be.getBrowserCore().getWindowHandle();
            String newUrl = String.format("http://%s", realUri);
            executeJS(be, String.format("window.open('%s', 'new_window')", newUrl));
            be.selectWindow("new_window");
            maximizeWindow(be);
            be.expectTextExistOrNot(true, config.getBodyContent(), 5000);
            be.getBrowserCore().close();
            be.selectWindow(currentUrl);
        } else {
            // 启动应用
            be.click("//button[@id='controlBtn']");
            // 等待直到应用状态为运行中
            refreshUntilElementPresent(be, String.format("//td[@id='ad_status']/span[text()='%s']",
                    getI18n("man_application_list_status_run")), getCommonInt("app_start_timeout"));
            // 访问应用
            be.click("//button[@id='visitBtn']");
            String curwindow = swithLastWindow(be);
            be.expectTextExistOrNot(true, config.getBodyContent(), 15000);
            be.getBrowserCore().close();
            be.selectWindow(curwindow);
        }

        // 最后删除这个应用
        clickCheck(be, "//button[@id='delBtn']", "//button[@id='deleteUserInfoBtn']");
        be.click("//button[@id='deleteUserInfoBtn']");
        // 检查这个应用是否被删除了
        be.expectElementExistOrNot(false, String.format("//a[@id='%s_detail_d']", config.getAppname()), 3000);

        // 最后删除服务
        if (serviceName != null) {
            logger.info("serviceName=" + serviceName);
            clickCheck(be, "//a[@id='af_services']", "//button[@id='slBuyBtn']");
            String sx = "//p/a[substring(@href, string-length(@href) - string-length('%s')+1) = '%s' and text()='%s']";
            clickCheck(be, String.format(sx, serviceName, serviceName, getI18n("man_service_list_viewdetail")), "//button[@id='sdDeleteBtn']");
            be.click("//button[@id='sdDeleteBtn']");
            be.expectElementExistOrNot(true, "//button[@id='sdConfirmBtn']", 2000);
            be.click("//button[@id='sdConfirmBtn']");
            be.expectElementExistOrNot(true, "//button[@id='slBuyBtn']", 2000);
            // 检查服务是否已经删除了
            be.expectElementExistOrNot(false, String.format(sx, serviceName, serviceName, getI18n("man_service_list_viewdetail")), 2000);
        }
        logout(be);
    }

    /**
     * 为应用上传提供配置数据
     *
     * @return 配置数据
     */
    @DataProvider(name = "uploadDatas")
    public static Object[][] uploadDatas() {
        UP_KEYS = getStrList("man.application", "uploadKeys");
        MD_KEYS = getStrList("man.application", "modifyKeys");
        Set<String> all = new HashSet<String>();
        all.addAll(UP_KEYS);
        all.addAll(MD_KEYS);
        return prepareData(all);
    }
    public static List<String> UP_KEYS;
    public static List<String> MD_KEYS;

    private static Object[][] prepareData(Set<String> keyArray) {
        Object[][] result = new Object[keyArray.size()][];
        int i = 0;
        for (String kk : keyArray) {
            AppConfig config = new AppConfig();
            Map configMap = (Map) getMapData("man.application").get(kk);
            config.setDescription((String) configMap.get("description"));
            config.setFileVisiable((String) configMap.get("file_visiable"));
            config.setBuyService((String) configMap.get("by_service"));
            config.setVendorVersion((String) configMap.get("vendor_version"));
            config.setUploadFilename((String) configMap.get("upload_filename"));
            config.setFiletype((Integer) configMap.get("filetype"));
            //config.setAppname((String) configMap.get("appname"));
            config.setInstanceNum((Integer) configMap.get("instance_num"));
            config.setBodyContent((String) configMap.get("body_content"));
            config.setRuntimes((String) configMap.get("runtimes"));
            config.setFramework((String) configMap.get("framework"));
            config.setMemory((String) configMap.get("memory"));
            if (configMap.get("m_instance_num") != null) {
                config.setModifyInstanceNum((Integer) configMap.get("m_instance_num"));
            }
            config.setModifyMemory((String) configMap.get("m_memory"));
            config.setModifyUri((String) configMap.get("m_uri"));
            result[i++] = new Object[]{config, kk};
        }
        logger.info("result.length=" + result.length);
        return result;
    }
}
