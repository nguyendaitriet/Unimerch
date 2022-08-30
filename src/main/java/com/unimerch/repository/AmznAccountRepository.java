package com.unimerch.repository;

import com.unimerch.repository.model.AmznAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AmznAccountRepository extends JpaRepository<AmznAccount, Integer> {

    boolean existsByUsername(String username);

    AmznAccount getByUsername(String username);

}
