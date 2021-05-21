package com.sahibinden.backend.repo.mongo;

import com.sahibinden.backend.model.AdDocument;
import com.sahibinden.common.dto.ad.Category;
import com.sahibinden.common.dto.ad.ClientType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AdMongoRepository extends MongoRepository<AdDocument, String> {

    List<AdDocument> findByClientTypeAndCategoryListContainsAndLocationsContains(ClientType clientType, Category category, long location);
}
