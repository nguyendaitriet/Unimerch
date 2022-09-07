package com.unimerch.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimerch.dto.amznacc.Metadata;
import com.unimerch.repository.model.AmznAccount;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MetadataMapper extends StdDeserializer<Metadata> {

    public MetadataMapper() {
        this(null);
    }

    public MetadataMapper(Class<?> vc) {
        super(vc);
    }

    @Override
    public Metadata deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode orderNode = jsonParser.getCodec().readTree(jsonParser);

        int dailyProductCount = orderNode.get("dailyProductCount").asInt();
        int dailyProductLimit = orderNode.get("dailyProductLimit").asInt();
        int overallProductCount = orderNode.get("overallProductCount").asInt();
        int overallProductLimit = orderNode.get("overallProductLimit").asInt();
        int overallDesignCount = orderNode.get("overallDesignCount").asInt();
        int overallDesignLimit = orderNode.get("overallDesignLimit").asInt();
        int tier = orderNode.get("tier").asInt();
        int totalRejected = orderNode.get("totalRejected").asInt();
        int totalRemoved = orderNode.get("totalRemoved").asInt();

        return new Metadata(dailyProductCount, dailyProductLimit, overallProductCount, overallProductLimit,
                overallDesignCount, overallDesignLimit, tier, totalRejected, totalRemoved);
    }

    public AmznAccount updateAmznAccMetadata (AmznAccount amznAccount, Metadata metadata) {
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
