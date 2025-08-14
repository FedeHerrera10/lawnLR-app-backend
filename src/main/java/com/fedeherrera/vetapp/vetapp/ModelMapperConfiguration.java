package com.fedeherrera.vetapp.vetapp;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfiguration {

    @Bean
    ModelMapper modelMapperBean() {
        return new ModelMapper();
    }
}
