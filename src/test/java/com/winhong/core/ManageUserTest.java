package com.winhong.core;

import com.winhong.core.base.BaseTest;
import com.winhong.mango.RandomString;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import static com.winhong.mango.Operations.*;
import static com.winhong.mango.YamlUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * 测试用户管理功能
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/17
 */
public class ManageUserTest extends BaseTest {
    private static Logger logger = Logger.getLogger(ManageUserTest.class);

    @Test
    public void testManagerUser() {
        // 先产生一个随机的测试用户名
        String randUser = new RandomString(8).randomEmail();
        logger.info("+++++++++++++++++++++username:" + randUser);

        // 第一步：创建用户
        login(be, getCommonStr("base_url"),
                getCommonStr("username_admin"), getCommonStr("password_admin"));
        // 点击用户管理
        be.click("//a[@id='ausers']");
        be.expectElementExistOrNot(false, String.format("//a[@id='del_%s']", randUser), 3000);
        // 创建这个用户
        be.click("//button[@id='createUserBtn']");
        be.expectElementExistOrNot(true, "//input[@id='emailR']", 2000);
        be.type("//input[@id='emailR']", randUser);
        be.type("//input[@id='passwordR']", USER_PASSWORD);
        be.type("//input[@id='repasswordR']", USER_PASSWORD);
        be.click("//button[@id='submitUser']");
        // 看下是不是真的有了新建用户
        be.expectElementExistOrNot(true, String.format("//a[@id='del_%s']", randUser), 3000);

        // 新用户登录
        logout(be);
        login(be, getCommonStr("base_url"), randUser, USER_PASSWORD);

        // 第二步：禁止用户
        logout(be);
        login(be, getCommonStr("base_url"),
                getCommonStr("username_admin"), getCommonStr("password_admin"));
        // 点击用户管理
        be.click("//a[@id='ausers']");
        // 禁止用户
        be.expectElementExistOrNot(true, String.format("//a[@id='aut_%s' and text()='%s']",
                randUser, getI18n("forbidden")), 3000);
        be.click(String.format("//a[@id='aut_%s']", randUser));
        be.expectElementExistOrNot(true, "//button[@id='updateUserStateBtn']", 1000);
        be.click("//button[@id='updateUserStateBtn']");
        be.pause(1000);
        // 检查是否修改完成了
        be.expectElementExistOrNot(true, String.format("//a[@id='aut_%s' and text()='%s']",
                randUser, getI18n("activate")), 3000);
        // 使用刚刚禁止的用户再次登录
        logout(be);
        be.type("//input[@id='email']", randUser);
        be.type("//input[@id='password']", USER_PASSWORD);
        be.click("//button[@id='signinBtn']");
        // 登录出错
        be.expectTextExistOrNot(true, getI18n("messages.31002"), 3000);

        // 第三步：激活用户
        be.open(String.format("%s/logout", getCommonStr("base_url")));
        login(be, getCommonStr("base_url"),
                getCommonStr("username_admin"), getCommonStr("password_admin"));
        // 点击用户管理
        be.click("//a[@id='ausers']");
        // 激活用户
        be.expectElementExistOrNot(true, String.format("//a[@id='aut_%s' and text()='%s']",
                randUser, getI18n("activate")), 3000);
        be.click(String.format("//a[@id='aut_%s']", randUser));
        be.expectElementExistOrNot(true, "//button[@id='updateUserStateBtn']", 1000);
        be.click("//button[@id='updateUserStateBtn']");
        be.pause(1000);
        // 检查是否修改完成了
        be.expectElementExistOrNot(true, String.format("//a[@id='aut_%s' and text()='%s']",
                randUser, getI18n("forbidden")), 3000);
        // 使用刚刚激活的用户再次登录
        logout(be);
        login(be, getCommonStr("base_url"), randUser, USER_PASSWORD);

        // 第四步：最后删除用户
        logout(be);
        login(be, getCommonStr("base_url"),
                getCommonStr("username_admin"), getCommonStr("password_admin"));
        // 点击用户管理
        be.click("//a[@id='ausers']");
        // 删除用户
        be.expectElementExistOrNot(true, String.format("//a[@id='del_%s']", randUser), 3000);
        be.click(String.format("//a[@id='del_%s']", randUser));
        be.expectElementExistOrNot(true, "//button[@id='deleteUserInfoBtn']", 1000);
        be.click("//button[@id='deleteUserInfoBtn']");
        be.pause(1000);
        // 验证是否已经删除了
        be.expectElementExistOrNot(false, String.format("//a[@id='del_%s']", randUser), 3000);
    }
}
