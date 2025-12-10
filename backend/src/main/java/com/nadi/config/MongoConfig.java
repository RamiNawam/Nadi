package com.nadi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new OffsetDateTimeWriteConverter());
        converters.add(new OffsetDateTimeReadConverter());
        return new MongoCustomConversions(converters);
    }

    @WritingConverter
    static class OffsetDateTimeWriteConverter implements Converter<OffsetDateTime, Date> {
        @Override
        public Date convert(@NonNull OffsetDateTime source) {
            return Date.from(source.toInstant());
        }
    }

    @ReadingConverter
    static class OffsetDateTimeReadConverter implements Converter<Date, OffsetDateTime> {
        @Override
        public OffsetDateTime convert(@NonNull Date source) {
            return OffsetDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }
}

