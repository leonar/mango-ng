## Wingarden平台的UI自动化测试框架 - 基于网易UI测试框架[Dagger](https://github.com/NetEase/Dagger)

公司的Paas产品Wingarden的web应用功能测试，地址： <http://mango.wingarden.net>

* Wiki: <https://github.com/yidao620c/mango-ng/wiki>
* Issues: <https://github.com/yidao620c/mango-ng/issues>
* Quick Start: <https://github.com/yidao620c/mango-ng/wiki/Quick-Start>

---------------------------------------------------
## 使用方法

* 先下载源代码
* 如果需要自动重试失败案例功能，还需要再下载[arrow的源代码](https://github.com/NetEase/Dagger)，然后deploy到maven私服上去
* 下载[chromedriver.exe](http://chromedriver.storage.googleapis.com/2.9/chromedriver_win32.zip) 和 [iedriver.exe](http://selenium.googlecode.com/files/IEDriverServer_Win32_2.39.0.zip)
* 将上面下载的driver解压后放到res文件夹下面，名称为chromedriver.exe和iedriver.exe
* 如果是Linux上面运行，下载chromedriver文件放在/usr/local/bin/目录下面，然后mvn clean test -DChromeDriverPath=/usr/local/bin/chromedriver
* 运行testng测试

## xpath一点笔记：
* //span[text()="webapps/"]/../.././span
* //bookstore/book[last()]
* /DocText/WithQuads/Page/Word[text()='July' and Quad/P1/@X > 90]
* record[field[@id='220' and @value='Red'] and field[@id='221' and @value='Large']]
* /Root//Person[contains(Blog,'cn') and contains(@ID,'01')]
* //tr[td[1][text()="hello"] and td[2][contains(text(), "512M")]]
* //td[text()="short_open_tag"]/following-sibling::td[1]
* //td[text()="short_open_tag"]/preceding-sibling::td[1]
* //td[starts-with(text(), "%s") and contains(text(), "disk:%sMB")]/following-sibling::td[2][contains(text(), "%s")]
* //a[text()='x222']/../following-sibling::td[8]/a[2]

## mango页面元素命名规约：
1. html非动态生成的元素尽量都加上id，这样定位会非常快，也不会出错
2. 页面元素最好用标准化html元素，除非必要不要复杂的CSS/JS技术实现效果
3. window.open('xxx', 'windowName') 打开新窗口，最好指定名字
4. 凡是能产生action的元素都应该有id，如果是循环产生的元素，对于每个可产生action的元素都应有id
5. 对于循环ID应该利用循环的每个值生成相应的ID，比如服务列表中购买服务的链接：id="@se.getName()_@se.getVendor()_@se.getVersion()"

-----------------------------------------------------
## How to Contribute

You are welcome to contribute to mango-test as follow

* add/edit wiki
* report/fix issue
* code review
* commit new feature
* add testcase

Meanwhile you'd better follow the rules below

* It's *NOT* recommended to submit a pull request directly to `master` branch. `develop` branch is more appropriate
* Follow common Java coding conventions
* Put all Java class files under *com.netease* package
* Add the following [license](#license) in each Java class file

## License

(The Apache License)

Copyright (c) 2013-2014 [WinHong, Inc.](http://www.winhong.com/) and other contributors

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
