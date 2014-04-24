package com.winhong.mango;

/**
 * 应用上传配置类
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/18
 */
public class AppConfig {
    /**
     * 测试用例描述
     */
    private String description;
    /**
     * 设置file的可见性js
     */
    private String fileVisiable;
    /**
     * 购买的服务名
     */
    private String buyService;
    /**
     * 购买的服务类型_版本
     */
    private String vendorVersion;
    /**
     * 上传文件名
     */
    private String uploadFilename;
    /**
     * 文件上传类型：0表示本地文件，1表示远程文件
     */
    private int filetype;
    /**
     * 测试的应用名
     */
    private String appname;
    /**
     * 部署实例数
     */
    private int instanceNum;
    /**
     * 测试应用的首页包含内容
     */
    private String bodyContent;
    /**
     * 运行语言
     */
    private String runtimes;
    /**
     * 使用框架
     */
    private String framework;
    /**
     * 内存
     */
    private String memory;
    /**
     * 修改实例数
     */
    private int modifyInstanceNum;
    /**
     * 修改内存
     */
    private String modifyMemory;
    /**
     * 修改uri
     */
    private String modifyUri;

    /**
     * 获取 使用框架.
     *
     * @return 使用框架.
     */
    public String getFramework() {
        return framework;
    }

    /**
     * 设置 设置file的可见性js.
     *
     * @param fileVisiable 设置file的可见性js.
     */
    public void setFileVisiable(String fileVisiable) {
        this.fileVisiable = fileVisiable;
    }

    /**
     * 设置 内存.
     *
     * @param memory 内存.
     */
    public void setMemory(String memory) {
        this.memory = memory;
    }

    /**
     * 获取 设置file的可见性js.
     *
     * @return 设置file的可见性js.
     */
    public String getFileVisiable() {
        return fileVisiable;
    }

    /**
     * 获取 部署实例数.
     *
     * @return 部署实例数.
     */
    public int getInstanceNum() {
        return instanceNum;
    }

    /**
     * 设置 测试应用的首页包含内容.
     *
     * @param bodyContent 测试应用的首页包含内容.
     */
    public void setBodyContent(String bodyContent) {
        this.bodyContent = bodyContent;
    }

    /**
     * 获取 测试应用的首页包含内容.
     *
     * @return 测试应用的首页包含内容.
     */
    public String getBodyContent() {
        return bodyContent;
    }

    /**
     * 设置 上传文件名.
     *
     * @param uploadFilename 上传文件名.
     */
    public void setUploadFilename(String uploadFilename) {
        this.uploadFilename = uploadFilename;
    }

    /**
     * 获取 购买的服务名.
     *
     * @return 购买的服务名.
     */
    public String getBuyService() {
        return buyService;
    }

    /**
     * 设置 购买的服务类型_版本.
     *
     * @param vendorVersion 购买的服务类型_版本.
     */
    public void setVendorVersion(String vendorVersion) {
        this.vendorVersion = vendorVersion;
    }

    /**
     * 获取 内存.
     *
     * @return 内存.
     */
    public String getMemory() {
        return memory;
    }

    /**
     * 设置 文件上传类型：0表示本地文件，1表示远程文件.
     *
     * @param filetype 文件上传类型：0表示本地文件，1表示远程文件.
     */
    public void setFiletype(int filetype) {
        this.filetype = filetype;
    }

    /**
     * 获取 购买的服务类型_版本.
     *
     * @return 购买的服务类型_版本.
     */
    public String getVendorVersion() {
        return vendorVersion;
    }

    /**
     * 设置 部署实例数.
     *
     * @param instanceNum 部署实例数.
     */
    public void setInstanceNum(int instanceNum) {
        this.instanceNum = instanceNum;
    }

    /**
     * 设置 运行语言.
     *
     * @param runtimes 运行语言.
     */
    public void setRuntimes(String runtimes) {
        this.runtimes = runtimes;
    }

    /**
     * 获取 测试的应用名.
     *
     * @return 测试的应用名.
     */
    public String getAppname() {
        return appname;
    }

    /**
     * 设置 测试的应用名.
     *
     * @param appname 测试的应用名.
     */
    public void setAppname(String appname) {
        this.appname = appname;
    }

    /**
     * 获取 测试用例描述.
     *
     * @return 测试用例描述.
     */
    public String getDescription() {
        return description;
    }

    /**
     * 获取 上传文件名.
     *
     * @return 上传文件名.
     */
    public String getUploadFilename() {
        return uploadFilename;
    }

    /**
     * 设置 购买的服务名.
     *
     * @param buyService 购买的服务名.
     */
    public void setBuyService(String buyService) {
        this.buyService = buyService;
    }

    /**
     * 获取 运行语言.
     *
     * @return 运行语言.
     */
    public String getRuntimes() {
        return runtimes;
    }

    /**
     * 设置 使用框架.
     *
     * @param framework 使用框架.
     */
    public void setFramework(String framework) {
        this.framework = framework;
    }

    /**
     * 设置 测试用例描述.
     *
     * @param description 测试用例描述.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取 文件上传类型：0表示本地文件，1表示远程文件.
     *
     * @return 文件上传类型：0表示本地文件，1表示远程文件.
     */
    public int getFiletype() {
        return filetype;
    }

    /**
     * 设置 修改实例数.
     *
     * @param modifyInstanceNum 修改实例数.
     */
    public void setModifyInstanceNum(int modifyInstanceNum) {
        this.modifyInstanceNum = modifyInstanceNum;
    }

    /**
     * 设置 修改内存.
     *
     * @param modifyMemory 修改内存.
     */
    public void setModifyMemory(String modifyMemory) {
        this.modifyMemory = modifyMemory;
    }

    /**
     * 获取 修改内存.
     *
     * @return 修改内存.
     */
    public String getModifyMemory() {
        return modifyMemory;
    }

    /**
     * 获取 修改uri.
     *
     * @return 修改uri.
     */
    public String getModifyUri() {
        return modifyUri;
    }

    /**
     * 设置 修改uri.
     *
     * @param modifyUri 修改uri.
     */
    public void setModifyUri(String modifyUri) {
        this.modifyUri = modifyUri;
    }

    /**
     * 获取 修改实例数.
     *
     * @return 修改实例数.
     */
    public int getModifyInstanceNum() {
        return modifyInstanceNum;
    }
}
