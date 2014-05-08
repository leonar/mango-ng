package com.winhong.core;

import com.winhong.core.base.BaseTest;
import com.winhong.mango.AppConfig;
import com.winhong.mango.CommonUtil;
import com.winhong.mango.RandomString;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.Map;

import static com.winhong.mango.Operations.*;
import static com.winhong.mango.YamlUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/**
 * 测试文件浏览功能
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/17
 */
public class FileViewTest extends BaseTest{
    private static Logger logger = Logger.getLogger(FileViewTest.class);

    @Test
    public void testViewFile() {
        Map configMap = getMapData("man.fileview");
        AppConfig config = new AppConfig();
        config.setUploadFilename((String) configMap.get("upload_filename"));
        config.setBodyContent((String) configMap.get("body_content"));
        config.setRuntimes((String) configMap.get("runtimes"));
        config.setFramework((String) configMap.get("framework"));
        config.setMemory((String) configMap.get("memory"));
        config.setInstanceNum((Integer) configMap.get("instance_num"));
        // 先随机生成一个应用名
        config.setAppname(new RandomString(8).randomName());
        logger.info("+++++++++++++++++++++appname:" + config.getAppname());
        logger.info("+++++++++++++++++++++username:" + USER_NAME);
        String fullpath = CommonUtil.getDirPath("zips/") + config.getUploadFilename();
        // 登录
        login(be, getCommonStr("base_url"), USER_NAME, USER_PASSWORD);
        // 点击我的应用
        clickCheck(be, "//a[@id='aapps']", "//button[@id='deployBtn']");
        clickCheck(be, "//button[@id='deployBtn']", "//input[@id='appUploadnameInput']");
        be.type("//input[@id='appUploadnameInput']", config.getAppname());
        be.click(String.format("//input[@name='framework' and @value='%s']", config.getFramework()));
        be.click(String.format("//input[@name='runtime' and @value='%s']", config.getRuntimes()));
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
        executeJS(be, String.format(getCustom("man.application", "vclick_even"), 0));
        // 执行file可见性的js
        executeJS(be, getCustom("man.application", "file_visiable"));
        be.expectElementExistOrNot(true, "//input[@id='arrangelocalfile']", 2000);
        be.type("//input[@id='arrangelocalfile']", fullpath);
        be.pause(1000);
        be.click("//button[@id='submitButton']");
        be.expectElementExistOrNot(true, "//div[@id='listMydeployTitle']", 60000);

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
        // 启动应用
        be.click("//button[@id='controlBtn']");
        // 等待直到应用状态为运行中
        refreshUntilElementPresent(be, String.format("//td[@id='ad_status']/span[text()='%s']",
                getI18n("man_application_list_status_run")), getCommonInt("app_start_timeout"));

        // 展开tomcat文件夹
        be.expectElementExistOrNot(true, "//a[@id='filetree_1_a']", 2000);
        be.click("//span[text()='tomcat/']/../.././span");
        be.pause(1000);
        be.expectElementExistOrNot(true, "//span[text()='webapps/']/../.././span", 3000);
        be.click("//span[text()='webapps/']/../.././span");
        be.pause(1000);
        be.expectElementExistOrNot(true, "//span[starts-with(@id, 'filetree') and contains(text(), 'ROOT/')]", 3000);
        // 直接点击ROOT文件夹
        be.click("//span[starts-with(@id, 'filetree') and contains(text(), 'ROOT/')]");
        // index.jsp会出现在文件列表中
        be.pause(1000);
        be.expectElementExistOrNot(true, "//a[starts-with(@class, 'viewFileLink') and text()='index.jsp']", 3000);

        // 验证支持的文件浏览功能(.txt, .jsp...)
        logger.info("00000000000000000000000000000000000");
        be.click("//a[starts-with(@class, 'viewFileLink') and text()='index.jsp']");
        be.pause(1000);
        logger.info("1111111111111111111111111111111");
        be.expectElementExistOrNot(true, "//textarea[@id='fileContentText']", 1000);
        logger.info("222222222222222222222222222222222");
        assertThat(be.getText("//textarea[@id='fileContentText']"), containsString(config.getBodyContent()));
        logger.info("333333333333333333333333333333333");

        // 验证文件下载浏览功能支持
        be.expectElementExistOrNot(true, "//a[@id='fileDownloadBtn']", 1000);
        be.click("//a[@id='fileDownloadBtn']");
        be.pause(1000);
        // 关闭弹出框
        be.click("//a[@id='closeBtn']");

        // 最后删除这个应用
        clickCheck(be, "//button[@id='delBtn']", "//button[@id='deleteUserInfoBtn']");
        be.click("//button[@id='deleteUserInfoBtn']");
        // 检查这个应用是否被删除了
        be.expectElementExistOrNot(false, String.format("//a[@id='%s_detail_d']", config.getAppname()), 3000);
    }
}
