package com.customerService.app.controller;

import com.customerService.app.DemoApplication;
import com.customerService.app.dto.*;
import com.customerService.app.model.entity.*;
import com.customerService.app.utility.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class FacadeLayer {

    private static Logger logger = LoggerFactory.getLogger(DemoApplication.class);
    private CustomerServiceController customerServiceController;
    private TransactionServiceController transactionServiceController;

    public FacadeLayer(CustomerServiceController customerServiceController, TransactionServiceController transactionServiceController) {
        this.customerServiceController = customerServiceController;
        this.transactionServiceController = transactionServiceController;
    }

    public void addContact(RealPersonDto realPersonDto) throws RealPersonException {
        logger.info("addContact facade is running!");
        customerServiceController.addContact(CustomMapper.objectMapper(new RealPersonEntity(), realPersonDto));
    }

    public void addLegalContact(LegalPersonDto legalPersonDto) throws LegalPersonException {
        logger.info("addLegalContact facade Successfully ended !");
        customerServiceController.addLegalContact(CustomMapper.objectMapper(new LegalPersonEntity(), legalPersonDto));
        logger.info("addLegalContact facade Successfully ended !");
    }

    @Transactional(rollbackOn = Exception.class)
    public void save(RealPersonDto realPersonDto) throws RealPersonException, AccountException {

        logger.info("saveRealPerson facade is starting !");
        customerServiceController.save(CustomMapper.objectMapper(new RealPersonEntity(), realPersonDto));
        logger.info("saveRealPerson facade Successfully ended !");

    }

    @Transactional(rollbackOn = Exception.class)
    public void saveLegal(LegalPersonDto legalPersonDto) throws LegalPersonException, AccountException {

        logger.info("SaveLegalPerson facade Is Starting !");
        customerServiceController.saveLegal(CustomMapper.objectMapper(new LegalPersonEntity(), legalPersonDto));
        logger.info("SaveLegalPerson facade Has Successfully Ended !");
    }

    public void addAccount(UiAccountDto uiAccountDto) throws AccountException {
        logger.info("addAccount facade is starting !");
        customerServiceController.addAccount(uiAccountDto);
        logger.info("addAccount facade is starting !");


    }


    @Transactional(rollbackOn = Exception.class)
    public void deleteLegal(LegalPersonDto legalPersonDto) throws Exception {
        logger.info("DeleteLegalPerson Web Service Is Starting !");
        customerServiceController.deleteLegal(CustomMapper.objectMapper(new LegalPersonEntity(), legalPersonDto));
        logger.info("DeleteLegalPerson Web Service Is Successfully Ended");

    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(RealPersonDto realPersonDto) throws Exception {
        logger.info("DeleteRealPerson Web Service Is Starting");
        customerServiceController.delete(CustomMapper.objectMapper(new RealPersonEntity(), realPersonDto));
        logger.info("DeleteRealPerson Web Service Is Successfully Ended !");

    }

    public RealPersonDto uniqueRealSearch(String nationalCode) throws Exception {
        logger.info("UniqueRealSearch Web Service Is Starting !");
        RealPersonDto realPersonDto = CustomMapper.objectMapper(new RealPersonDto(), customerServiceController.uniqueRealSearch(nationalCode));
        return realPersonDto;
    }

    public LegalPersonDto uniqueLegalSearch(String registrationCode) throws Exception {
        logger.info("UniqueRealSearch Web Service Is Starting !");
        LegalPersonDto legalPersonDto = CustomMapper.objectMapper(new LegalPersonDto(), customerServiceController.uniqueLegalSearch(registrationCode));
        logger.info("UniqueRealSearch web service is Successfully ended !");
        return legalPersonDto;
    }

    public List<RealPersonDto> realSearch(SearchDto searchDto) throws Exception {
        logger.info("realPersonSearch web service is starting !");
        List<RealPersonEntity> realPersonEntities = customerServiceController.realSearch(searchDto);
        List<RealPersonDto> realPersonDtos = new ArrayList<>();
        for (RealPersonEntity realPersonEntity : realPersonEntities) {
            realPersonDtos.add(CustomMapper.objectMapper(new RealPersonDto(), realPersonEntity));
        }
        logger.info("realPersonSearch web service is Successfully ended !");
        return realPersonDtos;

    }

    public List<LegalPersonDto> legalSearch(SearchDto searchDto) throws Exception {
        logger.info("realPersonSearch web service is starting !");
        List<LegalPersonEntity> legalPersonEntities = customerServiceController.legalSearch(searchDto);
        List<LegalPersonDto> legalPersonDtos = new ArrayList<>();
        for (LegalPersonEntity legalPersonEntity : legalPersonEntities) {
            legalPersonDtos.add(CustomMapper.objectMapper(new LegalPersonDto(), legalPersonEntity));
        }
        logger.info("realPersonSearch web service is Successfully ended !");
        return legalPersonDtos;

    }

    @Transactional(rollbackOn = Exception.class)
    public void rejection(BankFacilitiesDto bankFacilitiesDto) throws TransactionException {
        customerServiceController.rejection(bankFacilitiesDto);
    }


    @Transactional(rollbackOn = Exception.class)
    public void payment(BankFacilitiesDto bankFacilitiesDto) throws TransactionException {
        customerServiceController.payment(bankFacilitiesDto);
    }

    @Transactional(rollbackOn = Exception.class)
    public void transfer(UiTransactionDto uiTransactionDto) throws TransactionException {
        logger.info("Transfer Transaction Started!");
        transactionServiceController.transfer(uiTransactionDto);
        logger.info("Transfer Transaction Ended Successfully!");

    }

    @Transactional(rollbackOn = Exception.class)
    public void deposit(UiTransactionDto uiTransactionDto) throws TransactionException {
        logger.info("Transfer Transaction Started!");
        transactionServiceController.deposit(uiTransactionDto);
        logger.info("Deposit Transaction Ended Successfully!");

    }


    public AccountEntity search(String accountNumber) throws Exception {
        logger.info("search web service is starting !");
        AccountEntity accountEntity = transactionServiceController.search(accountNumber);
        logger.info("search web service is Successfully ended !");
        return accountEntity;
    }

    @Transactional(rollbackOn = Exception.class)
    public void removal(UiTransactionDto uiTransactionDto) throws TransactionException {
        logger.info("Removal Transaction Started!");
        transactionServiceController.removal(uiTransactionDto);
        logger.info("Removal Transaction Ended Successfully!");
    }
}
