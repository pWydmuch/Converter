package com.example.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ConverterApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ConverterApplication.class, args);
    }
	
//	@Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(ConverterApplication.class);
//    }
//
//    @Bean //how to change scope in @Component
//    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public ConverterToRoman getConverterToRoman(){ //name of the method was important
//        return new ConverterToRoman();
//    }
//
//    @Bean
//    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public ConverterToArabic getConverterToArabic(){
//        return new ConverterToArabic();
//    }

}

