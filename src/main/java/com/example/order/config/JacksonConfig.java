package com.example.order.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return builder -> {
			// Configure date/time formats
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

			builder.modules(new JavaTimeModule()); // Register the JSR-310 module
			builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
			builder.failOnUnknownProperties(false);
			builder.failOnEmptyBeans(false);

			// Register custom date/time serializers and deserializers
			builder.deserializers(new LocalDateDeserializer(dateFormatter));
			builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));
			builder.serializers(new LocalDateSerializer(dateFormatter));
			builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
		};
	}

}