package com.joonsang.example.SpringbootUsingJPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;
import java.util.UUID;


@SpringBootApplication
@EnableJpaAuditing														// JpaAuditing
//@EnableJpaAuditing(modifyOnCreate = false) 							// 저장시점에 저장데이터만 입력하고 싶으면 사용
@EnableJpaRepositories(													// 스프링 부트라 생략 가능.
		basePackages = "com.joonsang.example.SpringbootUsingJPA.repo",
		repositoryImplementationPostfix = "Impl")
public class SpringbootUsingJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootUsingJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		// 실무에서는 세션 정보나, 스프링 시큐리티 로그인 정보에서 ID를 받음
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
