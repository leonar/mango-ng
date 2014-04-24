package com.winhong.core;

import com.winhong.core.base.BaseTest;
import org.testng.annotations.Test;

import static com.winhong.mango.YamlUtil.*;
import static com.winhong.mango.CommonUtil.*;

/**
 * 登录测试类
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/17
 */
public class LoginTest extends BaseTest{
    @Test
    public void testLogin() {
        login(be, getCommonStr("base_url"), USER_NAME, USER_PASSWORD);
    }
}
