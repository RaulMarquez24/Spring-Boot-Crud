package com.bolsadeideas.springboot.app;

import java.util.Locale;

import org.springframework.context.annotation.Bean;

// import java.nio.file.Paths;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

        // private final Logger log = LoggerFactory.getLogger(getClass());


    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     // TODO Auto-generated method stub
    //     WebMvcConfigurer.super.addResourceHandlers(registry);
        
    //     String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
    //     log.info(resourcePath);
    //     // LINUX "file:/opt/uploads/" WINDOWS "file:/C:/Temp/uploads/" --> esta forma es la mas adecuada
    //     registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
    // }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error_403").setViewName("error/error_403");
    }

    // Languaje
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.forLanguageTag("es-ES"));
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        return localeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TODO Auto-generated method stub
        registry.addInterceptor(localeChangeInterceptor());
    }


    
}
