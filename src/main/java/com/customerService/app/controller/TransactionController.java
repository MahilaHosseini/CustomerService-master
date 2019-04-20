package com.customerService.app.controller;

import com.customerService.app.dto.*;
import com.customerService.app.dto.ResponseStatus;
import com.customerService.app.dto.TransactionDto;
import com.customerService.app.model.dao.AccountDao;
import com.customerService.app.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@RestController
@EnableScheduling
public class TransactionController {
    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private AccountDao accountDao;

    @Autowired
    @Qualifier("minOfDay")
    private BenefitCalculation benefitCalculation;

    public TransactionController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    @RequestMapping(value = "/ws/transfer", method = RequestMethod.POST)
    @Transactional(rollbackOn = Exception.class)
    public ResponseDto<TransactionEntity> transfer(@RequestBody TransactionDto transactionDto) {
        logger.info("Transfer Transaction Started!");
        try {
            if (TransactionValidationUtility.validateTransfer(transactionDto, accountDao)) {
                AccountEntity sourceAccountEntity = accountDao.findByAccountNumber(transactionDto.getAccountNumber());
                sourceAccountEntity.setAccountAmount(sourceAccountEntity.getAccountAmount().subtract(transactionDto.getAmount()));
                AccountEntity destinationAccountEntity = accountDao.findByAccountNumber(transactionDto.getDestinationAccountNumber());
                destinationAccountEntity.setAccountAmount(destinationAccountEntity.getAccountAmount().add(transactionDto.getAmount()));
                TransactionEntity sourceTransactionEntity = new TransactionEntity(transactionDto.getAmount(), TransactionType.Transfer, transactionDto.getDestinationAccountNumber());
                sourceAccountEntity.setTransactionEntities(sourceAccountEntity.addTransaction(sourceTransactionEntity));
                TransactionEntity destinationTransactionEntity = new TransactionEntity(transactionDto.getAmount(), TransactionType.Deposit, null);
                destinationAccountEntity.setTransactionEntities(destinationAccountEntity.addTransaction(destinationTransactionEntity));
                if (sourceAccountEntity.getAccountAmount().compareTo(sourceAccountEntity.getMinimumOfTheDay()) == -1)
                    sourceAccountEntity.setMinimumOfTheDay(sourceAccountEntity.getAccountAmount());
                logger.info("Transfer Transaction Ended Successfully!");
                return new ResponseDto(ResponseStatus.Ok, null, "Successfully Transferred!", null);

            }
        } catch (Exception errorMessage) {
            logger.error("Transfer Transaction Exited With Error: " + errorMessage.getMessage());
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(errorMessage.getMessage()));
        }

        logger.error("Transfer Transaction Exited With Error: \"Unexpected Error Has Occurred!\" ");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Unexpected Error Has Occurred!"));

    }


    @RequestMapping(value = "/ws/deposit", method = RequestMethod.POST)
    @Transactional(rollbackOn = Exception.class)
    public ResponseDto deposit(@RequestBody TransactionDto transactionDto) {
        logger.info("Transfer Transaction Started!");

        try {
            if (TransactionValidationUtility.validateDeposit(transactionDto, accountDao)) {
                AccountEntity destinationAccountEntity = accountDao.findByAccountNumber(transactionDto.getAccountNumber());
                destinationAccountEntity.setAccountAmount(destinationAccountEntity.getAccountAmount().add(transactionDto.getAmount()));
                TransactionEntity destinationTransactionEntity = new TransactionEntity(transactionDto.getAmount(), TransactionType.Deposit, null);
                destinationAccountEntity.setTransactionEntities(destinationAccountEntity.addTransaction(destinationTransactionEntity));
                logger.info("Deposit Transaction Ended Successfully!");
                return new ResponseDto(ResponseStatus.Ok, null, "Successfully Deposited!", null);

            }
        } catch (Exception errorMessage) {
            logger.error("Deposit Transaction Exited With Error: " + errorMessage.getMessage());
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(errorMessage.getMessage()));
        }

        logger.error("Deposit Transaction Exited With Error: \"Unexpected Error Has Occurred!\" ");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Unexpected Error Has Occurred!"));

    }

    @RequestMapping(value = "/ws/search", method = RequestMethod.POST)
    public ResponseDto<AccountEntity> search(@RequestParam String accountNumber) {
        logger.info("search web service is starting !");
        AccountEntity accountEntity = accountDao.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accountEntity)) {
            logger.info("search web service is Successfully ended !");
            return new ResponseDto(ResponseStatus.Ok, accountEntity, null, null);
        }
        logger.error("uniqueRealSearch web service is exiting with  *NO Such Customer*  error !");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("NO Such Customer"));
    }

    @RequestMapping(value = "/ws/removal", method = RequestMethod.POST)
    @Transactional(rollbackOn = Exception.class)
    public ResponseDto<TransactionEntity> removal(@RequestBody TransactionDto transactionDto) {
        logger.info("Removal Transaction Started!");
        try {
            if (TransactionValidationUtility.validateRemoval(transactionDto, accountDao)) {
                AccountEntity sourceAccountEntity = accountDao.findByAccountNumber(transactionDto.getAccountNumber());
                sourceAccountEntity.setAccountAmount(sourceAccountEntity.getAccountAmount().subtract(transactionDto.getAmount()));
                TransactionEntity transactionEntity = new TransactionEntity(transactionDto.getAmount(), TransactionType.Removal, null);
                sourceAccountEntity.setTransactionEntities(sourceAccountEntity.addTransaction(transactionEntity));
                if (sourceAccountEntity.getAccountAmount().compareTo(sourceAccountEntity.getMinimumOfTheDay()) == -1)
                    sourceAccountEntity.setMinimumOfTheDay(sourceAccountEntity.getAccountAmount());
                if (sourceAccountEntity.getAccountAmount().compareTo(sourceAccountEntity.getMinimumOfMonth()) == -1)
                    sourceAccountEntity.setMinimumOfMonth(sourceAccountEntity.getAccountAmount());
                logger.info("Removal Transaction Ended Successfully!");
                return new ResponseDto(ResponseStatus.Ok, null, "Successfully Removed!", null);
            }
        } catch (Exception errorMessage) {
            logger.error("Removal Transaction Exited With Error: " + errorMessage.getMessage());
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(errorMessage.getMessage()));
        }

        logger.error("Deposit Transaction Exited With Error: \"Unexpected Error Has Occurred!\" ");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Unexpected Error Has Occurred!"));
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
