package com.project.geofencing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Application {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        log.debug("Geofencing application as started");
        SpringApplication.run(Application.class, args);
    }
}
