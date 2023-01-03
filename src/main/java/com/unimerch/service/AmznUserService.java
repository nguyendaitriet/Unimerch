package com.unimerch.service;

import com.unimerch.dto.amznacc.*;
import com.unimerch.repository.model.amzn_user.AmznUser;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AmznUserService {

    void updateMetadata(Metadata data, Authentication authentication);

    void updateStatus(AmznStatus status, Authentication authentication);

    DataTablesOutput<AmznAccResult> findAll(DataTablesInput input);

    List<AmznAccResult> findAll();

    List<AmznAccFilterResult> findAllFilter();

    List<AmznAccAnalyticsResult> findAllAnalytics();

    List<AmznAccDieResult> findAllAccDie();

    AmznUser findById(String id);

    AmznAccResult findByUsername(String username);

    List<AmznAccAnalyticsResult> findAnalyticsByGrpId(String groupId);

    List<AmznAccAnalyticsResult> findAnalyticsByAmznAccId(String amznAccId);

    List<AmznAccDieResult> findAccDieByGrpId(String groupId);

    List<AmznAccLastCheckResult> findAllLastCheck12Hours();

    List<AmznAccLastCheckResult> findAllLastCheck12HoursByGrpId(String groupId);

    AmznAccResult create(AmznAccParam amznAccCreateParam);

    AmznAccResult update(String id, AmznAccParam amznAccParam);

    void delete(String id);

    void deleteAllByListId(List<Integer> idList);

    List<AmznAccResult> importFile(MultipartFile amznAccFile);

    byte[] getAmznAccFileSample();

    void addNoteToAmznAcc(String id, String note);

}
