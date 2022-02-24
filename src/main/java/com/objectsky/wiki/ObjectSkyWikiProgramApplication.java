package com.objectsky.wiki;

import com.mysql.cj.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

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