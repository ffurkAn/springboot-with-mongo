package com.mongoboot.internal.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.mongoboot.backend.repo.mongo")
public class MongoConfiguration {
}
