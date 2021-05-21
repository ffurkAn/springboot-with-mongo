package com.sahibinden.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "delivery_document")
public class DeliveryDocument {

    @Id
    private String id;
    private String visitorId; // hangi kullancı için deliver edildi
    private AdDocument adDocument; // deliver edilen doküman
    private Boolean hasDeliveryBeenResponded; // deliver edilen reklam için herhangi bir event tetiklendi mi?

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AdDocument getAdDocument() {
        return adDocument;
    }

    public void setAdDocument(AdDocument adDocument) {
        this.adDocument = adDocument;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public Boolean getHasDeliveryBeenResponded() {
        return hasDeliveryBeenResponded;
    }

    public void setHasDeliveryBeenResponded(Boolean hasDeliveryBeenResponded) {
        this.hasDeliveryBeenResponded = hasDeliveryBeenResponded;
    }
}
