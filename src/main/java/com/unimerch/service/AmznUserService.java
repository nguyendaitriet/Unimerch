package com.unimerch.service;

import com.unimerch.dto.amznacc.AmznAccFilterItemResult;
import com.unimerch.dto.amznacc.AmznAccParam;
import com.unimerch.dto.amznacc.AmznAccResult;
import com.unimerch.repository.model.AmznUser;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AmznUserService {

    void updateMetadata(String data, String jwt);

    DataTablesOutput<AmznAccResult> findAll(DataTablesInput input);

    List<AmznAccResult> findAll();

    List<AmznAccFilterItemResult> findAllFilter();

    AmznUser findById(String id);

    AmznAccResult create(AmznAccParam amznAccCreateParam);

    AmznAccResult update(String id, AmznAccParam amznAccParam);

    void delete(String id);

    List<AmznAccResult> importFile(MultipartFile amznAccFile);

    AmznAccResult findByUsername(String username);
}
