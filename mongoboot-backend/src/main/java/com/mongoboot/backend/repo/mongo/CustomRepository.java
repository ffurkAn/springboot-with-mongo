package com.mongoboot.backend.repo.mongo;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomRepository {

    void updateImpressionCountStatistic(String adId);

    void updateClickCountStatistic(String adId);

    @Retryable(value = {OptimisticLockingFailureException.class}, maxAttempts = 10,  backoff = @Backoff(delay = 3000))
    void updateVisitorImpressionStatistic(String adId, String visitorId);
}
