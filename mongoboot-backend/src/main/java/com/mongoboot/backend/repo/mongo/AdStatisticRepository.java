package com.mongoboot.backend.repo.mongo;

import com.mongoboot.backend.model.AdStatisticDocument;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface AdStatisticRepository extends MongoRepository<AdStatisticDocument, String> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @QueryHints({@QueryHint(name="javax.persistence.lock.timeout", value ="1000")})
    Optional<AdStatisticDocument> findByAdId(String adId);
}
