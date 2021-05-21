package com.mongoboot.common.service;

import com.mongoboot.common.dto.ad.AdCreateRequest;
import com.mongoboot.common.dto.ad.AdResponse;
import com.mongoboot.common.dto.ad.AdStatistic;
import com.mongoboot.common.dto.ad.DeliveryResult;
import com.mongoboot.common.dto.ad.MatchCriteria;

public interface AdService {


   AdResponse createAd(AdCreateRequest adCreateRequest);

   AdStatistic getAdStatistic(String adId);

   DeliveryResult getWinner(MatchCriteria matchCriteria);

   void processImpression(String deliveryId);

   void processClick(String deliveryId);

   void deleteAll();

}
