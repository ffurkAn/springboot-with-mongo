package com.mongoboot.common.dto.ad;

import javax.validation.constraints.*;
import java.util.List;

public class AdCreateRequest {

   public static final String REGEXP_LINK = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

   @NotNull
   private ClientType clientType;

   @NotEmpty
   private List<Category> categoryList;

   @NotNull
   @Min(50)
   @Max(300)
   private Long bidPrice;

   @NotNull
   private Long totalBudget;

   @NotNull
   @Min(6)
   @Max(24)
   private Long frequencyCapping;

   private List<@Min(1) @Max(81) Long> locations;

   @Size(min = 10, max = 30)
   private String title;

   @Size(min = 30, max = 100)
   private String description;

   @Pattern(regexp = REGEXP_LINK)
   private String link;


   public ClientType getClientType() {
      return clientType;
   }

   public void setClientType(ClientType clientType) {
      this.clientType = clientType;
   }

   public List<Category> getCategoryList() {
      return categoryList;
   }

   public void setCategoryList(List<Category> categoryList) {
      this.categoryList = categoryList;
   }

   public Long getBidPrice() {
      return bidPrice;
   }

   public void setBidPrice(Long bidPrice) {
      this.bidPrice = bidPrice;
   }

   public Long getTotalBudget() {
      return totalBudget;
   }

   public void setTotalBudget(Long totalBudget) {
      this.totalBudget = totalBudget;
   }

   public Long getFrequencyCapping() {
      return frequencyCapping;
   }

   public void setFrequencyCapping(Long frequencyCapping) {
      this.frequencyCapping = frequencyCapping;
   }

   public List<Long> getLocations() {
      return locations;
   }

   public void setLocations(List<Long> locations) {
      this.locations = locations;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getLink() {
      return link;
   }

   public void setLink(String link) {
      this.link = link;
   }


   @Override
   public String toString() {
      return "AdCreateRequest{" +
              "clientType=" + clientType +
              ", categoryList=" + categoryList +
              ", bidPrice=" + bidPrice +
              ", totalBudget=" + totalBudget +
              ", frequencyCapping=" + frequencyCapping +
              ", locations=" + locations +
              ", title='" + title + '\'' +
              ", description='" + description + '\'' +
              ", link='" + link + '\'' +
              '}';
   }
}
