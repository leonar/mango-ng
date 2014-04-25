package com.winhong.core;

import com.winhong.core.base.BaseTest;
import com.winhong.mango.AppConfig;
import com.winhong.mango.RandomString;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.Map;

import static com.winhong.mango.CommonUtil.*;
import static com.winhong.mango.YamlUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/**
 * 测试修改环境变量功能
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/17
 */
public class ModifyEnvTest extends BaseTest {
    private static Logger logger = Logger.getLogger(ModifyEnvTest.class);

    @Test
    public void testModifyEnvironment() {
        Map configMap = getMapData("man.modifyenv");
        String randomAppname = new RandomString(8).randomName();
        logger.info("+++++++++++++++++++++appname:" + randomAppname);
        logger.info("+++++++++++++++++++++username:" + USER_NAME);
        String fullpath = Thread.currentThread().getContextClassLoader().getResource("zips/").getFile()
                + configMap.get("upload_filename");
        // 登录
        login(be, getCommonStr("base_url"), USER_NAME, USER_PASSWORD);
        // 点击我的应用
        clickCheck(be, "//a[@id='aapps']", "//button[@id='deployBtn']");
        clickCheck(be, "//button[@id='deployBtn']", "//input[@id='appUploadnameInput']");
        be.type("//input[@id='appUploadnameInput']", randomAppname);
        be.click(String.format("//input[@name='framework' and @value='%s']", (String)configMap.get("framework")));
        be.click(String.format("//input[@name='runtime' and @value='%s']", (String) configMap.get("runtimes")));
        // 实例数微调
        if ((Integer)configMap.get("instance_num") > 1) {
            for (int i = 1, max = (Integer) configMap.get("instance_num"); i < max; i++) {
                be.click("//div[@id='instancesDiv']/div/button[1]");
            }
        }
        // 选择内存
        be.click(String.format(
                "//input[@name='ram' and contains(@onclick, \"changeMemory('%s')\")]", (String) configMap.get("memory")));
        // 滚动条到页面底部
        executeJS(be, getCommonStr("scroll_bottom"));
        // 执行文件类型选择的js
        executeJS(be, String.format(getCustom("man.application", "vclick_even"), 0));
        // 执行file可见性的js
        executeJS(be, getCustom("man.application", "file_visiable"));
        be.expectElementExistOrNot(true, "//input[@id='arrangelocalfile']", 2000);
        be.type("//input[@id='arrangelocalfile']", fullpath.substring(1));
        be.pause(1000);
        be.click("//button[@id='submitButton']");
        be.expectElementExistOrNot(true, "//div[@id='listMydeployTitle']", 60000);

        // 进入应用详情页面
        be.click(String.format("//a[@id='%s_detail_d']", randomAppname));
        // 确认url
        assertThat(be.getText("//td[@id='ad_urls']"),
                is(String.format("%s.%s", randomAppname, getCommonStr("wingarden_url"))));
        // 实例数
        assertThat(be.getText("//td[@id='ad_insatnces']"), is(String.valueOf(configMap.get("instance_num"))));
        // 运行语言
        assertThat(be.getText("//td[@id='ad_runtimes']"), is((String) configMap.get("runtimes")));
        // 使用框架
        assertThat(be.getText("//td[@id='ad_framework']"), is((String)configMap.get("framework")));
        // 内存数
        assertThat(be.getText("//td[@id='ad_memory']"), is(String.format("%s M", (String) configMap.get("memory"))));

        // 先停止应用
        be.click("//button[@id='controlBtn']");
        be.pause(1000);
        // 启动应用
        be.click("//button[@id='controlBtn']");
        // 等待直到应用状态为运行中
        refreshUntilElementPresent(be, String.format("//td[@id='ad_status']/span[text()='%s']",
                getMsg("man_application_list_status_run")), getCommonInt("app_start_timeout"));
        // 访问应用
        be.click("//button[@id='visitBtn']");
        String curwindow = swithLastWindow(be);
        String open_tag_xpath = "//td[text()='short_open_tag']/following-sibling::td[1]";
        // 修改前的环境变量
        assertThat(be.getText(open_tag_xpath), is((String)configMap.get("open_tag_pre")));
        be.getBrowserCore().close();
        be.selectWindow(curwindow);

        // 先停止应用
        be.click("//button[@id='controlBtn']");
        be.pause(1000);

        // 点击修改应用
        clickCheck(be, "//button[@id='modifyBtn']", "//textarea[@id='uris']");
        be.type("//textarea[@id='env']", String.format("short_open_tag=%s", (String)configMap.get("open_tag_post")));
        // 滚动条到页面底部
        executeJS(be, getCommonStr("scroll_bottom"));
        be.click("//button[@id='submitButton']");
        be.expectElementExistOrNot(true, "//button[@id='controlBtn']", 3000);
        // 启动应用
        be.click("//button[@id='controlBtn']");
        // 等待直到应用状态为运行中
        refreshUntilElementPresent(be, String.format("//td[@id='ad_status']/span[text()='%s']",
                getMsg("man_application_list_status_run")), getCommonInt("app_start_timeout"));
        // 访问应用
        be.click("//button[@id='visitBtn']");
        curwindow = swithLastWindow(be);
        open_tag_xpath = "//td[text()='short_open_tag']/following-sibling::td[1]";
        // 修改后的环境变量
        assertThat(be.getText(open_tag_xpath), is((String)configMap.get("open_tag_post")));
        be.getBrowserCore().close();
        be.selectWindow(curwindow);

        // 最后删除这个应用
        clickCheck(be, "//button[@id='delBtn']", "//button[@id='deleteUserInfoBtn']");
        be.click("//button[@id='deleteUserInfoBtn']");
        // 检查这个应用是否被删除了
        be.expectElementExistOrNot(false, String.format("//a[@id='%s_detail_d']", randomAppname), 3000);
    }
}
