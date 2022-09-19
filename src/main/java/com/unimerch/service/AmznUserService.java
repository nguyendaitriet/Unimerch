package com.unimerch.service;

import com.unimerch.dto.amznacc.*;
import com.unimerch.repository.model.AmznUser;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AmznUserService {

    void updateMetadata(String data, Authentication authentication);

    DataTablesOutput<AmznAccResult> findAll(DataTablesInput input);

    List<AmznAccResult> findAll();

    List<AmznAccFilterResult> findAllFilter();

    AmznUser findById(String id);

    AmznAccResult create(AmznAccParam amznAccCreateParam);

    AmznAccResult update(String id, AmznAccParam amznAccParam);

    void delete(String id);

    List<AmznAccResult> importFile(MultipartFile amznAccFile);

    AmznAccResult findByUsername(String username);

    List<AmznAccAnalyticsResult> findAllAnalytics();

    List<AmznAccAnalyticsResult> findAnalyticsByGrpId(String groupId);

    List<AmznAccAnalyticsResult> findAnalyticsByAmznAccId(String amznAccId);

    void addNoteToAmznAcc(String id, String note);

    List<AmznAccDieResult> findAllAccDie();

    List<AmznAccDieResult> findAccDieByGrpId(String groupId);
}
