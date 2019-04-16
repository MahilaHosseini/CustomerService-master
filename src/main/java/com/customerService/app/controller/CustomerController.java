package com.customerService.app.controller;

import com.customerService.app.DemoApplication;
import com.customerService.app.model.dao.AccountDao;
import com.customerService.app.model.dao.LegalPersonDao;
import com.customerService.app.model.dao.PersonDao;
import com.customerService.app.model.dao.RealPersonDao;
import com.customerService.app.model.entity.AccountEntity;
import com.customerService.app.model.entity.LegalPersonEntity;
import com.customerService.app.model.entity.PersonEntity;
import com.customerService.app.model.entity.RealPersonEntity;
import com.customerService.app.dto.*;
import com.customerService.app.dto.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class CustomerController {
    private static Logger logger = LoggerFactory.getLogger(DemoApplication.class);
    private PersonDao personDao;
    private RealPersonDao realPersonDao;
    private LegalPersonDao legalPersonDao;
    private AccountDao accountDao;
    private Integer accountNumber = 1000100010;

    public CustomerController(PersonDao personDao, RealPersonDao realPersonDao, LegalPersonDao legalPersonDao, AccountDao accountDao) {
        this.personDao = personDao;
        this.realPersonDao = realPersonDao;
        this.legalPersonDao = legalPersonDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(value = "/ws/login", method = RequestMethod.GET)
    public ResponseDto<AfterLoginInfoDto> loginSuccess() {
        AfterLoginInfoDto afterLoginInfoDto = new AfterLoginInfoDto();
        MenuItmDto menuItmDto = new MenuItmDto(null, null, null, new ArrayList<MenuItmDto>(Arrays.asList(
                new MenuItmDto(MenuItemType.MENU, "سامانه مشتریان", null, new ArrayList<MenuItmDto>(Arrays.asList(
                        new MenuItmDto(MenuItemType.MENU, "افزودن مشتری جدید", null, new ArrayList<MenuItmDto>(Arrays.asList(
                                new MenuItmDto(MenuItemType.PAGE, "افزودن مشتری حقیقی", new UIPageDto(null, "addReal"), new ArrayList<MenuItmDto>()),
                                new MenuItmDto(MenuItemType.PAGE, "افزودن مشتری حقوقی", new UIPageDto(null, "addLegal"), new ArrayList<MenuItmDto>())
                        ))),
                        new MenuItmDto(MenuItemType.MENU, " ویرایش مشتریان", null, new ArrayList<MenuItmDto>(Arrays.asList(
                                new MenuItmDto(MenuItemType.PAGE, "ویرایش مشتری حقیقی", new UIPageDto(null, "realPersonSearch"), new ArrayList<MenuItmDto>()),
                                new MenuItmDto(MenuItemType.PAGE, "ویرایش مشتری حقوقی", new UIPageDto(null, "legalPersonSearch"), new ArrayList<MenuItmDto>())
                        ))),
                        new MenuItmDto(MenuItemType.MENU, " نمایش", null, new ArrayList<MenuItmDto>(Arrays.asList(
                                new MenuItmDto(MenuItemType.PAGE, "نمایش مشتری حقیقی", new UIPageDto(null, "showReal"), new ArrayList<MenuItmDto>()),
                                new MenuItmDto(MenuItemType.PAGE, "نمایش مشتری حقوقی", new UIPageDto(null, "showLegal"), new ArrayList<MenuItmDto>())
                        ))),
                        new MenuItmDto(MenuItemType.MENU, " حذف", null, new ArrayList<MenuItmDto>(Arrays.asList(
                                new MenuItmDto(MenuItemType.PAGE, "حذف مشتری حقیقی", new UIPageDto(null, "deleteReal"), new ArrayList<MenuItmDto>()),
                                new MenuItmDto(MenuItemType.PAGE, "حذف مشتری حقوقی", new UIPageDto(null, "deleteLegal"), new ArrayList<MenuItmDto>())
                        ))),
                        new MenuItmDto(MenuItemType.MENU, "انجام تراکنش", null, new ArrayList<MenuItmDto>(Arrays.asList(
                                new MenuItmDto(MenuItemType.PAGE, "انتقال وجه ", new UIPageDto(null, "transfer"), new ArrayList<MenuItmDto>()),
                                new MenuItmDto(MenuItemType.PAGE, "واریز وجه ", new UIPageDto(null, "deposit"), new ArrayList<MenuItmDto>()),
                                new MenuItmDto(MenuItemType.PAGE, "برداشت وجه ", new UIPageDto(null, "removal"), new ArrayList<MenuItmDto>())
                        ))),
                        new MenuItmDto(MenuItemType.PAGE, "افزودن حساب", new UIPageDto(null, "addAccount"), new ArrayList<MenuItmDto>())

                )))
        )));
        afterLoginInfoDto.setMenu(menuItmDto);
        return new ResponseDto(ResponseStatus.Ok, afterLoginInfoDto, null, null);
    }

    @RequestMapping(value = "/pws/uipage/getPage", method = RequestMethod.POST)
    public ResponseDto<String> getPage(@RequestParam String name) throws IOException {
        return new ResponseDto(ResponseStatus.Ok, readFile(name, StandardCharsets.UTF_8), null, null);
    }

    private String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(new ClassPathResource(path + ".xml").getFile().getPath()));
        return new String(encoded, encoding);
    }

    @RequestMapping(value = "/pws/error", method = RequestMethod.GET)
    public ResponseDto error(@RequestParam String code) {
        switch (code) {
            case "loginFailure":
                return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("نام کاربری یا کلمه عبور درست وارد نشده"));
            default:
                return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("خطای سیستمی رخ داده است"));
        }
    }

    @RequestMapping(value = "/ws/logout", method = RequestMethod.POST)
    public ResponseDto<String> logOut() {
        return new ResponseDto(ResponseStatus.Ok, null, "Bye!", null);
    }


    @RequestMapping(value = "/ws/addRealContact", method = RequestMethod.POST)
    public ResponseDto<RealPersonEntity> addContact(@RequestBody RealPersonEntity realPersonEntity) {
        logger.info("addContact web service is running!");
        try {
            if (CustomerValidationUtility.realPersonValidation(realPersonEntity)) {
                if (Objects.isNull(realPersonDao.findByNationalCode(realPersonEntity.getNationalCode()))) {
                    createAccount(realPersonEntity);
                    personDao.save(realPersonEntity);
                    logger.info("addContact web service Successfully ended !");
                    return new ResponseDto(ResponseStatus.Ok, null, "Successfully Added", null);
                }
                logger.error("addContact web service is exiting with  *Duplicate National code*  error!");
                return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Duplicate National code"));

            }
        } catch (Exception errorMessage) {
            logger.error("addContact web service is exiting with errors: " + errorMessage.getMessage());
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(errorMessage.getMessage()));
        }
        logger.error("addContact web service is exiting with errors: Unexpected Error Has Occurred");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Unexpected Error Has Occurred"));

    }

    @RequestMapping(value = "/ws/addLegalContact", method = RequestMethod.POST)
    public ResponseDto<LegalPersonEntity> addLegalContact(@RequestBody LegalPersonEntity legalPersonEntity) {
        logger.info("addLegalContact web service Successfully ended !");
        try {
            if (CustomerValidationUtility.legalPersonValidation(legalPersonEntity)) {
                if (Objects.isNull(legalPersonDao.findByRegistrationCode(legalPersonEntity.getRegistrationCode()))) {
                    personDao.save(legalPersonEntity);
                    logger.info("addLegalContact web service Successfully ended !");
                    return new ResponseDto(ResponseStatus.Ok, null, "Successfully Added", null);
                }
                logger.error("addLegalContact web service is exiting with  *Duplicate Registration code*  error!");
                return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Duplicate Registration Code"));

            }
        } catch (Exception errorMessage) {
            logger.error("addLegalContact web service is exiting with errors : " + errorMessage.getMessage());
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(errorMessage.getMessage()));
        }
        logger.error("addContact web service is exiting with errors: Unexpected Error Has Occurred");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Unexpected Error Has Occurred"));
    }

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/saveRealPerson", method = RequestMethod.POST)
    public ResponseDto<RealPersonEntity> save(@RequestBody RealPersonEntity realPersonEntity) {

        logger.info("saveRealPerson web service is starting !");
        try {
            if (CustomerValidationUtility.realPersonValidation(realPersonEntity) && CustomerValidationUtility.accountValidation(realPersonEntity.getAccountEntities())) {
                try {

                    realPersonDao.save(realPersonEntity);
                } catch (Exception e) {
                    logger.error("saveRealPerson web service is exiting with errors : " + e);
                    return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Version Conflict!"));
                }
                logger.info("saveRealPerson web service Successfully ended !");
                return new ResponseDto(ResponseStatus.Ok, null, "Successfully Edited", null);
            }
        } catch (Exception errorMessage) {
            logger.error("saveRealPerson web service is exiting with errors : " + errorMessage.getMessage());
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(errorMessage.getMessage()));
        }
        logger.error("addContact web service is exiting with errors: Unexpected Error Has Occurred");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Unexpected Error Has Occurred"));
    }


    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/addAccount", method = RequestMethod.POST)
    public ResponseDto<PersonEntity> addAccount(@RequestBody AccountDto accountDto) throws Exception {
        try {
            if (!Objects.isNull(realPersonDao.findByNationalCode(accountDto.getCode()))) {
                RealPersonEntity realPersonEntity = realPersonDao.findByNationalCode(accountDto.getCode());
                accountNumber++;
                AccountEntity account = new AccountEntity(accountNumber.toString(), accountDto.getAccountAmount());
                realPersonEntity.addAccountEntity(account);
                return new ResponseDto(ResponseStatus.Ok, null, " RealPerson Account Successfully Added", null);

            }
            else if (!Objects.isNull(legalPersonDao.findByRegistrationCode(accountDto.getCode()))){
                LegalPersonEntity legalPersonEntity =legalPersonDao.findByRegistrationCode(accountDto.getCode());
                accountNumber++;
                AccountEntity account = new AccountEntity(accountNumber.toString(), accountDto.getAccountAmount());
                legalPersonEntity.addAccountEntity(account);
                return new ResponseDto(ResponseStatus.Ok, null, " LegalPerson Account Successfully Added", null);
            }
            else {
                return new ResponseDto(ResponseStatus.Ok, null, null, new ResponseException("No Such Customer!"));

            }
        } catch (Exception e) {
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Couldn't Create Account"));

        }

    }

    //ServiceTask
    public void createAccount(PersonEntity personEntity) throws Exception {
        try {
            accountNumber++;
            AccountEntity account = new AccountEntity(accountNumber.toString(), BigDecimal.valueOf(50000));
            personEntity.addAccountEntity(account);
        } catch (Exception e) {
            throw new Exception("Couldn't Create Account");
        }

    }

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/saveLegalPerson", method = RequestMethod.POST)
    public ResponseDto<LegalPersonEntity> saveLegal(@RequestBody LegalPersonEntity legalPersonEntity) {

        logger.info("saveLegalPerson web service is starting !");
        try {
            if (CustomerValidationUtility.legalPersonValidation(legalPersonEntity) && CustomerValidationUtility.accountValidation(legalPersonEntity.getAccountEntities())) {
                try {
                    personDao.save(legalPersonEntity);
                } catch (Exception e) {
                    logger.error("saveLegalPerson web service is exiting with errors : " + e);
                    return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Version Conflict!"));
                }
                logger.info("saveLegalPerson web service is Successfully ended !");
                return new ResponseDto(ResponseStatus.Ok, null, "Successfully Edited", null);
            }
        } catch (Exception errorMassage) {
            logger.error("saveLegalPerson web service is exiting with errors : " + errorMassage.getMessage());
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(errorMassage.getMessage()));
        }
        logger.error("addContact web service is exiting with errors: Unexpected Error Has Occurred");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Unexpected Error Has Occurred"));
    }


    @RequestMapping(value = "/ws/uniqueRealSearch", method = RequestMethod.POST)
    public ResponseDto<RealPersonEntity> uniqueRealSearch(@RequestParam String nationalCode) {
        logger.info("uniqueRealSearch web service is starting !");
        RealPersonEntity realPersonEntity = realPersonDao.findByNationalCode(nationalCode);
        if (!Objects.isNull(realPersonEntity)) {
            logger.info("uniqueRealSearch web service is Successfully ended !");
            return new ResponseDto(ResponseStatus.Ok, realPersonEntity, null, null);
        }
        logger.error("uniqueRealSearch web service is exiting with  *NO Such Customer*  error !");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("NO Such Customer"));
    }

    @RequestMapping(value = "/ws/uniqueLegalSearch", method = RequestMethod.POST)
    public ResponseDto<LegalPersonEntity> uniqueLegalSearch(@RequestParam String registrationCode) {
        logger.info("uniqueLegalSearch web service is starting !");
        LegalPersonEntity legalPersonEntity = legalPersonDao.findByRegistrationCode(registrationCode);
        if (!Objects.isNull(legalPersonEntity)) {
            logger.info("uniqueLegalSearch web service is Successfully ended !");
            return new ResponseDto(ResponseStatus.Ok, legalPersonEntity, null, null);
        }
        logger.error("uniqueLegalSearch web service is exiting with  *NO Such Customer*  error !");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("NO Such Customer"));
    }

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/deleteLegalPerson", method = RequestMethod.POST)
    public ResponseDto<LegalPersonEntity> deleteLegal(@RequestBody LegalPersonEntity legalPersonEntity) {
        logger.info("deleteLegalPerson web service is starting !");
        try {
            personDao.delete(legalPersonEntity);
            logger.info("deleteLegalPerson web service is Successfully ended !");
            return new ResponseDto(ResponseStatus.Ok, null, "Successfully Deleted!", null);
        } catch (Exception e) {
            logger.error("deleteLegalPerson web service is exiting with errors : " + e);
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("error "));
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/deleteRealPerson", method = RequestMethod.POST)
    public ResponseDto<RealPersonEntity> delete(@RequestBody RealPersonEntity realPersonEntity) {
        logger.info("deleteRealPerson web service is starting !");
        try {
            personDao.delete(realPersonEntity);
            logger.info("deleteRealPerson web service is Successfully ended !");
            return new ResponseDto(ResponseStatus.Ok, null, "Successfully Deleted!", null);
        } catch (Exception e) {
            logger.error("deleteRealPerson web service is exiting with errors : " + e);
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("error "));
        }
    }

    @RequestMapping(value = "/ws/realPersonSearch", method = RequestMethod.POST)
    public ResponseDto<List<RealPersonEntity>> realSearch(@RequestBody SearchDto searchDto) {
        logger.info("realPersonSearch web service is starting !");
        List<RealPersonEntity> realPersonEntities = realPersonDao.findByName(searchDto.getName());
        if (!Objects.isNull(realPersonEntities)) {
            logger.info("realPersonSearch web service is Successfully ended !");
            return new ResponseDto(ResponseStatus.Ok, realPersonEntities, null, null);
        }
        logger.error("realPersonSearch web service is exiting with  *NO Such Customer*  error !");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("NO Such Customer"));
    }

    @RequestMapping(value = "/ws/legalPersonSearch", method = RequestMethod.POST)
    public ResponseDto<List<LegalPersonEntity>> legalSearch(@RequestBody SearchDto searchDto) {
        logger.info("legalPersonSearch web service is starting !");
        List<LegalPersonEntity> legalPersonEntities = legalPersonDao.findByName(searchDto.getName());
        if (!Objects.isNull(legalPersonEntities)) {
            logger.info("legalPersonSearch web service is Successfully ended !");
            return new ResponseDto(ResponseStatus.Ok, legalPersonEntities, null, null);
        }
        logger.error("legalPersonSearch web service is exiting with  *NO Such Customer*  error !");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("NO Such Customer"));
    }


}
