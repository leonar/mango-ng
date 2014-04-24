package com.winhong.core;

import com.winhong.core.base.BaseTest;
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
public class SimpleTest{
    @Test
    public void testSimple() {
//        be.open("file:///D:/work/head.html");
//        String frameinput = "//input[@id='ss']";
//        System.out.println("*******************************************************************");
//        System.out.println(be.getBrowserCore().findElementByXPath(frameinput).isDisplayed());
//        System.out.println(be.getBrowserCore().findElementByXPath(frameinput).isEnabled());
//        be.expectElementExistOrNot(true, frameinput, 2000);
//        System.out.println("*******************************************************************");

        System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        System.out.println();
    }
}
