package com.winhong.demo;

import com.winhong.dagger.BrowserEmulator;
import com.winhong.imagecheck.ImageProcess;
import org.testng.annotations.Test;

/**
 * This demo shows how to use image contrast feature to check page style
 * Please see details at https://github.com/leonar/Dagger/wiki/Demo-for-Image-Contrast
 * @author LingFei
 */
public class CheckPageStyle {

    @Test
    public void doTest() throws Exception {
        // Change githubUrl to http://zhidao.baidu.com/ for contrast
        // Remember to change ScreenShotType from 1 to 2 in imagecheck.properties
//        String githubUrl = "http://www.baidu.com/";
        String githubUrl = "http://zhidao.baidu.com/";
        String logoXpath = "//div[@id='search-box']";
        String checkPoint = "logo";
        String folderName = "baidu";
        BrowserEmulator be = new BrowserEmulator();
        be.open(githubUrl);
        ImageProcess.process(be.getBrowserCore(), folderName, checkPoint, logoXpath);
        be.quit();
    }
	
}
