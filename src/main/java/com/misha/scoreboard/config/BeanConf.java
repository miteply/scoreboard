package com.misha.scoreboard.config;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.github.javafaker.Faker;

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
	public Faker faker() {
		return new Faker(new Locale("en-US"));
	}

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
	
	/**
	 * Server adds the unique hashcode (ETag) in response header when the client performs
	 * request to get data. It'is helpful to avoid the concurrency during the
	 * updating. Before update data in database, the client must perform the get
	 * request, passing in request header the ETag returned by the server befor to be sure
	 * that data is not modified.
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
		FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<>(
				new ShallowEtagHeaderFilter());
		filterRegistrationBean.addUrlPatterns("/api/events/*");
		filterRegistrationBean.setName("ETag");
		return filterRegistrationBean;
	}

}
