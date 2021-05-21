package com.mongoboot.backend.repo.mongo;

import com.mongoboot.backend.model.DeliveryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends MongoRepository<DeliveryDocument, String> {
}
