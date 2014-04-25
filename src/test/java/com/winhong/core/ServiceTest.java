package com.winhong.core;

import com.winhong.core.base.BaseTest;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.*;

import static com.winhong.mango.CommonUtil.*;
import static com.winhong.mango.YamlUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * 购买/修改/删除服务
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/18
 */
public class ServiceTest extends BaseTest {
    private static Logger logger = Logger.getLogger(ServiceTest.class);
    /**
     * 购买/修改/删除服务
     */
    @Test
    public void testServiceOperation() {
        List<Map<String, String>> config = getMapList("man.service");
        // 登录
        login(be, getCommonStr("base_url"), USER_NAME, USER_PASSWORD);
        clickCheck(be, "//a[@id='af_services']", "//button[@id='slBuyBtn']");
        for (Map<String, String> eachConfig : config) {
            clickCheck(be, "//button[@id='slBuyBtn']", "//button[@id='sbBuyBtn']");
            be.click(String.format("//a[contains(@onclick, '%s')]", eachConfig.get("set_meal")));
            be.expectElementExistOrNot(true, "//a[starts-with(@onclick, 'removeplan')]", 2000);
            be.click("//button[@id='sbBuyBtn']");
            be.expectElementExistOrNot(true, String.format(
                    "//a[text()='%s']", getI18n("man_service_list_viewdetail")), 30000);
            clickCheck(be, String.format("//a[text()='%s']", getI18n("man_service_list_viewdetail")),
                    "//button[@id='sdModifyBtn']");
            clickCheck(be, "//button[@id='sdModifyBtn']", "//button[@id='seSaveBtn']");
            String path = String.format("//span[starts-with(text(), '%s')]/preceding-sibling::input[1]",
                    eachConfig.get("new_set_meal"));
            be.click(path);
            be.click("//button[@id='seSaveBtn']");
            be.expectElementExistOrNot(true, "//button[@id='sdDeleteBtn']", 10000);
            // 检查是否修改成功
            be.expectElementExistOrNot(true, String.format(
                    "//td[@class='valueField' and starts-with(text(), '%s')]", eachConfig.get("new_set_meal")), 1000);
            clickCheck(be, "//button[@id='sdDeleteBtn']", "//button[@id='sdConfirmBtn']");
            be.click("//button[@id='sdConfirmBtn']");
            be.expectElementExistOrNot(true, "//div[@id='slNothing']", 5000);
        }
    }
}
