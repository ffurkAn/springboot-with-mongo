package com.mongoboot.tests;


import com.mongoboot.common.dto.ad.AdCreateRequest;
import com.mongoboot.common.dto.ad.AdResponse;
import com.mongoboot.common.dto.ad.DeliveryResult;
import com.mongoboot.common.dto.ad.MatchCriteria;
import com.mongoboot.common.service.AdService;
import com.mongoboot.service.AdServiceImpl;
import com.mongoboot.util.AdUtil;
import com.mongoboot.util.AdWithActualStat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentLinkedQueue;


public class TestMatchCriteria {

   private AdService adService;

   @Before
   public void init() {
      adService = new AdServiceImpl("http://localhost:9494","http://localhost:9393");
      adService.deleteAll();
   }


   @Test
   public void testMatchCriteriaLocation() {
      ConcurrentLinkedQueue<String> visitors = AdUtil.generateRandomVisitors(1);
      AdCreateRequest adCreateRequest = AdUtil.getRandomAdCreateRequest();
      AdResponse adResponse = adService.createAd(adCreateRequest);
      com.sahibinden.util.AdWithActualStat adWithActualStat = new com.sahibinden.util.AdWithActualStat(adResponse, adCreateRequest, adService);

      MatchCriteria matchCriteria = adWithActualStat.getUnmatchableCriteria(true, false, false, visitors.poll());

      DeliveryResult deliveryResult = adService.getWinner(matchCriteria);
      Assert.assertNull(String.format("ad with id : %s should not match with this matchCriteria %s ", deliveryResult.getAdResponse(), matchCriteria), deliveryResult.getAdResponse());

      visitors.add(matchCriteria.getVisitorId());

      matchCriteria = adWithActualStat.getMatchableCriteria(visitors);

      deliveryResult = adService.getWinner(matchCriteria);

      Assert.assertNotNull(String.format("ad with id : %s should  match with this matchCriteria %s ", adResponse.getId(), matchCriteria), deliveryResult.getDeliveryId());
      Assert.assertNotNull(String.format("ad with id : %s should match with this matchCriteria %s ", adResponse.getId(), matchCriteria), deliveryResult.getAdResponse());
   }


   @Test
   public void testMatchCriteriaClientType() {
      ConcurrentLinkedQueue<String> visitors = AdUtil.generateRandomVisitors(1);
      AdCreateRequest adCreateRequest = AdUtil.getRandomAdCreateRequest();
      AdResponse adResponse = adService.createAd(adCreateRequest);
      com.sahibinden.util.AdWithActualStat adWithActualStat = new com.sahibinden.util.AdWithActualStat(adResponse, adCreateRequest, adService);

      MatchCriteria matchCriteria = adWithActualStat.getUnmatchableCriteria(false, true, false, visitors.poll());

      DeliveryResult deliveryResult = adService.getWinner(matchCriteria);
      Assert.assertNull(deliveryResult.getAdResponse());
      Assert.assertNull(deliveryResult.getDeliveryId());

      visitors.add(matchCriteria.getVisitorId());

      matchCriteria = adWithActualStat.getMatchableCriteria(visitors);

      deliveryResult = adService.getWinner(matchCriteria);

      Assert.assertNotNull(String.format("ad with id : %s should  match with this matchCriteria %s ", adResponse.getId(), matchCriteria), deliveryResult.getDeliveryId());
      Assert.assertNotNull(String.format("ad with id : %s should match with this matchCriteria %s ", adResponse.getId(), matchCriteria), deliveryResult.getAdResponse());

   }


   @Test
   public void testMatchCriteriaCategory() {
      ConcurrentLinkedQueue<String> visitors = AdUtil.generateRandomVisitors(1);
      AdCreateRequest adCreateRequest = AdUtil.getRandomAdCreateRequest();
      AdResponse adResponse = adService.createAd(adCreateRequest);
      com.sahibinden.util.AdWithActualStat adWithActualStat = new AdWithActualStat(adResponse, adCreateRequest, adService);

      MatchCriteria matchCriteria = adWithActualStat.getUnmatchableCriteria(false, false, true, visitors.poll());

      DeliveryResult deliveryResult = adService.getWinner(matchCriteria);
      Assert.assertNull(deliveryResult.getAdResponse());
      Assert.assertNull(deliveryResult.getDeliveryId());

      visitors.add(matchCriteria.getVisitorId());

      matchCriteria = adWithActualStat.getMatchableCriteria(visitors);

      deliveryResult = adService.getWinner(matchCriteria);

      Assert.assertNotNull(String.format("ad with id : %s should  match with this matchCriteria %s ", adResponse.getId(), matchCriteria), deliveryResult.getDeliveryId());
      Assert.assertNotNull(String.format("ad with id : %s should match with this matchCriteria %s ", adResponse.getId(), matchCriteria), deliveryResult.getAdResponse());

   }
}
