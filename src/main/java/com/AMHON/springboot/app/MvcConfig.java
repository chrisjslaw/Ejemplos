package com.AMHON.springboot.app;

import java.nio.file.Paths;

import org.apache.catalina.filters.RemoteIpFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;


@EnableJpaAuditing//codigo para habilitar auditoria en las tablas
@Configuration
@EnableSpringConfigured
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class MvcConfig implements WebMvcConfigurer{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public void addViewControllers(ViewControllerRegistry registry) {
		
		
		registry.addViewController("/error_403").setViewName("error_403");
		//registry.addViewController("/landing").setViewName("landing");
	}
	
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub		
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		log.info(resourcePath);
		
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations(resourcePath) //se agrega resourcePath en lugar de "file:/C:/temp/uploads/" para directorio absoluto y externo en la raiz del proyecto
		.setCachePeriod(0);//poniendole tiempo al cache
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	 @Bean
	    public RemoteIpFilter remoteIpFilter() {
	        return new RemoteIpFilter();
	    }
	 
	 @Bean
	 public HttpSessionEventPublisher httpSessionEventPublisher() {
	     return new HttpSessionEventPublisher();
	 }
	 
	/*
	 * @Bean public Module hibernate5Module() { Hibernate5Module hibernate5Module =
	 * new Hibernate5Module(); hibernate5Module.enable(
	 * Hibernate5Module.Feature.FORCE_LAZY_LOADING ); hibernate5Module.disable(
	 * Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION ); return hibernate5Module;
	 * }
	 */
}
