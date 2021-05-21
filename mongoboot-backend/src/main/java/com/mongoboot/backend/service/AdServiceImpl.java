package com.mongoboot.backend.service;

import com.mongoboot.backend.mapper.AdMapper;
import com.sahibinden.backend.model.AdDocument;
import com.sahibinden.backend.model.AdStatisticDocument;
import com.sahibinden.backend.model.DeliveryDocument;
import com.sahibinden.backend.repo.mongo.AdMongoRepository;
import com.sahibinden.backend.repo.mongo.AdStatisticRepository;
import com.sahibinden.backend.repo.mongo.CustomRepository;
import com.sahibinden.backend.repo.mongo.DeliveryRepository;
import com.sahibinden.common.dto.ad.*;
import com.sahibinden.common.service.AdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
public class AdServiceImpl implements AdService {

    public static final String BAD_WORDS_TXT = "badWords.txt";
    @Autowired
    AdMongoRepository adMongoRepository;

    @Autowired
    AdMapper adMapper;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    AdStatisticRepository adStatisticRepository;

    @Autowired
    CustomRepository customRepository;

    @Override
    public AdResponse createAd(AdCreateRequest adCreateRequest) {

        if (isRequestValid(adCreateRequest)) {
            AdDocument savedDoc = adMongoRepository.save(adMapper.toAdDocument(adCreateRequest));
            createInitialAdStatistic(savedDoc.getId());
            return adMapper.toAdResponse(savedDoc);
        }
        return null;
    }

    private void createInitialAdStatistic(String adId) {

        AdStatisticDocument adStatisticDocument = new AdStatisticDocument();
        adStatisticDocument.setAdId(adId);
        adStatisticDocument.setClickCount(0l);
        adStatisticDocument.setImpressionCount(0l);
        adStatisticDocument.setVisitorImpressionMap(new HashMap<>());
        adStatisticRepository.save(adStatisticDocument);
    }

    private boolean isRequestValid(AdCreateRequest adCreateRequest) {

        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<AdCreateRequest>> constraintViolations = validator.validate(adCreateRequest);
        if (!constraintViolations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<AdCreateRequest> cv : constraintViolations) {
                sb.append("[");
                sb.append(cv.getPropertyPath());
                sb.append(" ");
                sb.append(cv.getMessage());
                sb.append("] ");
            }

            return false;
        }

        if (adCreateRequest.getTotalBudget().compareTo(adCreateRequest.getBidPrice() * 10) < 0) {
            return false;
        }

        // check title or description contains bad word
        String fileName = BAD_WORDS_TXT;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            Optional<String> lineOpt = stream.filter(line -> adCreateRequest.getTitle().contains(line) ||
                    adCreateRequest.getDescription().contains(line)).findFirst();

