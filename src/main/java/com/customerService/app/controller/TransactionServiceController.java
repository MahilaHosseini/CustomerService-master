package com.customerService.app.controller;

import com.customerService.app.dto.*;
import com.customerService.app.dto.UiTransactionDto;
import com.customerService.app.model.dao.AccountDao;
import com.customerService.app.model.entity.*;
import com.customerService.app.utility.BenefitCalculation;
import com.customerService.app.utility.TransactionException;
import com.customerService.app.utility.TransactionValidationUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Objects;

@Component
@EnableScheduling
public class TransactionServiceController {
    private static Logger logger = LoggerFactory.getLogger(TransactionServiceController.class);
    private AccountDao accountDao;

    @Autowired
    @Qualifier("minOfDay")
    private BenefitCalculation benefitCalculation;

    public TransactionServiceController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Transactional(rollbackOn = Exception.class)
    public void transfer(UiTransactionDto uiTransactionDto) throws TransactionException {
        logger.info("Transfer Transaction Started!");
        TransactionValidationUtility.validateTransfer(uiTransactionDto, accountDao);
        AccountEntity sourceAccountEntity = accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber());
        sourceAccountEntity.setAccountAmount(sourceAccountEntity.getAccountAmount().subtract(uiTransactionDto.getAmount()));
        AccountEntity destinationAccountEntity = accountDao.findByAccountNumber(uiTransactionDto.getDestinationAccountNumber());
        destinationAccountEntity.setAccountAmount(destinationAccountEntity.getAccountAmount().add(uiTransactionDto.getAmount()));
        TransactionEntity sourceTransactionEntity = new TransactionEntity(uiTransactionDto.getAmount(), TransactionType.Transfer, uiTransactionDto.getDestinationAccountNumber());
        sourceAccountEntity.setTransactions(sourceAccountEntity.addTransaction(sourceTransactionEntity));
        TransactionEntity destinationTransactionEntity = new TransactionEntity(uiTransactionDto.getAmount(), TransactionType.Deposit, null);
        destinationAccountEntity.setTransactions(destinationAccountEntity.addTransaction(destinationTransactionEntity));
        if (sourceAccountEntity.getAccountAmount().compareTo(sourceAccountEntity.getMinimumOfTheDay()) == -1)
            sourceAccountEntity.setMinimumOfTheDay(sourceAccountEntity.getAccountAmount());
        logger.info("Transfer Transaction Ended Successfully!");

    }


    @Transactional(rollbackOn = Exception.class)
    public void deposit(UiTransactionDto uiTransactionDto) throws TransactionException {
        logger.info("Transfer Transaction Started!");

        TransactionValidationUtility.validateDeposit(uiTransactionDto, accountDao);
        AccountEntity destinationAccountEntity = accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber());
        destinationAccountEntity.setAccountAmount(destinationAccountEntity.getAccountAmount().add(uiTransactionDto.getAmount()));
        TransactionEntity destinationTransactionEntity = new TransactionEntity(uiTransactionDto.getAmount(), TransactionType.Deposit, null);
        destinationAccountEntity.setTransactions(destinationAccountEntity.addTransaction(destinationTransactionEntity));
        logger.info("Deposit Transaction Ended Successfully!");

    }

    public AccountEntity search(String accountNumber) throws Exception {
        logger.info("search web service is starting !");
        AccountEntity accountEntity = accountDao.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accountEntity)) {
            logger.info("search web service is Successfully ended !");
            return accountEntity;
        } else {
            throw new Exception("No such Account");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void removal(UiTransactionDto uiTransactionDto) throws TransactionException {
        logger.info("Removal Transaction Started!");
        TransactionValidationUtility.validateRemoval(uiTransactionDto, accountDao);
        AccountEntity sourceAccountEntity = accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber());
        sourceAccountEntity.setAccountAmount(sourceAccountEntity.getAccountAmount().subtract(uiTransactionDto.getAmount()));
        TransactionEntity transactionEntity = new TransactionEntity(uiTransactionDto.getAmount(), TransactionType.Removal, null);
        sourceAccountEntity.setTransactions(sourceAccountEntity.addTransaction(transactionEntity));
        if (sourceAccountEntity.getAccountAmount().compareTo(sourceAccountEntity.getMinimumOfTheDay()) == -1)
            sourceAccountEntity.setMinimumOfTheDay(sourceAccountEntity.getAccountAmount());
        if (sourceAccountEntity.getAccountAmount().compareTo(sourceAccountEntity.getMinimumOfMonth()) == -1)
            sourceAccountEntity.setMinimumOfMonth(sourceAccountEntity.getAccountAmount());
        logger.info("Removal Transaction Ended Successfully!");

}
/*

    @Scheduled(fixedRate = 30000 */
    /* cron = "0 0 * * * *" *//*
)
    @Transactional(rollbackOn = Exception.class)
    public void dailyProfitCalculation() {
        logger.info("Scheduled dailyProfitCalculation Started!");
        try {
            accountDao.findAllByOrderByIdAsc().forEach(accountEntity -> {
                accountEntity.setProfit(accountEntity.getProfit().add(accountEntity.getMinimumOfTheDay().multiply(new BigDecimal(20)).divide(new BigDecimal(36500), 0, RoundingMode.HALF_UP)));
                accountEntity.setMinimumOfTheDay(accountEntity.getAccountAmount());

            });

            logger.info("Scheduled dailyProfitCalculation Ended Successfully!");
        } catch (Exception errorMessage) {
            logger.error("Scheduled dailyProfitCalculation Exited With Error: " + errorMessage.getMessage());
        }
    }

    */
/*
            * * * * * *
            | | | | | |
            | | | | | +-- Year              (range: 1900-3000)
            | | | | +---- Day of the Week   (range: 1-7, 1 standing for Monday)
            | | | +------ Month of the Year (range: 1-12)
            | | +-------- Day of the Month  (range: 1-31)
            | +---------- Hour              (range: 0-23)
            +------------ Minute            (range: 0-59)

    *//*

    @Scheduled(fixedRate = 120000 */
    /*cron = "0 0 1 * * *" *//*
)
    @Transactional(rollbackOn = Exception.class)
    public void ProfitDeposit() {
        logger.info("ProfitDeposit Started!");

        try {
            benefitCalculation.calculateBenefit();
            logger.info("ProfitDeposit Ended Successfully!");

        } catch (Exception errorMessage) {
            logger.error("ProfitDeposit Exited With Error: " + errorMessage.getMessage());
        }
    }
*/


}
