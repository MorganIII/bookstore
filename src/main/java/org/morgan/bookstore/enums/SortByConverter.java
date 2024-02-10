package org.morgan.bookstore.enums;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SortByConverter implements Converter<String, SortBy> {

    @Override
    public SortBy convert(String source) {
        try {
            return SortBy.valueOf(source);
        } catch(Exception e) {
            return null;
        }
    }
}
