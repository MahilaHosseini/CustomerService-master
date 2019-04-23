package com.customerService.app.utility;

import com.customerService.app.dto.TransactionType;
import com.customerService.app.model.dao.AccountDao;
import com.customerService.app.model.entity.TransactionEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("minOfDay")
public class MinOfDayBenefitCalculation implements BenefitCalculation {
    private AccountDao accountDao;

    public MinOfDayBenefitCalculation(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void calculateBenefit() {

        accountDao.findAllByOrderByIdAsc().forEach(accountEntity -> {
            accountEntity.setAccountAmount(accountEntity.getAccountAmount().add(accountEntity.getProfit()));
            TransactionEntity transactionEntity = new TransactionEntity(accountEntity.getProfit(), TransactionType.Deposit, null);
            accountEntity.addTransaction(transactionEntity);
            accountEntity.setProfit(BigDecimal.ZERO);
            accountEntity.setMinimumOfTheDay(accountEntity.getAccountAmount());
        });


    }
}

