package com.winhong.mango;

import com.winhong.dagger.BrowserEmulator;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import static com.winhong.mango.YamlUtil.USER_NAME;
import static com.winhong.mango.YamlUtil.USER_PASSWORD;
import static com.winhong.mango.YamlUtil.getCommonStr;

import java.util.Set;

/**
 * 公共的一些操作
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/17
 */
public class Operations {

    public static void openHome(BrowserEmulator be, String baseurl) {
        openCheck(be, baseurl, "//div[@id='msgSignIn']");
        maximizeWindow(be); // 最大化窗口
    }

    /**
     * 登录
     * @param be c
     * @param baseurl 登录地址
     * @param username 用户名
     * @param password 密码
     */
    public static void login(BrowserEmulator be,
                             String baseurl, String username, String password) {
        openHome(be, baseurl);
        be.type("//input[@id='email']", username);
        be.type("//input[@id='password']", password);
        // 如果有退出的链接，表明登录成功了
        clickCheck(be, "//button[@id='signinBtn']", "//a[@id='logoutLink']");
    }

    /**
     * 用户登录
     * @param be
     * @param isAdmin 是否管理员
     */
    public static void userLogin(BrowserEmulator be, boolean isAdmin) {
        if (isAdmin) {
            login(be, getCommonStr("base_url"),
                    getCommonStr("username_admin"), getCommonStr("password_admin"));
        } else {
            login(be, getCommonStr("base_url"), USER_NAME, USER_PASSWORD);
        }
    }

    /**
     * 退出
     * @param be logoutLink
     */
    public static void logout(BrowserEmulator be) {
        clickCheck(be, "//a[@id='logoutLink']", "//div[@id='msgSignIn']");
    }

    /**
     * 打开网页
     *
     * @param be
     */
    public static void openCheck(BrowserEmulator be , String url, String checkXpath) {
        be.open(url);
        be.expectElementExistOrNot(true, checkXpath, 5000);
    }

    /**
     * 最大化窗口
     * @param be
     */
    public static void maximizeWindow(BrowserEmulator be) {
        be.getBrowserCore().manage().window().maximize();
    }

    /**
     * 清空
     * @param be
     * @param xpath
     */
    public static void clear(BrowserEmulator be, String xpath) {
        be.getBrowserCore().findElementByXPath(xpath).clear();
    }

    /**
     * 点击Button按钮/Link链接并且验证
     *
     * @param be
     */
    public static void clickCheck(BrowserEmulator be, String xpath, String checkXpath) {
        be.click(xpath);
        be.expectElementExistOrNot(true, checkXpath, 5000);
    }

    /**
     * mouseOver
     *
     * @param be
     */
    public static void mouseOver(BrowserEmulator be, String xpath, String checkXpath) {
        be.mouseOver(xpath);
        be.expectElementExistOrNot(true, checkXpath, 5000);
    }

    /**
     * 判断页面上文本可见不可见
     *
     * @param be
     * @param text
     */
    public static void isTextPresent(BrowserEmulator be, String text, boolean expect) {
        if (expect) {
            if (!be.isTextPresent(text, 3000)) {
                Assert.fail();
            }
        } else {
            if (be.isTextPresent(text, 3000)) {
                Assert.fail();
            }
        }
    }

    /**
     * 判断元素在页面可见不可见
     *
     * @param be
     * @param xpath
     * @param expect 可见/不可见
     */
    public static void isElementPresent(BrowserEmulator be, String xpath, boolean expect) {
        if (expect) {
            if (!be.isElementPresent(xpath, 3000)) {
                Assert.fail();
            }
        } else {
            if (be.isElementPresent(xpath, 3000)) {
                Assert.fail();
            }
        }
    }

    /**
     * 不断刷新页面直到期待某个元素存在，或者超时结束
     * @param be
     * @param xpath
     * @param timeout
     * @param expect
     */
    public static void refreshUntilElementPresent(BrowserEmulator be, String xpath, int timeout) {
        long start = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - start > timeout) {
                Assert.fail();
                break;
            }
            if (be.isElementPresent(xpath, 500)) {
                break;
            } else {
                // 等待6秒后刷新
                be.pause(6000);
                be.refresh();
            }
        }
    }

    /**
     * WebDriver执行JS
     *
     * @param be
     * @throws InterruptedException
     */
    public static void executeJS(BrowserEmulator be, String javascript) {
        be.getBrowserCore().executeScript(javascript);
        be.pause(3000);
    }

    /**
     * 跳转到新开的窗口（窗口没有title的情况）
     * @param be BrowserEmulator
     * @return 当前window的名字
     */
    public static String swithLastWindow(BrowserEmulator be) {
        RemoteWebDriver webDriver = be.getBrowserCore();
        String currentWindow = webDriver.getWindowHandle();
        Set<String> windows = webDriver.getWindowHandles();
        for (String w : windows) {
            if (!currentWindow.equals(w)) {
                be.selectWindow(w);
                break;
            }
        }
        return currentWindow;
    }
}
