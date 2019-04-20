package com.customerService.app.controller;

import com.customerService.app.dto.TransactionType;
import com.customerService.app.model.dao.AccountDao;
import com.customerService.app.model.entity.TransactionEntity;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;


@Component("minOfMonth")
public class MinOfMonthBenefitCalculation implements BenefitCalculation {
    private AccountDao accountDao;

    public MinOfMonthBenefitCalculation(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void calculateBenefit() {
        accountDao.findAllByOrderByIdAsc().forEach(accountEntity -> {
            accountEntity.setAccountAmount(accountEntity.getAccountAmount().add(accountEntity.getMinimumOfMonth().multiply(new BigDecimal(600)).divide(new BigDecimal(36500))));
            accountEntity.setMinimumOfMonth(accountEntity.getAccountAmount());
            TransactionEntity transactionEntity = new TransactionEntity(accountEntity.getProfit(), TransactionType.Deposit, null);
            accountEntity.addTransaction(transactionEntity);
        });

   }
}
