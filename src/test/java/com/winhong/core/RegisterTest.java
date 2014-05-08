package com.winhong.core;

import com.winhong.core.base.BaseTest;
import com.winhong.mango.Operations;
import com.winhong.mango.RandomString;
import org.testng.annotations.Test;

import static com.winhong.mango.Operations.*;
import static com.winhong.mango.YamlUtil.getCommonStr;
import static com.winhong.mango.YamlUtil.getI18n;

/**
 * 注册流程测试
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/18
 */
public class RegisterTest extends BaseTest {
    /**
     * 测试Email格式错误
     */
    @Test
    public void testEmailFormat() {
        String username = "wrong";
        String password = "111111";
        openHome(be, getCommonStr("base_url"));
        clickCheck(be, "//a[@id='registLink']", "//legend[@id='welRegister']");
        be.type("//input[@id='userEmailInput']", username);
        be.type("//input[@id='passwordR']", password);
        be.type("//input[@id='repasswordR']", password);
        be.click("//input[@id='agree']");
        be.click("//button[@id='submitBtn']");
        isTextPresent(be, getI18n("js_illegal_email"), true);
    }

    /**
     * 测试密码长度错误
     */
    @Test(dependsOnMethods = "testEmailFormat")
    public void testPasswordFormat() {
        String username = new RandomString(8).randomEmail();
        String password = "11";
        be.refresh();
        be.type("//input[@id='userEmailInput']", username);
        be.type("//input[@id='passwordR']", password);
        be.type("//input[@id='repasswordR']", password);
        be.click("//input[@id='agree']");
        be.click("//button[@id='submitBtn']");
        isTextPresent(be, String.format(getI18n("js_minlength"), 6), true);
    }

    /**
     * 测试两次密码输入不一致
     */
    @Test(dependsOnMethods = "testPasswordFormat")
    public void testPasswordNotEqual() {
        String username = new RandomString(8).randomEmail();
        String password = "111111";
        String rpassword = "222222";
        be.refresh();
        be.type("//input[@id='userEmailInput']", username);
        be.type("//input[@id='passwordR']", password);
        be.type("//input[@id='repasswordR']", rpassword);
        be.click("//input[@id='agree']");
        be.click("//button[@id='submitBtn']");
        // 页面的js验证为两次密码不一致
        isTextPresent(be, getI18n("messages.31008"), true);
    }

    /**
     * 测试正常注册成功流程
     */
    @Test(dependsOnMethods = "testPasswordNotEqual")
    public void testSuccess() {
        String username = new RandomString(8).randomEmail();
        String password = "111111";
        be.refresh();
        be.type("//input[@id='userEmailInput']", username);
        be.type("//input[@id='passwordR']", password);
        be.type("//input[@id='repasswordR']", password);
        be.click("//input[@id='agree']");
        be.click("//button[@id='submitBtn']");
        // 成功
        isTextPresent(be, getI18n("suc_register"), true);

        // 使用注册成功用户登录
        be.pause(6000);
        isElementPresent(be, "//button[@id='signinBtn']", true);
        login(be, getCommonStr("base_url"), username, password);

        logout(be);

        // 管理员登录，删除这个测试注册用户
        Operations.login(be, getCommonStr("base_url"), getCommonStr("username_admin"), getCommonStr("password_admin"));
        // 点击用户管理
        be.click("//a[@id='ausers']");
        be.expectElementExistOrNot(true, String.format("//a[@id='del_%s']", username), 2000);
        be.click(String.format("//a[@id='del_%s']", username));
        be.expectElementExistOrNot(true, "//button[@id='deleteUserInfoBtn']", 1000);
        be.click("//button[@id='deleteUserInfoBtn']");
        // 确认已经删除了
        be.expectElementExistOrNot(false, String.format("//a[@id='del_%s']", username), 2000);
    }
}
