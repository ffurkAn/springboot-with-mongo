package com.sahibinden.common.dto.ad;

public class DeliveryResult {

   private AdResponse adResponse;
   private String deliveryId;

   public AdResponse getAdResponse() {
      return adResponse;
   }

   public void setAdResponse(AdResponse adResponse) {
      this.adResponse = adResponse;
   }

   public String getDeliveryId() {
      return deliveryId;
   }

   public void setDeliveryId(String deliveryId) {
      this.deliveryId = deliveryId;
   }


   @Override
   public String toString() {
      return "DeliveryResult{" +
              "adResponse=" + adResponse +
              ", deliveryId='" + deliveryId + '\'' +
              '}';
   }
}

