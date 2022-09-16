package com.unimerch.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimerch.dto.amznacc.*;
import com.unimerch.repository.model.AmznUser;
import com.unimerch.repository.model.AzmnStatus;
import com.unimerch.repository.model.BrgGroupAmznAccount;
import com.unimerch.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class AmznUserMapper extends StdDeserializer<AmznStatus> {
    @Autowired
    OrderService orderService;
    public AmznUserMapper() {
        this(null);
    }

    public AmznUserMapper(Class<?> vc) {
        super(vc);
    }

    @Override
    public AmznStatus deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode productNode = jsonParser.getCodec().readTree(jsonParser);
        String status = productNode.get("status").textValue();
        return new AmznStatus(status);
    }

    public AmznAccResult toDTO(BrgGroupAmznAccount brgGroupAmznAccount) {
        return new AmznAccResult()
                .setId(brgGroupAmznAccount.getAmznAccount().getId())
                .setUsername((brgGroupAmznAccount.getAmznAccount().getUsername()));
    }

    public AmznAccResult toDTO(AmznUser amznUser) {
        return new AmznAccResult()
                .setId(amznUser.getId())
                .setUsername(amznUser.getUsername());
    }

    public AmznUser toAmznAcc(AmznAccParam amznAccCreateParam) {
        return new AmznUser()
                .setUsername(amznAccCreateParam.getUsername())
                .setPassword(amznAccCreateParam.getPassword())
                .setLastCheck(Instant.now())
                .setDailyProductCount(0)
                .setDailyProductLimit(0)
                .setOverallDesignCount(0)
                .setOverallDesignLimit(0)
                .setOverallProductCount(0)
                .setOverallProductLimit(0)
                .setStatus(AzmnStatus.APPROVED)
                .setTier(0)
                .setTotalRejected(0)
                .setTotalRemoved(0);
    }

    public AmznAccFilterItemResult toAmznAccFilterItemResult(AmznUser amznUser) {
        int soldToday = orderService.getNumberSoldInDayByAmznId(amznUser.getId());

        return new AmznAccFilterItemResult()
                .setId(amznUser.getId())
                .setUsername(amznUser.getUsername())
                .setSoldToday(soldToday);
    }

    public AmznAccAnalyticsItemResult toAmznAccAnalyticsItemResult(AmznUser amznUser) {
        int slotRemaining = amznUser.getDailyProductLimit() - amznUser.getDailyProductCount();

        return new AmznAccAnalyticsItemResult()
                .setId(amznUser.getId())
                .setUsername(amznUser.getUsername())
                .setPublished(null)
                .setTier(amznUser.getTier())
                .setSlotRemaining(slotRemaining)
                .setSlotTotal(amznUser.getDailyProductLimit())
                .setReject(amznUser.getTotalRejected())
                .setRemove(amznUser.getTotalRemoved())
                .setNote(amznUser.getNote());
    }


}
