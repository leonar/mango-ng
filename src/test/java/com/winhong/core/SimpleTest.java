package com.winhong.core;

import com.winhong.core.base.BaseTest;
import com.winhong.dagger.BrowserEmulator;
import com.winhong.mango.CommonUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description of this file.
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/19
 */
public class SimpleTest {
    protected BrowserEmulator be;
    @Test
    public void testWebdriver() {
        be = new BrowserEmulator();
        be.open("http://www.baidu.com/");
        String frameinput = "//input[@id='su1']";
        System.out.println("*******************************************************************");
        System.out.println(be.getBrowserCore().findElementByXPath(frameinput).isDisplayed());
        System.out.println(be.getBrowserCore().findElementByXPath(frameinput).isEnabled());
        be.expectElementExistOrNot(true, frameinput, 2000);
        CommonUtil.executeJS(be, "window.open('http://www.google.com.hk', 'new_window')");
        String current = be.getBrowserCore().getWindowHandle();
        be.selectWindow("new_window");
        be.getBrowserCore().manage().window().maximize();
        System.out.println(be.getBrowserCore().getCurrentUrl());
        be.getBrowserCore().close();
        be.selectWindow(current);
        System.out.println(be.getBrowserCore().getCurrentUrl());
        System.out.println("*******************************************************************");
        be.quit();
    }

    public void testSimple() {
        System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        System.out.println();
    }
}
