package com.winhong.core;

import com.winhong.core.base.BaseTest;
import com.winhong.mango.CommonUtil;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import static com.winhong.mango.Operations.*;
import static com.winhong.mango.YamlUtil.*;
import static org.hamcrest.Matchers.not;

/**
 * 大文件上传测试
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/17
 */
public class BigFileUploadTest extends BaseTest {
    private static Logger logger = Logger.getLogger(BigFileUploadTest.class);
    private String zipPath;
    private Map config;

    @BeforeMethod
    public void beforeMethod() {
        zipPath = CommonUtil.getDirPath("zips/");
        config = getMapData("man.uploadfile");
    }

    /**
     * 测试大于50M的大文件上传
     */
    @Test
    public void testUploadBigFile() {
        // 登录
        login(be, getCommonStr("base_url"), USER_NAME, USER_PASSWORD);
        String bigFile = (String) config.get("big_file");
        logger.info("bigFile full path = " + zipPath + bigFile);
        // 先确认文件大小大于50M
        File file = new File(zipPath + bigFile);
        assertThat(file.length(), greaterThan(50 * 1024 * 1024L));
        // 点击文件管理
        be.click("//a[@id='uploadFileLink']");
        // 切换到新窗口
        swithLastWindow(be);
        uploadFile(bigFile);
    }

    /**
     * 测试不同文件类型，zip类型和war类型
     */
    @Test(dependsOnMethods = "testUploadBigFile")
    public void testUploadType() {
        uploadFile((String) config.get("war_file"));
        uploadFile((String) config.get("zip_file"));
    }

    /**
     * 断点续传
     */
    @Test(dependsOnMethods = "testUploadType")
    public void testResumeUpload() {
        String resumeFile = (String) config.get("zip_big_file");
        // 先确认文件不存在
        be.expectElementExistOrNot(false, String.format("//td[text()='%s']", resumeFile), 1000);
        isElementPresent(be, "//button[@id='largeUploadBtn']", true);
        clickCheck(be, "//button[@id='largeUploadBtn']", "//span[@id='error_tips']");
        executeJS(be, (String) config.get("file_visiable"));
        be.pause(1000);
        isElementPresent(be, "//input[@id='fileChoose']", true);
        be.type("//input[@id='fileChoose']", zipPath + resumeFile);
        be.pause(500);
        be.click("//a[@id='uploadButton']");
        // 等待1秒钟
        be.pause(1000);
        // 点击取消按钮
        be.click(String.format("//a[text()='%s']", getI18n("man_large_file_upload_cancle_button")));
        // 先check下还没传完的文件是否存在
        be.expectElementExistOrNot(true, String.format("//td[text()='%s']", resumeFile), 30000);
        // 然后check已上传和总大小不相等
        String ft =getI18n("man_large_file_upload_table_name");
        be.expectElementExistOrNot(
                true, String.format("//td[@data-title='%s' and text()='%s']", ft, resumeFile), 2000);
        // 文件大小
        String fsize = be.getText(String.format("//td[@data-title='%s' and text()='%s']/following-sibling::td[1]", ft, resumeFile));
        // 已上传
        String upsize = be.getText(String.format("//td[@data-title='%s' and text()='%s']/following-sibling::td[2]", ft, resumeFile));
        // 两个应该不相等
        assertThat(fsize, not(upsize));
        String continuePath = String.format("//td[@data-title='%s' and text()='%s']/following-sibling::td[6]/a[2]", ft, resumeFile);
        be.expectElementExistOrNot(true, continuePath, 1000);
        assertThat(be.getText(continuePath), is(getI18n("man_large_file_upload_table_operation_upload")));
        // 开始断点续传
        be.click(continuePath);
        executeJS(be, (String) config.get("file_visiable"));
        be.pause(1000);
        isElementPresent(be, "//input[@id='fileChoose']", true);
        be.type("//input[@id='fileChoose']", zipPath + resumeFile);
        be.pause(500);
        be.click("//a[@id='uploadButton']");
        // 先等待直到上传弹出框消失
        be.waitUntilDisappear("//span[@id='error_tips']", 30000);
        be.pause(500);
        // 确认文件已经上传了
        be.expectElementExistOrNot(true, String.format("//td[text()='%s']", resumeFile), 1000);
        // 文件大小
        fsize = be.getText(String.format("//td[@data-title='%s' and text()='%s']/following-sibling::td[1]", ft, resumeFile));
        // 已上传
        upsize = be.getText(String.format("//td[@data-title='%s' and text()='%s']/following-sibling::td[2]", ft, resumeFile));
        // 这时候两个应该相等
        assertThat(fsize, is(upsize));
        continuePath = String.format("//td[@data-title='%s' and text()='%s']/following-sibling::td[6]/a[2]", ft, resumeFile);
        // 续传链接应该不见了
        be.expectElementExistOrNot(false, continuePath, 1000);
        // 每次上传成功后删除之
        be.click(String.format("//a[text()='%s']", getI18n("man_large_file_upload_table_operation_delete")));
        be.expectElementExistOrNot(true, "//button[@onclick='deleteFile();']", 2000);
        be.click("//button[@onclick='deleteFile();']");
        // 确认已经删除了
        be.expectElementExistOrNot(false, "//button[@onclick='deleteFile();']", 2000);
    }

    private void uploadFile(String filename) {
        // 先确认文件不存在
        be.expectElementExistOrNot(false, String.format("//td[text()='%s']", filename), 1000);
        isElementPresent(be, "//button[@id='largeUploadBtn']", true);
        clickCheck(be, "//button[@id='largeUploadBtn']", "//span[@id='error_tips']");
        executeJS(be, (String) config.get("file_visiable"));
        be.pause(1000);
        isElementPresent(be, "//input[@id='fileChoose']", true);
        be.type("//input[@id='fileChoose']", zipPath + filename);
        be.pause(500);
        be.click("//a[@id='uploadButton']");
        // 先等待直到上传弹出框消失
        be.waitUntilDisappear("//span[@id='error_tips']", 30000);
        be.pause(500);
        // 确认文件已经上传了
        be.expectElementExistOrNot(true, String.format("//td[text()='%s']", filename), 1000);
        // 每次上传成功后删除之
        be.click(String.format("//a[text()='%s']", getI18n("man_large_file_upload_table_operation_delete")));
        be.expectElementExistOrNot(true, "//button[@onclick='deleteFile();']", 2000);
        be.click("//button[@onclick='deleteFile();']");
        // 确认已经删除了
        be.expectElementExistOrNot(false, "//button[@onclick='deleteFile();']", 2000);
    }
 }
