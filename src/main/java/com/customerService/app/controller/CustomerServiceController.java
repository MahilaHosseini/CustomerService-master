package com.customerService.app.controller;

import com.customerService.app.DemoApplication;
import com.customerService.app.model.dao.AccountDao;
import com.customerService.app.model.dao.LegalPersonDao;
import com.customerService.app.model.dao.PersonDao;
import com.customerService.app.model.dao.RealPersonDao;
import com.customerService.app.model.entity.*;
import com.customerService.app.dto.*;
import com.customerService.app.utility.*;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Component
public class CustomerServiceController implements EnvironmentAware {
    private static Logger logger = LoggerFactory.getLogger(DemoApplication.class);
    private PersonDao personDao;
    private RealPersonDao realPersonDao;
    private LegalPersonDao legalPersonDao;
    private AccountDao accountDao;
    private TransactionServiceController transactionServiceController;


    private Environment environment;

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    @Autowired
    private TaskService taskService;


    public CustomerServiceController(PersonDao personDao, RealPersonDao realPersonDao, LegalPersonDao legalPersonDao, TransactionServiceController transactionServiceController, AccountDao accountDao) {
        this.personDao = personDao;
        this.realPersonDao = realPersonDao;
        this.legalPersonDao = legalPersonDao;
        this.transactionServiceController = transactionServiceController;
        this.accountDao = accountDao;
    }


    public void addContact(RealPersonEntity realPersonEntity) throws RealPersonException {
        logger.info("addContact service is running!");
        CustomerValidationUtility.realPersonValidation(realPersonEntity);
        if (Objects.isNull(realPersonDao.findByNationalCode(realPersonEntity.getNationalCode()))) {
            personDao.save(realPersonEntity);
            logger.info("AddContact Web Service Successfully Ended !");
        } else {
            logger.error("AddContact Web Service Is Exiting With  *Duplicate NationalCode*  Error");
            throw new RealPersonException(environment.getProperty("customerService.error.person.realDuplication"));
        }
    }

