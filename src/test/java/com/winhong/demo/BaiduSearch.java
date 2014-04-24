/*
 * Copyright (c) 2012-2013 NetEase, Inc. and other contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.winhong.demo;

import com.winhong.dagger.BrowserEmulator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * The demo to show Dagger's basic usage
 * @author ChenKan
 */
public class BaiduSearch {

    BrowserEmulator be;

    @BeforeClass
    public void doBeforeClass() throws Exception {
        be = new BrowserEmulator();
    }

    @Test
    public void doTest() {
        String googleUrl = "http://www.baidu.com/";
        String searchBox = "//input[@id='kw1']";
        String searchBtn = "//input[@id='su1']";

        be.open(googleUrl);
        be.type(searchBox, "github");
        be.click(searchBtn);
        be.expectTextExistOrNot(true, "GitHub · Build software better, together.", 5000);
    }

    @AfterClass(alwaysRun = true)
    public void doAfterClass() {
        be.quit();
    }
}
