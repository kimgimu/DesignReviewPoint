package com.kimu.dichamsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		exclude = {
				org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
		}
)
public class DichamsiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DichamsiApplication.class, args);
	}


}
