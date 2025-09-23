package com.fedeherrera.vetapp.vetapp.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


public class CopyNonNullProperties {
    
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        // Obtener las propiedades nulas del objeto fuente
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        
        // Agregar las propiedades a ignorar
        if (ignoreProperties != null) {
            for (String ignoreProperty : ignoreProperties) {
                emptyNames.add(ignoreProperty);
            }
        }
        
        // Copiar propiedades no nulas
        BeanUtils.copyProperties(source, target, emptyNames.toArray(new String[0]));
    }
}
