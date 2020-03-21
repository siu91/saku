package org.siu.saku;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类 @SpringBootApplication
 * 启用swagger @EnableSwagger2
 * 在启动类中添加对 Mapper 包扫描 @MapperScan，或者直接在 Mapper 类上面添加注解 @
 * @EnableJpaAuditing 实现DO属性自动填充
 *
 * 作为 provider 启动
 *
 *
 * @Author Siu
 */

@SpringBootApplication
public class SakuApplication {

    public static void main(String[] args) {
        SpringApplication.run(SakuApplication.class, args);
    }

}
