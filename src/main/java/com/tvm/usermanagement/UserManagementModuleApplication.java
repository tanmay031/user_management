package com.tvm.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class UserManagementModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementModuleApplication.class, args);
	}

}
