package com.thinxz;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 服务入口[提供公共服务调用, 及 JAR 包直接引用服务]
 *
 * @author thinxz
 */
@SpringBootApplication
public class ThinApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext run =
                new SpringApplicationBuilder(ThinApp.class)
                        .addCommandLineProperties(false)
                        .listeners(new ApplicationPidFileWriter("./app.pid"))
                        .run(args);
    }
}
