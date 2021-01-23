package com.joonsang.example.SpringbootUsingJPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.joonsang.example.SpringbootUsingJPA.repo",		// 스프링 부트라 생략 가능.
		repositoryImplementationPostfix = "Impl")

public class SpringbootUsingJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootUsingJpaApplication.class, args);
	}

}
