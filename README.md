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
