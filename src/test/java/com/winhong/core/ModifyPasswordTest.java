package com.winhong.core;

import com.winhong.core.base.BaseTest;
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
        String newPwd = getCustom("man.modifypwd", "new_password");
        login(be, getCommonStr("base_url"), USER_NAME, USER_PASSWORD);
        clickCheck(be, "//a[@id='af_accounts']", "//td[@id='apwdTD']");
        be.click("//td[@id='apwdTD']/a");
        be.type("//input[@name='password']", USER_PASSWORD);
        be.type("//input[@name='newPassword']", newPwd);
        be.type("//input[@name='newPasswordAgain']", newPwd);
        be.click("//button[@id='submitBtn']");
        // 更新成功
        isTextPresent(be, getMsg("messages.41008"), true);
        // 使用新密码登录
        login(be, getCommonStr("base_url"), USER_NAME, newPwd);
    }
}
