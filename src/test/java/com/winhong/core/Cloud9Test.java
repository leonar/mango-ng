package com.winhong.core;

import com.winhong.core.base.BaseTest;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import static com.winhong.mango.CommonUtil.clickCheck;
import static com.winhong.mango.CommonUtil.login;
import static com.winhong.mango.YamlUtil.*;

/**
 * Cloud9在线IDE服务
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/18
 */
public class Cloud9Test extends BaseTest {
    private static Logger logger = Logger.getLogger(Cloud9Test.class);
    /**
     * Cloud9在线IDE服务的创建
     */
    @Test
    public void testCreateCloud9() {
        // 登录
        login(be, getCommonStr("base_url"), USER_NAME, USER_PASSWORD);
        clickCheck(be, "//a[@id='af_accounts']", "//td[@id='svninfoLabel']");
        be.expectElementExistOrNot(true, "//button[@id='initIdeLabel']", 1000);
        be.click("//button[@id='initIdeLabel']");
        be.expectElementExistOrNot(true, "//a[@id='getideLink' and starts-with(text(), 'http')]", 3000);
        be.click("//a[@id='getideLink' and starts-with(text(), 'http')]");
        be.expectElementExistOrNot(true, "//a[@id='backToC9' and @href='http://c9.io/']", 3000);
    }
}
