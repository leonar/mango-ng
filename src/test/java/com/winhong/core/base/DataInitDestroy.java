package com.winhong.core.base;

import com.winhong.dagger.BrowserEmulator;
import com.winhong.mango.CommonUtil;
import com.winhong.mango.RandomString;
import com.winhong.mango.YamlUtil;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import static com.winhong.mango.YamlUtil.*;

/**
 * 数据准备/销毁
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/17
 */
public class DataInitDestroy {
    @BeforeSuite
    public void testBeforeSuite() {
        // 先加载测试数据的yaml配置文件
        YamlUtil.getInstance();
        // 先产生随机用户名
        USER_NAME = new RandomString(8).randomEmail();

        // 管理员登录创建这个新的测试账号
        BrowserEmulator browser = new BrowserEmulator();
        CommonUtil.login(browser, getCommonStr("base_url"),
                getCommonStr("username_admin"), getCommonStr("password_admin"));
        // 点击用户管理
        browser.click("//a[@id='ausers']");
        browser.expectElementExistOrNot(false, String.format("//a[@id='del_%s']", USER_NAME), 3000);
        // 创建这个用户
        browser.click("//button[@id='createUserBtn']");
        browser.expectElementExistOrNot(true, "//input[@id='emailR']", 2000);
        browser.type("//input[@id='emailR']", USER_NAME);
        browser.type("//input[@id='passwordR']", USER_PASSWORD);
        browser.type("//input[@id='repasswordR']", USER_PASSWORD);
        browser.click("//button[@id='submitUser']");
        // 看下是不是真的有了新建用户
        browser.expectElementExistOrNot(true, String.format("//a[@id='del_%s']", USER_NAME), 3000);
        browser.quit();
    }

    @AfterSuite
    public void testAfterSuite() {
        BrowserEmulator browser = new BrowserEmulator();
        // 管理员登录创建这个新的测试账号
        CommonUtil.login(browser, getCommonStr("base_url"),
                getCommonStr("username_admin"), getCommonStr("password_admin"));
        // 点击用户管理
        browser.click("//a[@id='ausers']");
        browser.expectElementExistOrNot(true, String.format("//a[@id='del_%s']", USER_NAME), 2000);
        browser.click(String.format("//a[@id='del_%s']", USER_NAME));
        //deleteUserInfoBtn
        browser.expectElementExistOrNot(true, "//button[@id='deleteUserInfoBtn']", 1000);
        browser.click("//button[@id='deleteUserInfoBtn']");
        // 确认已经删除了
        browser.expectElementExistOrNot(false, String.format("//a[@id='del_%s']", USER_NAME), 2000);
        browser.quit();
    }
}
