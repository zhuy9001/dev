package com.zhuy.manager.error;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ErrorConfiguration {

    @Bean
    public MyErrorController basicErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties,  ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider){
        return new MyErrorController(errorAttributes,serverProperties.getError(),errorViewResolversProvider.getIfAvailable());
    }
}
