package io.github.shamrice.nechat.server.web.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Erik on 10/18/2017.
 */

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        System.out.println("############# STARTING NECHAT WEB SERVER ##############");

        SpringApplication.run(Application.class, args);

        System.out.println("############# RUN CALL FINISHED. APPLICATION SHOULD BE RUNNING ##############");
    }
}
