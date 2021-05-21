package com.sahibinden.backend.mapper;

import com.sahibinden.backend.model.AdDocument;
import com.sahibinden.common.dto.ad.AdCreateRequest;
import com.sahibinden.common.dto.ad.AdResponse;
import com.sahibinden.common.dto.ad.Category;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-18T10:06:38+0300",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 11.0.10 (AdoptOpenJDK)"
)
@Component
public class AdMapperImpl implements AdMapper {

    @Override
    public AdDocument toAdDocument(AdCreateRequest adCreateRequest) {
        if ( adCreateRequest == null ) {
            return null;
        }

        AdDocument adDocument = new AdDocument();

        adDocument.setClientType( adCreateRequest.getClientType() );
        List<Category> list = adCreateRequest.getCategoryList();
        if ( list != null ) {
            adDocument.setCategoryList( new ArrayList<Category>( list ) );
        }
        else {
            adDocument.setCategoryList( null );
        }
        adDocument.setBidPrice( adCreateRequest.getBidPrice() );
        adDocument.setTotalBudget( adCreateRequest.getTotalBudget() );
        adDocument.setFrequencyCapping( adCreateRequest.getFrequencyCapping() );
        List<Long> list1 = adCreateRequest.getLocations();
        if ( list1 != null ) {
            adDocument.setLocations( new ArrayList<Long>( list1 ) );
        }
        else {
            adDocument.setLocations( null );
        }
        adDocument.setTitle( adCreateRequest.getTitle() );
        adDocument.setDescription( adCreateRequest.getDescription() );
        adDocument.setLink( adCreateRequest.getLink() );

        return adDocument;
    }

    @Override
    public AdResponse toAdResponse(AdDocument adDocument) {
        if ( adDocument == null ) {
            return null;
        }

        AdResponse adResponse = new AdResponse();

        adResponse.setId( adDocument.getId() );
        adResponse.setTitle( adDocument.getTitle() );
        adResponse.setDescription( adDocument.getDescription() );
        adResponse.setLink( adDocument.getLink() );

        return adResponse;
    }
}
