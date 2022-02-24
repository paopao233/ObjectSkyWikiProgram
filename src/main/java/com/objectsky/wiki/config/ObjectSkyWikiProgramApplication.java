package com.objectsky.wiki.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

@ComponentScan(value = "com.objectsky") // 因为修改了启动了的路径 不加这个注解的话他扫描不了包
@SpringBootApplication
public class ObjectSkyWikiProgramApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectSkyWikiProgramApplication.class);

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ObjectSkyWikiProgramApplication.class);
        ConfigurableEnvironment environment = application.run(args).getEnvironment();
        LOG.info("启动成功！");
        LOG.info("地址：\thttp://127.0.0.1:{}", environment.getProperty("server.port"));
    }

}
