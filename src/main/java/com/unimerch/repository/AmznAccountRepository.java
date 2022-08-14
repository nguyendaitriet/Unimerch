package com.unimerch.repository;

import com.unimerch.repository.model.AmznAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmznAccountRepository extends JpaRepository<AmznAccount, Integer> {


}
