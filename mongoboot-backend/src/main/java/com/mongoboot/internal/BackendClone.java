package com.mongoboot.internal;

import com.mongoboot.internal.config.MongoConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class})
@ComponentScan(value = "com.mongoboot.backend")
@Import(value = MongoConfiguration.class)
@EnableRetry
public class BackendClone {

}
