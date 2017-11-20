package io.github.shamrice.nechat.webservice;

import io.github.shamrice.nechat.webservice.controller.AuthenticationController;
import io.github.shamrice.nechat.webservice.controller.BuddiesController;
import io.github.shamrice.nechat.webservice.controller.MessagesController;
import io.github.shamrice.nechat.webservice.controller.TestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Erik on 10/18/2017.
 */

@SpringBootApplication
/*
@ComponentScan(basePackageClasses = {
        AuthenticationController.class,
        BuddiesController.class,
        MessagesController.class,
        TestController.class })
        */
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {

        System.out.println("############# STARTING NECHAT WEB SERVER ##############");

        SpringApplication.run(Application.class, args);

        System.out.println("############# RUN CALL FINISHED. APPLICATION SHOULD BE RUNNING ##############");
    }
}
