package com.winhong.core;

import com.winhong.core.base.BaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.winhong.mango.Operations.*;
import static com.winhong.mango.YamlUtil.*;
import static com.winhong.mango.Arith.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * 账单测试
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/18
 */
public class BillingTest extends BaseTest {
    private static Logger logger = Logger.getLogger(BillingTest.class);
    private String filekey = "man.billing";

    /**
     * 检查应用和服务的账单数据
     */
    @Test
    public void testAppServiceBilling() {
        // 登录
        login(be, getCommonStr("base_url"), getCustom(filekey, "billing_user"),
                getCustom(filekey, "billing_password"));
        be.click("//a[@id='af_billings']");
        if (be.isElementPresent("//table[@id='billingListTable']", 1000)) {
            double cal_total = 0.00;
            // 应用的计费检查，对每一行数据集， 天	* 单价(元/天)	= 小计 要成立
            // 除了最后一行的月总计外 ，其他都要做检查
            if (be.isElementPresent("//table[@id='appBilllingsTable']", 1000)) {
                RemoteWebDriver driver = be.getBrowserCore();
                List<WebElement> app_rows = driver.findElementsByXPath("//table[@id=\"appBilllingsTable\"]/tbody/tr");
                double app_total = 0.00;
                for (int i = 0, size = app_rows.size() - 1; i < size; i++) {
                    double day = Double.parseDouble(be.getText(String.format(
                            "//table[@id=\"appBilllingsTable\"]/tbody/tr[%s]/td[last()-2]", i)));
                    double dayprice = Double.parseDouble(be.getText(String.format(
                            "//table[@id=\"appBilllingsTable\"]/tbody/tr[%s]/td[last()-1]", i)));
                    double smalltotal = Double.parseDouble(be.getText(String.format(
                            "//table[@id=\"appBilllingsTable\"]/tbody/tr[%s]/td[last()]", i)));
                    assertThat(compare(mul(day, dayprice), smalltotal), is(0));
                    app_total = add(app_total, smalltotal);
                }

                // 当月总计
                double page_app_total = Double.parseDouble(be.getText(String.format(
                        "//table[@id=\"appBilllingsTable\"]/tbody/tr[%s]/td[last()]", app_rows.size())));

                assertThat(compare(app_total, page_app_total), is(0));
                logger.info("应用总计:" + app_total);
                cal_total = add(cal_total, app_total);
            }

            // 服务的计费检查，对每一行数据集， 天	* 单价(元/天)	= 小计 要成立
            // 除了最后一行的月总计外 ，其他都要做检查
            if (be.isElementPresent("//table[@id='appBilllingsTable']", 1000)) {
                RemoteWebDriver driver = be.getBrowserCore();
                List<WebElement> service_rows = driver.findElementsByXPath("//table[@id=\"serviceBillingTable\"]/tbody/tr");
                double service_total = 0.00;
                for (int i = 0, size = service_rows.size() - 1; i < size; i++) {
                    double day = Double.parseDouble(be.getText(String.format(
                            "//table[@id=\"serviceBillingTable\"]/tbody/tr[%s]/td[last()-2]", i)));
                    double dayprice = Double.parseDouble(be.getText(String.format(
                            "//table[@id=\"serviceBillingTable\"]/tbody/tr[%s]/td[last()-1]", i)));
                    double smalltotal = Double.parseDouble(be.getText(String.format(
                            "//table[@id=\"serviceBillingTable\"]/tbody/tr[%s]/td[last()]", i)));
                    assertThat(compare(mul(day, dayprice), smalltotal), is(0));
                    service_total = add(service_total, smalltotal);
                }

                // 当月总计
                double page_service_total = Double.parseDouble(be.getText(String.format(
                        "//table[@id=\"serviceBillingTable\"]/tbody/tr[%s]/td[last()]", service_rows.size())));

                assertThat(compare(page_service_total, page_service_total), is(0));
                logger.info("服务总计:" + service_total);
                cal_total = add(cal_total, service_total);
            }
            cal_total = round(cal_total, 2);
            // 应用总计+服务总计
            logger.info("应用总计+服务总计:" + cal_total);
        }
    }

    /**
     * 检查历史账单数据
     */
    @Test(dependsOnMethods = "testAppServiceBilling")
    public void testHistoryBilling() {
        // 上一个月
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        String lastMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
        // 需要测试的历史月份账单统计
        double his_m_bill = 0.00;
        String xp = String.format(
                "//table[@id='billingListTable']/tbody/tr/td[1][starts-with(text(), '%s')]", lastMonth);
        if (be.isElementPresent(xp, 2000)) {
            his_m_bill = Double.parseDouble(be.getText(xp));
        }
        // 滚动条到页面底部
        executeJS(be, getCommonStr("scroll_bottom"));
        executeJS(be, String.format(getCustom(filekey, "input_date_js"), lastMonth));
        be.pause(500);
        be.click("//button[@id='billingQueryBtn']");
        RemoteWebDriver driver = be.getBrowserCore();
        if (be.isElementPresent("//table[@id='billDetailTable']", 5000)) {
            int allrows = driver.findElementsByXPath("//table[@id='billDetailTable']/tbody/tr").size();
            double cal_total = 0.00;
            double page_total = 0.00;
            for (int i = 1; i <= allrows; i++) {
                int each_rd_num = driver.findElementsByXPath(
                        String.format("//table[@id='billDetailTable']/tbody/tr[%s]/td", i)).size();
                if (each_rd_num > 3) {
                    double day = Double.parseDouble(be.getText(String.format(
                            "//table[@id=\"billDetailTable\"]/tbody/tr[%s]/td[last()-2]", i)));
                    double dayprice = Double.parseDouble(be.getText(String.format(
                            "//table[@id=\"billDetailTable\"]/tbody/tr[%s]/td[last()-1]", i)));
                    double smalltotal = Double.parseDouble(be.getText(String.format(
                            "//table[@id=\"billDetailTable\"]/tbody/tr[%s]/td[last()]", i)));
                    // logger.info("day * dayprice = " + round(mul(day, dayprice), 2));
                    // logger.info("smalltotal = " + smalltotal);
                    assertThat(compare(round(mul(day, dayprice), 2), smalltotal), is(0));
                    cal_total = add(cal_total, smalltotal);
                } else if (each_rd_num == 3) {
                    page_total = Double.parseDouble(be.getText(String.format(
                            "//table[@id='billDetailTable']/tbody/tr[%s]/td[last()]/strong", i)));
                }
            }
            cal_total = round(cal_total, 2);
            System.out.println("history_bill: page_total=" + page_total);
            System.out.println("history_bill: cal_total=" + cal_total);
            assertThat(compare(page_total, cal_total), is(0));
            if (his_m_bill > 0) {
                assertThat(compare(his_m_bill, cal_total), is(0));
            }
        }
    }
}
