package com.fedeherrera.lawn.app.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

    @Component
    public class MapperUtil {

        @Autowired
        private ModelMapper modelMapper ;
    
        public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
        }

        public <D, T> D mapEntityToDto( T entity, Class<D> outClass) {
            return modelMapper.map(entity, outClass);
        }
    }

