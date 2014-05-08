package com.winhong.core;

import com.winhong.core.base.BaseTest;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import static com.winhong.mango.Operations.clickCheck;
import static com.winhong.mango.Operations.login;
import static com.winhong.mango.YamlUtil.*;

/**
 * Svn服务
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/18
 */
public class SvnTest extends BaseTest {
    private static Logger logger = Logger.getLogger(SvnTest.class);
    /**
     * Svn服务的创建
     */
    @Test
    public void testCreateSvn() {
        // 登录
        login(be, getCommonStr("base_url"), USER_NAME, USER_PASSWORD);
        clickCheck(be, "//a[@id='af_accounts']", "//td[@id='svninfoLabel']");
        be.expectElementExistOrNot(true, "//button[@id='svnInitBtn']", 1000);
        be.click("//button[@id='svnInitBtn']");
        be.expectElementExistOrNot(true, "//a[@id='getsvnLink']", 3000);
        String svnInfo = be.getText("//td[@id='svninfoLabel']/.././td[2]");
        String msgUrl = getI18n("man_account_svn_url");
        String msgName = getI18n("man_account_svn_user");
        String msgPwd = getI18n("man_account_svn_password");
        String svnName = svnInfo.split(msgUrl)[0].split(msgPwd)[0].split(msgName + ": ")[1].trim();
        String svnPwd = svnInfo.split(msgUrl)[0].split(msgPwd)[1].split(": ")[1].trim();
        logger.info("svn name=" + svnName);
        logger.info("svn pwd=" + svnPwd);
        String svnLink = be.getAttribute("//a[@id='getsvnLink']", "href");
        // 认证的svn地址
        String authUrl = "http://" + svnName + ":" + svnPwd + "@" + svnLink.split("http://")[1];
        logger.info("full svn auth url=" + authUrl);
        be.open(authUrl);
        be.expectElementExistOrNot(true, "//a[@href='http://subversion.apache.org/']", 3000);
    }
}
