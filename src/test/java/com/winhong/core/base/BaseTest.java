package com.winhong.core.base;

import com.winhong.dagger.BrowserEmulator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * 测试基类
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/18
 */
public class BaseTest {
    protected BrowserEmulator be;
    @BeforeClass
    public void beforeclass() {
        be = new BrowserEmulator();
    }
    @AfterClass
    public void afterclass() {
        be.quit();
    }
}
