package com.web.kokoro.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class BackendApplication {

    public static void main(String[] args) {
//        //IOC容器
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
//        TestShow t = applicationContext.getBean(TestShow.class);
//        System.out.println(t.getTitle());
        SpringApplication.run(BackendApplication.class, args);
    }

}
