
package com.uiuing.Back_end;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.uiuing.Back_end.dao") // 添加 @Mapper 注解
public class Application {
    public static void main(String[] args) {
        System.out.println("启动 Spring Boot...");
        System.setProperty("java.net.preferIPv4Stack", "true"); // IPV4
        SpringApplication.run(Application.class, args);
    }

}
