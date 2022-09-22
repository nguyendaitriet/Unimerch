package com.unimerch.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimerch.dto.amznacc.Metadata;
import com.unimerch.repository.model.AmznUser;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MetadataMapper{
    public AmznUser updateAmznAccMetadata (AmznUser amznAccount, Metadata metadata) {
        amznAccount.setDailyProductCount(metadata.getDailyProductCount());
        amznAccount.setDailyProductLimit(metadata.getDailyProductLimit());
        amznAccount.setOverallProductCount(metadata.getOverallProductCount());
        amznAccount.setOverallProductLimit(metadata.getOverallProductLimit());
        amznAccount.setOverallDesignCount(metadata.getOverallDesignCount());
        amznAccount.setOverallDesignLimit(metadata.getOverallDesignLimit());
        amznAccount.setTier(metadata.getTier());
        amznAccount.setTotalRejected(metadata.getTotalRejected());
        amznAccount.setTotalRemoved(metadata.getTotalRemoved());
        return amznAccount;
    }
}
