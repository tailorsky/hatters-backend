package com.hatterscraft.hatters_backend;

import org.springframework.boot.SpringApplication;

public class TestHattersBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(HattersBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
