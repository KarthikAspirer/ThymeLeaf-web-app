package com.java.Themeleaf;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
public class ThemeleafWebAppApplication {

	public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ThemeleafWebAppApplication.class);
        SpringApplication.run(ThemeleafWebAppApplication.class, args);
        logger.info("Application running");
	//ConfigurableApplicationContext ctxt = SpringApplication.run(ThemeleafWebAppApplication.class, args);
	}

}