            if (lineOpt.isPresent()) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occured while reading file: " + fileName);
            return false;
        }

        return true;
    }

    @Override
    public AdStatistic getAdStatistic(String adId) {

        Optional<AdStatisticDocument> adStatisticDocumentOptional = adStatisticRepository.findByAdId(adId);
        if (adStatisticDocumentOptional.isPresent()) {
            AdStatisticDocument adStatisticDocument = adStatisticDocumentOptional.get();
            AdStatistic adStatistic = new AdStatistic();
            adStatistic.setAdId(adStatisticDocument.getAdId());
            adStatistic.setClickCount(adStatisticDocument.getClickCount());
            adStatistic.setImpressionCount(adStatisticDocument.getImpressionCount());

            return adStatistic;
        }
        return null;
    }

    @Override
    public DeliveryResult getWinner(MatchCriteria matchCriteria) {

        DeliveryResult deliveryResult = new DeliveryResult();

        String visitorId = matchCriteria.getVisitorId();
        List<AdDocument> adDocumentList = adMongoRepository.findByClientTypeAndCategoryListContainsAndLocationsContains(
                matchCriteria.getClientType(), matchCriteria.getCategory(), matchCriteria.getLocation());

        if (!adDocumentList.isEmpty()) {

            for (AdDocument adDocument : adDocumentList) {
                // return first add which has enough budget and given visitor can see it
                if (isBudgetEnough(adDocument) && checkVisitorCanSeeAd(adDocument, visitorId)) {
                    DeliveryDocument deliveryDocument = new DeliveryDocument();
                    deliveryDocument.setAdDocument(adDocument);
                    deliveryDocument.setVisitorId(matchCriteria.getVisitorId());
                    deliveryDocument.setHasDeliveryBeenResponded(Boolean.FALSE);
                    deliveryRepository.save(deliveryDocument);

                    deliveryResult.setDeliveryId(deliveryDocument.getId());
                    deliveryResult.setAdResponse(adMapper.toAdResponse(adDocument));
                }
            }
        }
        return deliveryResult;
    }

    private boolean checkVisitorCanSeeAd(AdDocument adDocument, String visitorId) {

        Optional<AdStatisticDocument> adStatisticDocumentOptional = adStatisticRepository.
                findByAdId(adDocument.getId());

        if (adStatisticDocumentOptional.isPresent()) {
            AdStatisticDocument adStatisticDocument = adStatisticDocumentOptional.get();

            // if impression count is less than frequencyCapping
            return adStatisticDocument.getVisitorImpressionMap().get(visitorId) == null ||
                    adStatisticDocument.getVisitorImpressionMap().get(visitorId) < adDocument.getFrequencyCapping();
        }
        return true;
    }

    @Override
    public void processImpression(String deliveryId) {

        Optional<DeliveryDocument> deliveryDocumentOptional = deliveryRepository.findById(deliveryId);

        // If Delivery exists with given deliveryId and has not been responded yet
        if (deliveryDocumentOptional.isPresent() && Boolean.FALSE.equals(deliveryDocumentOptional.get().getHasDeliveryBeenResponded())) {
            DeliveryDocument deliveryDocument = deliveryDocumentOptional.get();

            deliveryDocument.setHasDeliveryBeenResponded(Boolean.TRUE);
            deliveryRepository.save(deliveryDocument);

            // increment impression count of Ad
            customRepository.updateImpressionCountStatistic(deliveryDocument.getAdDocument().getId());
            customRepository.updateVisitorImpressionStatistic(deliveryDocument.getAdDocument().getId(), deliveryDocument.getVisitorId());

        }
    }

    @Override
    public void processClick(String deliveryId) {

        Optional<DeliveryDocument> deliveryDocumentOptional = deliveryRepository.findById(deliveryId);

        // If Delivery exists with given deliveryId and has not been responded yet
        if (deliveryDocumentOptional.isPresent() && Boolean.FALSE.equals(deliveryDocumentOptional.get().getHasDeliveryBeenResponded())) {
            // ad delivery için kayıt bulundu.
            DeliveryDocument deliveryDocument = deliveryDocumentOptional.get();

            // check budget to see if click is doable
            if (isBudgetEnough(deliveryDocument.getAdDocument())) {

                // increment click count of Ad
                // since every click is an impression, increment impression count as well
                customRepository.updateClickCountStatistic(deliveryDocument.getAdDocument().getId());
                customRepository.updateImpressionCountStatistic(deliveryDocument.getAdDocument().getId());
                customRepository.updateVisitorImpressionStatistic(deliveryDocument.getAdDocument().getId(), deliveryDocument.getVisitorId());

                deliveryDocument.setHasDeliveryBeenResponded(Boolean.TRUE);
                deliveryRepository.save(deliveryDocument);
            }

        }
    }


    private boolean isBudgetEnough(AdDocument adDocument) {

        Optional<AdStatisticDocument> adStatisticDocumentOptional = adStatisticRepository.findByAdId(adDocument.getId());

        if (adStatisticDocumentOptional.isPresent()) {
            AdStatisticDocument adStatisticDocument = adStatisticDocumentOptional.get();
            if (adDocument.getTotalBudget() >= ((adStatisticDocument.getClickCount().intValue() + 1) * adDocument.getBidPrice())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteAll() {

        adStatisticRepository.deleteAll();
        adMongoRepository.deleteAll();
        deliveryRepository.deleteAll();

    }
}
