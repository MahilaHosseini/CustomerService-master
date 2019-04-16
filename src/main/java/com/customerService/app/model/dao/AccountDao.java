package com.customerService.app.model.dao;

import com.customerService.app.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AccountDao extends JpaRepository<AccountEntity, Integer> {
    AccountEntity findByAccountNumber(String accountNumber);
    List<AccountEntity> findAllByOrderByIdAsc();

}
