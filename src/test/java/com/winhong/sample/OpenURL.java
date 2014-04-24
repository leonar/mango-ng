package com.winhong.sample;

import com.winhong.dagger.BrowserEmulator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 打开网页
 * @author WeiYating
 */
public class OpenURL {

	BrowserEmulator be;

	@BeforeClass
	public void doBeforeClass() {
		be = new BrowserEmulator();
//		CommonFunction.openCaptain(Be);
	}

	@Test
	public void test() {
		CommonFunction.openURL(be);
	}

	@AfterClass(alwaysRun = true)
	public void doAfterClass() {
		be.quit();
	}
}
