package com.mongoboot.backend.controller;


import com.mongoboot.common.dto.ad.AdCreateRequest;
import com.mongoboot.common.dto.ad.AdResponse;
import com.mongoboot.common.dto.ad.AdStatistic;
import com.mongoboot.common.dto.ad.DeliveryResult;
import com.mongoboot.common.dto.ad.MatchCriteria;
import com.mongoboot.common.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ad")
public class AdController {

   @Autowired
   private AdService adService;

   @PostMapping(value = "/createAd", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<AdResponse> createAd(@RequestBody AdCreateRequest adCreateRequest) {
      return ResponseEntity.ok(adService.createAd(adCreateRequest));
   }

   @GetMapping(value = "/{adId}/statistic", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<AdStatistic> getAdStatistic(@PathVariable("adId") String adId) {
      return ResponseEntity.ok(adService.getAdStatistic(adId));
   }

   @PostMapping(value = "/getWinner", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<DeliveryResult> getWinner(@RequestBody MatchCriteria matchCriteria) {
      return ResponseEntity.ok(adService.getWinner(matchCriteria));
   }

   @GetMapping(value = "/{deliveryId}/processImpression")
   public ResponseEntity<Void> processImpression(@PathVariable("deliveryId") String deliveryId) {

      adService.processImpression(deliveryId);
      return ResponseEntity.ok().build();
   }

   @GetMapping(value = "/{deliveryId}/processClick")
   public ResponseEntity<Void> processClick(@PathVariable("deliveryId") String deliveryId) {
      adService.processClick(deliveryId);
      return ResponseEntity.ok().build();
   }

   @GetMapping(value = "/deleteAll")
   public ResponseEntity<Void> deleteAllAds() {
      adService.deleteAll();
      return ResponseEntity.ok().build();
   }

}