    public void addLegalContact(LegalPersonEntity legalPersonEntity) throws LegalPersonException {
        logger.info("addLegalContact web service Successfully ended !");
        CustomerValidationUtility.legalPersonValidation(legalPersonEntity);
        if (Objects.isNull(legalPersonDao.findByRegistrationCode(legalPersonEntity.getRegistrationCode()))) {
            personDao.save(legalPersonEntity);
            logger.info("addLegalContact web service Successfully ended !");
        } else {
            logger.error("addLegalContact web service is exiting with  *Duplicate RegistrationCode*  error!");
            throw new LegalPersonException(environment.getProperty("customerService.error.person.legalDuplication"));
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void save(RealPersonEntity realPersonEntity) throws RealPersonException, AccountException {
        logger.info("saveRealPerson service is starting !");
        CustomerValidationUtility.realPersonValidation(realPersonEntity);
        CustomerValidationUtility.accountValidation(realPersonEntity.getAccounts());
        realPersonDao.save(realPersonEntity);
        logger.info("saveRealPerson web service Successfully ended !");
    }

    @Transactional(rollbackOn = Exception.class)
    public void saveLegal(LegalPersonEntity legalPersonEntity) throws LegalPersonException, AccountException {
        logger.info("SaveLegalPerson Web Service Is Starting !");
        CustomerValidationUtility.legalPersonValidation(legalPersonEntity);
        CustomerValidationUtility.accountValidation(legalPersonEntity.getAccounts());
        personDao.save(legalPersonEntity);
        logger.info("SaveLegalPerson Web Service Has Successfully Ended !");
    }

    public Integer generateAccountNumber(Integer id) {
        Random r = new Random();
        return r.nextInt(500000000) + 1000000000 + id;
    }

    @Transactional(rollbackOn = Exception.class)
    public void addAccount(UiAccountDto uiAccountDto) throws AccountException {
        TransactionValidationUtility.amountValidation(uiAccountDto.getAccountAmount());
        if (!Objects.isNull(realPersonDao.findByNationalCode(uiAccountDto.getCode()))) {
            RealPersonEntity realPersonEntity = realPersonDao.findByNationalCode(uiAccountDto.getCode());
            AccountEntity account = new AccountEntity(generateAccountNumber(realPersonEntity.getID()).toString(), uiAccountDto.getAccountAmount());
            CustomerValidationUtility.accountGenerationValidation(account);
            realPersonEntity.addAccountEntity(account);
            System.out.println("hi");
        } else if (!Objects.isNull(legalPersonDao.findByRegistrationCode(uiAccountDto.getCode()))) {
            LegalPersonEntity legalPersonEntity = legalPersonDao.findByRegistrationCode(uiAccountDto.getCode());
            AccountEntity account = new AccountEntity(generateAccountNumber(legalPersonEntity.getID()).toString(), uiAccountDto.getAccountAmount());
            CustomerValidationUtility.accountGenerationValidation(account);
            legalPersonEntity.addAccountEntity(account);
        }
    }


    @Transactional(rollbackOn = Exception.class)
    public void deleteLegal(LegalPersonEntity legalPersonEntity) throws Exception {
        logger.info("DeleteLegalPerson Web Service Is Starting !");
        for (AccountEntity account : legalPersonEntity.getAccounts()) {
            if (accountDao.findByAccountNumber(account.getAccountNumber()).getAccountAmount().compareTo(BigDecimal.ZERO) != 0) {
                logger.error("Can't Delete Customer Due To *Not Empty Bank Account* : Account Number " + account.getAccountNumber());
                throw new Exception(environment.getProperty("customerService.error.person.notEmptyAccount") + account.getAccountNumber());
            }
        }
        personDao.delete(legalPersonEntity);
        logger.info("DeleteLegalPerson Web Service Is Successfully Ended");

    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(RealPersonEntity realPersonEntity) throws Exception {
        logger.info("DeleteRealPerson Service Is Starting");
        for (AccountEntity account : realPersonEntity.getAccounts()) {
            if (accountDao.findByAccountNumber(account.getAccountNumber()).getAccountAmount().compareTo(BigDecimal.ZERO) != 0) {
                logger.error("Can't Delete Customer Due To *Not Empty Bank Account* : Account Number " + account.getAccountNumber());
                throw new Exception(environment.getProperty("customerService.error.person.notEmptyAccount") + account.getAccountNumber());
            }
        }
        personDao.delete(realPersonEntity);
        logger.info("DeleteRealPerson Web Service Is Successfully Ended !");

    }

    public RealPersonEntity uniqueRealSearch(String nationalCode) throws Exception {
        logger.info("UniqueRealSearch Web Service Is Starting !");
        RealPersonEntity realPersonEntity = realPersonDao.findByNationalCode(nationalCode);
        if (!Objects.isNull(realPersonEntity)) {
            logger.info("UniqueRealSearch Web Service Is Successfully Ended !");
            return realPersonEntity;

        } else {
            logger.error("UniqueRealSearch Web Service Is Exiting With  *NO Such Customer*  Error !");
            throw new Exception(environment.getProperty("customerService.error.person.noCustomer"));
        }
    }

    public LegalPersonEntity uniqueLegalSearch(String registrationCode) throws Exception {
        logger.info("UniqueLegalSearch Web Service Is Starting !");
        LegalPersonEntity legalPersonEntity = legalPersonDao.findByRegistrationCode(registrationCode);
        if (!Objects.isNull(legalPersonEntity)) {
            logger.info("UniqueLegalSearch Web Service Is Successfully Ended !");
            return legalPersonEntity;
        } else {
            logger.error("UniqueRealSearch Web Service Is Exiting With  *NO Such Customer*  Error !");
            throw new Exception(environment.getProperty("customerService.error.person.noCustomer"));
        }
    }

    public List<RealPersonEntity> realSearch(SearchDto searchDto) throws Exception {
        logger.info("realPersonSearch web service is starting !");
        List<RealPersonEntity> realPersonEntities = realPersonDao.findByName(searchDto.getName());
        if (!Objects.isNull(realPersonEntities)) {
            logger.info("realPersonSearch web service is Successfully ended !");
            return realPersonEntities;
        } else {
            logger.error("realPersonSearch Web Service Is Exiting With  *NO Such Customer*  Error !");
            throw new Exception(environment.getProperty("customerService.error.person.noCustomer"));
        }
    }

    public List<LegalPersonEntity> legalSearch(SearchDto searchDto) throws Exception {
        logger.info("legalPersonSearch web service is starting !");
        List<LegalPersonEntity> legalPersonEntities = legalPersonDao.findByName(searchDto.getName());
        if (!Objects.isNull(legalPersonEntities)) {
            logger.info("legalPersonSearch web service is Successfully ended !");
            return legalPersonEntities;
        } else {
            logger.error("legalPersonSearch Web Service Is Exiting With  *NO Such Customer*  Error !");
            throw new Exception(environment.getProperty("customerService.error.person.noCustomer"));
        }

    }

    @Transactional(rollbackOn = Exception.class)
    public void rejection(BankFacilitiesDto bankFacilitiesDto) throws TransactionException {
        if (!Objects.isNull(bankFacilitiesDto)) {
            TransactionValidationUtility.validateFacilityDto(bankFacilitiesDto, accountDao);
            FacilityEntity facilityEntity = new FacilityEntity(bankFacilitiesDto.getFacilityType(), bankFacilitiesDto.getAmount(), "رد شده");
            accountDao.findByAccountNumber(bankFacilitiesDto.getAccountNumber()).addFacility(facilityEntity);
            taskService.complete(bankFacilitiesDto.getTaskId());
            logger.info("rejecting Facility demand done successfully!");
        } else {
            logger.error("Error rejecting : null inputBankFacilitiesDto");
            throw new TransactionException(environment.getProperty("customerService.error.transaction.nullFacility"));
        }
    }


    @Transactional(rollbackOn = Exception.class)
    public void payment(BankFacilitiesDto bankFacilitiesDto) throws TransactionException {
        if (!Objects.isNull(bankFacilitiesDto)) {
            TransactionValidationUtility.validateFacilityDto(bankFacilitiesDto, accountDao);
            UiTransactionDto uiTransactionDto = new UiTransactionDto(bankFacilitiesDto.getAccountNumber(), bankFacilitiesDto.getAmount());
            TransactionValidationUtility.validateDeposit(uiTransactionDto, accountDao);
            transactionServiceController.deposit(uiTransactionDto);
            FacilityEntity facilityEntity = new FacilityEntity(bankFacilitiesDto.getFacilityType(), bankFacilitiesDto.getAmount(), "تائيد و واريز شده");
            accountDao.findByAccountNumber(bankFacilitiesDto.getAccountNumber()).addFacility(facilityEntity);
            taskService.complete(bankFacilitiesDto.getTaskId());
            logger.info("Depositing Facility Demand Done Successfully!");
        } else {
            logger.error("Error rejecting : null inputBankFacilitiesDto");
            throw new TransactionException(environment.getProperty("customerService.error.transaction.nullFacility"));
        }
    }

}
