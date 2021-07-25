package com.misha.scoreboard.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Class that configures the Application and returns the bean objects.
 * 
 * @author Mykhaylo.T
 *
 */
@Configuration
@EnableAsync
@EnableJpaAuditing
public class BeanConf {

	@Bean
	public SseEmitter sseEmitter() {
		return new SseEmitter();
	}

	@Bean
	public ExecutorService executorService() {
		return Executors.newCachedThreadPool();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
