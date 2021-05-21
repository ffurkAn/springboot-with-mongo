package com.mongoboot.backend.repo.mongo;


import com.mongoboot.backend.model.AdStatisticDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomRepositoryImpl implements CustomRepository {

    public static final String IMPRESSION_COUNT = "impressionCount";
    public static final String ID = "_id";
    public static final String CLICK_COUNT = "clickCount";
    private final MongoTemplate mongoTemplate;

    AdStatisticRepository adStatisticRepository;

    @Autowired
    public CustomRepositoryImpl(MongoTemplate mongoTemplate, AdStatisticRepository adStatisticRepository) {
        this.mongoTemplate = mongoTemplate;
        this.adStatisticRepository = adStatisticRepository;
    }

    @Override
    public void updateImpressionCountStatistic(String adId) {

        Query query = Query.query(Criteria.where(ID).is(adId));
        Update update = new Update().inc(IMPRESSION_COUNT, 1);

        mongoTemplate.updateFirst(query, update, AdStatisticDocument.class);
    }


    @Override
    public void updateClickCountStatistic(String adId) {

        Query query = Query.query(Criteria.where(ID).is(adId));
        Update update = new Update().inc(CLICK_COUNT, 1);

        mongoTemplate.updateFirst(query, update, AdStatisticDocument.class);
    }

    @Override
    @Retryable(value = {OptimisticLockingFailureException.class}, maxAttempts = 10,  backoff = @Backoff(delay = 3000))
    public void updateVisitorImpressionStatistic(String adId, String visitorId) {
        Optional<AdStatisticDocument> adStatisticDocumentOptional =
                adStatisticRepository.findByAdId(adId);

        if (adStatisticDocumentOptional.isPresent()) {
            AdStatisticDocument adStatisticDocument = adStatisticDocumentOptional.get();
            try{
                adStatisticDocument.getVisitorImpressionMap().put(visitorId, adStatisticDocument.getVisitorImpressionMap().get(visitorId) + 1);
            } catch (NullPointerException e){
                adStatisticDocument.getVisitorImpressionMap().put(visitorId, 1);
            }
            adStatisticRepository.save(adStatisticDocument);
        }
    }

}
