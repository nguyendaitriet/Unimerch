package com.unimerch.mapper;

import com.unimerch.dto.amznacc.Metadata;
import com.unimerch.repository.model.amzn_user.AmznUser;
import org.springframework.stereotype.Component;

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
