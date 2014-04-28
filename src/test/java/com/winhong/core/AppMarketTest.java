package com.winhong.core;

import com.winhong.core.base.BaseTest;
import com.winhong.mango.RandomString;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import static com.winhong.mango.CommonUtil.*;
import static com.winhong.mango.YamlUtil.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


/**
 * 应用商城模块测试
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/17
 */
public class AppMarketTest extends BaseTest {
    private static Logger logger = Logger.getLogger(AppMarketTest.class);

    @Test
    public void testManagerUser() {
        // 管理员登陆
        login(be, getCommonStr("base_url"),
                getCommonStr("username_admin"), getCommonStr("password_admin"));
        // 点击用户管理
        be.click("//a[@id='ausers']");
        be.expectElementExistOrNot(true, String.format("//a[@id='mod_%s']", USER_NAME), 3000);
        clickCheck(be, String.format("//a[@id='mod_%s']", USER_NAME), "//button[@id='updateSubmit']");
        String check = be.getAttribute("//input[@id='publisher_id']", "checked");
        // 赋予用户发布应用的权限
        if ("checked".equals(check)) {
            be.click("//a[@id='cancelRef']");
        } else {
            be.click("//input[@name='publisher_box']");
            assertThat(be.getAttribute("//input[@id='publisher_id']", "value"), is("true"));
            be.click("//button[@id='updateSubmit']");
        }
        // 退出管理员
        logout(be);

        // 准备数据
        String configFileKey = "man.appmarket";
        String zippath = Thread.currentThread().getContextClassLoader().getResource("zips/").getFile().substring(1);
        String pubpath = Thread.currentThread().getContextClassLoader().getResource("zips/publish/").getFile().substring(1);
        String app_upload_picname = getCustom(configFileKey, "upload_picname");
        String app_upload_sql = getCustom(configFileKey, "upload_sql");
        String app_upload_filename = getCustom(configFileKey, "upload_filename");
        String app_name = new RandomString(7).randomName();
        String show_name = new RandomString(7).randomName();
        logger.info("zippath=" + zippath);
        logger.info("pubpath=" + pubpath);
        logger.info("app_upload_picname=" + app_upload_picname);
        logger.info("app_upload_filename=" + app_upload_filename);
        logger.info("app_upload_sql=" + app_upload_sql);

        // 用户登录，发布一个应用
        userLogin(be, false);
        // 点击我的应用
        clickCheck(be, "//a[@id='aapps']", "//button[@id='pushBtn']");
        clickCheck(be, "//button[@id='pushBtn']", "//form[@id='mallAppuploadForm']");
        be.type("//input[@id='app_upload_name']", app_name);
        be.type("//input[@id='app_upload_screenName']", show_name);
        be.click("//input[@type='radio' and @value='spring']");
        be.click("//input[@name='runtime' and @value='java']");
        be.click("//input[@name='serviceName' and @value='mysql']");
        executeJS(be, String.format(getCustom(configFileKey, "file_visiable"), "app_upload_icon"));
        be.expectElementExistOrNot(true, "//input[@id='app_upload_icon']", 2000);
        be.type("//input[@id='app_upload_icon']", pubpath + app_upload_picname);
        be.pause(500);
        executeJS(be, String.format(getCustom(configFileKey, "file_visiable"), "app_upload_sql"));
        be.expectElementExistOrNot(true, "//input[@id='app_upload_sql']", 2000);
        be.type("//input[@id='app_upload_sql']", pubpath + app_upload_sql);
        be.pause(500);
        executeJS(be, String.format(getCustom(configFileKey, "file_visiable"), "app_upload_src"));
        be.expectElementExistOrNot(true, "//input[@id='app_upload_src']", 2000);
        be.type("//input[@id='app_upload_src']", zippath + app_upload_filename);
        be.pause(500);
        be.type("//input[@name='folder']", getCustom(configFileKey, "folder"));
        be.type("//input[@name='version']", getCustom(configFileKey, "version"));
        // 滚动条到页面底部
        executeJS(be, getCommonStr("scroll_bottom"));
        be.type("//textarea[@id='app_upload_description']", getCustom(configFileKey, "upload_description"));
        be.click("//button[@id='submitButton']");
        be.expectElementExistOrNot(true, String.format("//h4[@title='%s' and text()='%s']", app_name, show_name), 15000);

        // 安装刚刚发布的应用，并且提交审核
        be.click(String.format("//h4[@title='%s' and text()='%s']/../p/a", app_name, show_name));
        String installMsg = getI18n("man_application_list_mypush_operation_install");
        String waitingVerifyMsg = getI18n("man_application_list_mypush_operation_verify");
        String verifyMsg = getI18n("man_mall_application_verify_operation_verify");
        String logMsg = getI18n("man_application_list_mypush_operation_log");
        // 安装
        be.expectExistAndClick(String.format("//button[text()='%s']", installMsg), 2000);
        // 提交审核
        be.expectExistAndClick(String.format("//button[text()='%s']", waitingVerifyMsg), 50000);
        be.expectExistAndClick("//button[@id='statusConfirmBtn']", 1000);
        be.expectElementExistOrNot(true, String.format("//button[text()='%s']", logMsg), 10000);
        logout(be);

        // 管理员登录审核通过
        userLogin(be, true);
        be.click("//a[@id='aappVerifys']");
        String verifying = getI18n("man_mall_application_verify_tab_verify");
        be.click(String.format("//a[text()='%s']", verifying));
        be.expectExistAndClick(
                String.format("//a[text()='%s']/../following-sibling::td[8]/a[2]", app_name), 2000);
        be.select("//select[@id='verifyId']", getI18n("man_mall_application_verify_popup_status_pass"));
        // be.type("//textarea[@id='reason']", getCustom(configFileKey, "verify_explain"));
        be.click(String.format("//button[text()='%s']", getI18n("man_mall_application_verify_submit")));
        be.expectTextExistOrNot(true, getI18n("nothing_to_display"), 3000);
        logout(be);

        // 用户再次登录，申请上架
        userLogin(be, false);
        be.click(String.format("//h4[@title='%s' and text()='%s']/../p/a", app_name, show_name));
        String applyRelease = getI18n("man_application_list_mypush_operation_release");
        be.expectExistAndClick(String.format("//button[text()='%s']", applyRelease), 2000);
        be.expectExistAndType("//input[@id='appPrice_price']", 1000, getCustom(configFileKey, "my_price"));
        be.click("//button[@id='appSubmit_price']");
        be.expectExistAndClick(String.format("//button[text()='%s']", logMsg), 2000);
        // 查看审核日志，应该显示"已通过"了
        be.expectTextExistOrNot(true, getI18n("man_application_list_mypush_status_101"), 1000);
        be.expectExistAndClick("//button[@id='showlogBackBtn']", 500);
        logout(be);

        // 管理员再次登录，发布通过
        userLogin(be, true);
        be.click("//a[@id='aappVerifys']");
        String waitingPub = getI18n("man_application_list_mypush_status_103");
        be.expectExistAndClick(String.format("//a[text()='%s']", waitingPub), 1000);
        be.expectExistAndClick(
                String.format("//a[text()='%s']/../following-sibling::td[8]/a[2]", app_name), 2000);
        be.select("//select[@id='verifyId']", getI18n("man_mall_application_verify_popup_status_pass"));
        be.click(String.format("//button[text()='%s']", getI18n("man_mall_application_verify_submit")));
        be.expectTextExistOrNot(true, getI18n("nothing_to_display"), 3000);
        logout(be);

        // 用户再次登录
        userLogin(be, false);
        // 检查状态是否为已发布
        String pubed = getI18n("man_application_list_mypush_status_100");
        be.expectElementExistOrNot(true, String.format(
                "//span[@title='%s']/../following-sibling::li[2]/span[2][text()='%s']", app_name, pubed), 1000);
        // 进入商城
        be.click("//a[@id='appmallLink']");
        // 一键安装刚刚上架的应用
        be.expectExistAndClick(String.format("//a[@id='%s_install']", app_name), 2000);

        // 安装完后点击我购买的应用，然后启动看看
        be.expectExistAndClick("//ul[@id='mybyAppsUL']/li[1]/div/div[2]/ul/li[6]/span/a[1]", 12000);
        // 启动应用
        // be.expectExistAndClick("//button[@id='controlBtn']", 2000);

    }
}
