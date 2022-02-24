package com.objectsky.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname: TestController
 * @Description:
 * @Date: Create in 9:31 PM 2022/2/24
 * @Created By: JinleWeng
 * @Modified By:
 */
@RestController
public class TestController {


    @RequestMapping("/hello")
    public String hello(@Value("${test.name2:eee}")String name) {
        return "hello world," + name;
    }

    @PostMapping("/hello1")
    public String hello1(String name) {
        return "hello, " + name;
    }
}
