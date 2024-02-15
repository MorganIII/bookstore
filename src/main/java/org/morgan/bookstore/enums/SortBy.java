package org.morgan.bookstore.enums;


import org.springframework.core.convert.converter.Converter;

public enum SortBy {

    DATE,
    POPULARITY;

    public static SortBy convert(String source) {
        Converter<String,SortBy> converter = (s)->{
            try {
                return SortBy.valueOf(source.toUpperCase());
            } catch(Exception e) {
                return null;
            }
        };
        return converter.convert(source);
    }
}
