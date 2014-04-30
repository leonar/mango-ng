package com.winhong.core;

import com.winhong.core.base.BaseTest;
import com.winhong.mango.CommonUtil;
import com.winhong.mango.RandomString;
import org.testng.annotations.Test;

import static com.winhong.mango.CommonUtil.*;
import static com.winhong.mango.YamlUtil.*;

/**
 * 修改密码测试
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/18
 */
public class ModifyPasswordTest extends BaseTest {
    /**
     * 测试更改密码
     */
    @Test
    public void testUpdatePwd() {
        //先注册一个新用户
        String username = new RandomString(7).randomEmail();
        String password = "111111";
        openHome(be, getCommonStr("base_url"));
        clickCheck(be, "//a[@id='registLink']", "//legend[@id='welRegister']");
        be.type("//input[@id='userEmailInput']", username);
        be.type("//input[@id='passwordR']", password);
        be.type("//input[@id='repasswordR']", password);
        be.click("//input[@id='agree']");
        be.click("//button[@id='submitBtn']");
        // 成功
        isTextPresent(be, getI18n("suc_register"), true);

        // 使用注册成功用户登录后修改密码
        be.pause(6000);
        String newPwd = getCustom("man.modifypwd", "new_password");
        login(be, getCommonStr("base_url"), username, password);
        clickCheck(be, "//a[@id='af_accounts']", "//td[@id='apwdTD']");
        be.click("//td[@id='apwdTD']/a");
        be.type("//input[@name='password']", password);
        be.type("//input[@name='newPassword']", newPwd);
        be.type("//input[@name='newPasswordAgain']", newPwd);
        be.click("//button[@id='submitBtn']");
        // 更新成功
        isTextPresent(be, getI18n("messages.41008"), true);
        logout(be);
        be.pause(500);
        // 使用新密码登录
        login(be, getCommonStr("base_url"), username, newPwd);

        logout(be);
        // 管理员登录，删除这个测试注册用户
        CommonUtil.userLogin(be, true);
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
