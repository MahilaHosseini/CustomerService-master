package com.customerService.app.controller;

import com.customerService.app.DemoApplication;
import com.customerService.app.model.dao.AccountDao;
import com.customerService.app.model.dao.LegalPersonDao;
import com.customerService.app.model.dao.PersonDao;
import com.customerService.app.model.dao.RealPersonDao;
import com.customerService.app.model.entity.*;
import com.customerService.app.dto.*;
import com.customerService.app.dto.ResponseStatus;
import com.customerService.app.utility.*;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private TransactionController transactionController;

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    public CustomerController(PersonDao personDao, RealPersonDao realPersonDao, LegalPersonDao legalPersonDao, TransactionController transactionController, AccountDao accountDao) {
        this.personDao = personDao;
        this.realPersonDao = realPersonDao;
        this.legalPersonDao = legalPersonDao;
        this.transactionController = transactionController;
        this.accountDao = accountDao;
    }

    @RequestMapping(value = "/ws/login", method = RequestMethod.GET)
    public ResponseDto<AfterLoginInfoDto> loginSuccess() {
        AfterLoginInfoDto afterLoginInfoDto = new AfterLoginInfoDto();
        MenuItmDto menuItmDto;
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Teller")))
            menuItmDto = new MenuItmDto(null, null, null, new ArrayList<MenuItmDto>(Arrays.asList(
                    new MenuItmDto(MenuItemType.PAGE, "کارتابل ", new UIPageDto(null, "showTasks"), new ArrayList<MenuItmDto>()),
                    new MenuItmDto(MenuItemType.MENU, "سامانه مشتریان", null, new ArrayList<MenuItmDto>(Arrays.asList(
                            new MenuItmDto(MenuItemType.MENU, "افزودن مشتری جدید", null, new ArrayList<MenuItmDto>(Arrays.asList(
                                    new MenuItmDto(MenuItemType.PAGE, "افزودن مشتری حقیقی", new UIPageDto(null, "addReal"), new ArrayList<MenuItmDto>()),
                                    new MenuItmDto(MenuItemType.PAGE, "افزودن مشتری حقوقی", new UIPageDto(null, "addLegal"), new ArrayList<MenuItmDto>())
                            ))),
                            new MenuItmDto(MenuItemType.PAGE, "افزودن حساب", new UIPageDto(null, "addAccount"), new ArrayList<MenuItmDto>()),
                            new MenuItmDto(MenuItemType.PAGE, "نمايش حساب ", new UIPageDto(null, "showAccountInfo"), new ArrayList<MenuItmDto>()),
                            new MenuItmDto(MenuItemType.PAGE, "درخواست تسهيلات ", new UIPageDto(null, "facility"), new ArrayList<MenuItmDto>()),
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
                            )))
                    )))
            )));

        else
            menuItmDto = new MenuItmDto(null, null, null, new ArrayList<MenuItmDto>(Arrays.asList(
                    new MenuItmDto(MenuItemType.PAGE, "کارتابل ", new UIPageDto(null, "showTasks"), new ArrayList<MenuItmDto>()))));


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
                logger.error("Wrong UserName Or Password");
                return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Wrong UserName Or Password"));
            default:
                logger.error("Login System Error");
                return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("System Error"));
        }
    }

    @RequestMapping(value = "/ws/logout", method = RequestMethod.POST)
    public ResponseDto<String> logOut() {
        return new ResponseDto(ResponseStatus.Ok, null, "Bye!", null);
    }


    @RequestMapping(value = "/ws/addRealContact", method = RequestMethod.POST)
    public ResponseDto<RealPersonEntity> addContact(@RequestBody RealPersonDto realPersonDto) throws RealPersonException {
   // public ResponseDto<RealPersonEntity> addContact(@RequestBody RealPersonEntity realPersonEntity) throws RealPersonException {
        logger.info("addContact web service is running!");
        RealPersonEntity realPersonEntity = new RealPersonEntity();
        realPersonEntity = MapperClass.mapper( realPersonEntity ,realPersonDto);
        CustomerValidationUtility.realPersonValidation(realPersonEntity);
        if (Objects.isNull(realPersonDao.findByNationalCode(realPersonEntity.getNationalCode()))) {
            personDao.save(realPersonEntity);
            logger.info("AddContact Web Service Successfully Ended !");
            return new ResponseDto(ResponseStatus.Ok, null, "Successfully Added", null);
        } else {
            logger.error("AddContact Web Service Is Exiting With  *Duplicate NationalCode*  Error");
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Duplicate NationalCode"));
        }
    }

    @RequestMapping(value = "/ws/addLegalContact", method = RequestMethod.POST)
    public ResponseDto<LegalPersonEntity> addLegalContact(@RequestBody LegalPersonEntity legalPersonEntity) throws LegalPersonException {
        logger.info("addLegalContact web service Successfully ended !");
        CustomerValidationUtility.legalPersonValidation(legalPersonEntity);
        if (Objects.isNull(legalPersonDao.findByRegistrationCode(legalPersonEntity.getRegistrationCode()))) {
            personDao.save(legalPersonEntity);
            logger.info("addLegalContact web service Successfully ended !");
            return new ResponseDto(ResponseStatus.Ok, null, "Successfully Added", null);
        } else {
            logger.error("addLegalContact web service is exiting with  *Duplicate Registration code*  error!");
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Duplicate Registration Code"));

        }
    }

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/saveRealPerson", method = RequestMethod.POST)
    public ResponseDto<RealPersonEntity> save(@RequestBody RealPersonEntity realPersonEntity) throws RealPersonException, AccountException {

        logger.info("saveRealPerson web service is starting !");

        CustomerValidationUtility.realPersonValidation(realPersonEntity);
        CustomerValidationUtility.accountValidation(realPersonEntity.getAccountEntities());
        try {
            realPersonDao.save(realPersonEntity);
        } catch (Exception e) {
            logger.error("saveRealPerson web service is exiting with errors : " + e);
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Version Conflict!"));
        }
        logger.info("saveRealPerson web service Successfully ended !");
        return new ResponseDto(ResponseStatus.Ok, null, "Successfully Edited", null);


    }

    public Integer generateAccountNumber(Integer id) {
        Random r = new Random();
        return r.nextInt(500000000) + 1000000000 + id;
    }

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/addAccount", method = RequestMethod.POST)
    public ResponseDto<PersonEntity> addAccount(@RequestBody UiAccountDto uiAccountDto) {

        if (!Objects.isNull(realPersonDao.findByNationalCode(uiAccountDto.getCode()))) {
            RealPersonEntity realPersonEntity = realPersonDao.findByNationalCode(uiAccountDto.getCode());
            AccountEntity account = new AccountEntity(generateAccountNumber(realPersonEntity.getID()).toString(), uiAccountDto.getAccountAmount());
            realPersonEntity.addAccountEntity(account);
            return new ResponseDto(ResponseStatus.Ok, null, " RealPerson Account Successfully Added", null);

        } else if (!Objects.isNull(legalPersonDao.findByRegistrationCode(uiAccountDto.getCode()))) {
            LegalPersonEntity legalPersonEntity = legalPersonDao.findByRegistrationCode(uiAccountDto.getCode());
            AccountEntity account = new AccountEntity(generateAccountNumber(legalPersonEntity.getID()).toString(), uiAccountDto.getAccountAmount());
            legalPersonEntity.addAccountEntity(account);
            return new ResponseDto(ResponseStatus.Ok, null, " LegalPerson Account Successfully Added", null);
        } else {
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("No Such Customer!"));
        }


    }

    @RequestMapping(value = "/ws/activiti/startProcess", method = RequestMethod.POST)
    public ResponseDto startProcess(@RequestBody BankFacilitiesDto bankFacilitiesDto) throws TransactionException {
        if (TransactionValidationUtility.validateFacility(bankFacilitiesDto, accountDao)) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("bankFacilityAccNum", bankFacilitiesDto.getAccountNumber());
            variables.put("bankFacilityAccAmount", bankFacilitiesDto.getAmount());
            variables.put("bankFacilityType", bankFacilitiesDto.getFacilityType());
            variables.put("bankFacilityId", bankFacilitiesDto.getTaskId());
            runtimeService.startProcessInstanceByKey("Facility", variables);
            logger.info("Activiti Process Started successfully !");
            return new ResponseDto(ResponseStatus.Ok, null, "فرایند آغاز شد.", null);
        } else {
            logger.error("Activiti Process exited with error  !");
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Error Srarting Activiti process!"));
        }
    }

    @RequestMapping(value = "/pws/activiti/getUrlByFormKey", method = RequestMethod.POST)
    public ResponseDto<String> getUrlByFormKey(@RequestParam String formKey) {
        String url = "";
        if (!Objects.isNull(formKey)) {
            switch (formKey) {
                case "BranchChief":
                    url = "approveBranchTask";
                    break;
                case "Teller":
                    url = "rejection";
                    break;
                case "Cashier":
                    url = "payment";
                    break;
            }
            logger.info("url retrieved !");
            return new ResponseDto(ResponseStatus.Ok, url, "successfully redirected", null);
        } else {
            logger.error("Error getting Url By FormKey : null formKey");
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Error getting Url By FormKey : null formKey"));
        }
    }


    @RequestMapping(value = "/ws/activiti/getTasks", method = RequestMethod.POST)
    public ResponseDto<List<TaskDto>> getTasks() {
        logger.info("Getting Activiti tasks !");
        List<Task> list = taskService.createTaskQuery().taskAssignee(getUsername()).list();
        List<TaskDto> taskDtos = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TaskDto taskDto = new TaskDto();
            taskDto.setTaskId(list.get(i).getId());
            taskDto.setName(list.get(i).getName());
            taskDto.setFormKey(list.get(i).getFormKey());
            taskDtos.add(taskDto);
        }
        logger.info("Tasks list is retrieved successfully !");
        return new ResponseDto(ResponseStatus.Ok, taskDtos, null, null);
    }

    @RequestMapping(value = "/ws/plan/getTaskByTaskId", method = RequestMethod.POST)
    public ResponseDto<BankFacilitiesDto> getTaskByTaskId(@RequestParam String taskId) {

        if (!Objects.isNull(taskId)) {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            Map<String, Object> taskLocalVariables = runtimeService.getVariables(task.getProcessInstanceId());
            BankFacilitiesDto bankFacilitiesDto = new BankFacilitiesDto(taskLocalVariables.get("bankFacilityType").toString(), new BigDecimal(taskLocalVariables.get("bankFacilityAccAmount").toString()), (String) taskLocalVariables.get("bankFacilityAccNum"));
            bankFacilitiesDto.setTaskId(taskId);
            if (!Objects.isNull(taskLocalVariables.get("grant")))
                if (taskLocalVariables.get("grant").toString().equals("1"))
                    bankFacilitiesDto.setApprove("approved");
                else
                    bankFacilitiesDto.setApprove("rejected");
            logger.info("Task Is Retrieved !");
            return new ResponseDto(ResponseStatus.Ok, bankFacilitiesDto, null, null);
        } else {
            logger.error("Error Getting Task By TaskId : null TaskId");
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Error Getting Task By TaskId : null TaskId"));
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/activiti/rejection", method = RequestMethod.POST)
    public ResponseDto rejection(@RequestBody BankFacilitiesDto bankFacilitiesDto) {
        if (!Objects.isNull(bankFacilitiesDto)) {
            FacilityEntity facilityEntity = new FacilityEntity(bankFacilitiesDto.getFacilityType(), bankFacilitiesDto.getAmount(), "رد شده");
            accountDao.findByAccountNumber(bankFacilitiesDto.getAccountNumber()).addFacility(facilityEntity);
            taskService.complete(bankFacilitiesDto.getTaskId());
            logger.info("rejecting Facility demand done successfully!");
            return new ResponseDto(ResponseStatus.Ok, null, "Sent", null);
        } else {
            logger.error("Error rejecting : null inputBankFacilitiesDto");
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Error Rejecting : null TaskDto"));
        }
    }

    @RequestMapping(value = "/ws/activiti/approveBranchTask", method = RequestMethod.POST)
    public ResponseDto approveBranchTask(@RequestBody BankFacilitiesDto bankFacilitiesDto) {
        Map<String, Object> variables = new HashMap<>();
        if (bankFacilitiesDto.getApprove().equals("true"))
            taskService.setVariable(bankFacilitiesDto.getTaskId(), "grant", 1);
        else
            taskService.setVariable(bankFacilitiesDto.getTaskId(), "grant", 0);
        taskService.complete(bankFacilitiesDto.getTaskId(), variables);
        logger.info("Declaring Facility State Done Successfully!");
        return new ResponseDto(ResponseStatus.Ok, null, "Sent", null);
    }

    @RequestMapping(value = "/ws/activiti/payment", method = RequestMethod.POST)
    @Transactional(rollbackOn = Exception.class)
    public ResponseDto payment(@RequestBody BankFacilitiesDto bankFacilitiesDto) {
        UiTransactionDto uiTransactionDto = new UiTransactionDto(bankFacilitiesDto.getAccountNumber(), bankFacilitiesDto.getAmount());
        transactionController.deposit(uiTransactionDto);
        FacilityEntity facilityEntity = new FacilityEntity(bankFacilitiesDto.getFacilityType(), bankFacilitiesDto.getAmount(), "تائيد و واريز شده");
        accountDao.findByAccountNumber(bankFacilitiesDto.getAccountNumber()).addFacility(facilityEntity);
        taskService.complete(bankFacilitiesDto.getTaskId());
        logger.info("Depositing Facility Demand Done Successfully!");
        return new ResponseDto(ResponseStatus.Ok, null, "Successfully Deposited!", null);
    }

    private String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/saveLegalPerson", method = RequestMethod.POST)
    public ResponseDto<LegalPersonEntity> saveLegal(@RequestBody LegalPersonEntity legalPersonEntity) throws LegalPersonException, AccountException {

        logger.info("SaveLegalPerson Web Service Is Starting !");

        CustomerValidationUtility.legalPersonValidation(legalPersonEntity);
        CustomerValidationUtility.accountValidation(legalPersonEntity.getAccountEntities());
        try {
            personDao.save(legalPersonEntity);
        } catch (Exception e) {
            logger.error("SaveLegalPerson Web Service Is Exiting With Errors : " + e);
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Version Conflict!"));
        }
        logger.info("SaveLegalPerson Web Service Has Successfully Ended !");
        return new ResponseDto(ResponseStatus.Ok, null, "Successfully Edited", null);
    }


    @RequestMapping(value = "/ws/uniqueRealSearch", method = RequestMethod.POST)
    public ResponseDto<RealPersonEntity> uniqueRealSearch(@RequestParam String nationalCode) {
        logger.info("UniqueRealSearch Web Service Is Starting !");
        RealPersonEntity realPersonEntity = realPersonDao.findByNationalCode(nationalCode);
        if (!Objects.isNull(realPersonEntity)) {
            logger.info("UniqueRealSearch Web Service Is Successfully Ended !");
            return new ResponseDto(ResponseStatus.Ok, realPersonEntity, null, null);
        }
        logger.error("UniqueRealSearch Web Service Is Exiting With  *NO Such Customer*  Error !");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("NO Such Customer"));
    }

    @RequestMapping(value = "/ws/uniqueLegalSearch", method = RequestMethod.POST)
    public ResponseDto<LegalPersonEntity> uniqueLegalSearch(@RequestParam String registrationCode) {
        logger.info("UniqueLegalSearch Web Service Is Starting !");
        LegalPersonEntity legalPersonEntity = legalPersonDao.findByRegistrationCode(registrationCode);
        if (!Objects.isNull(legalPersonEntity)) {
            logger.info("UniqueLegalSearch Web Service Is Successfully Ended !");
            return new ResponseDto(ResponseStatus.Ok, legalPersonEntity, null, null);
        }
        logger.error("UniqueLegalSearch Web Service Is Exiting With  *NO Such Customer*  Error !");
        return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("NO Such Customer"));
    }

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/deleteLegalPerson", method = RequestMethod.POST)
    public ResponseDto<LegalPersonEntity> deleteLegal(@RequestBody LegalPersonEntity legalPersonEntity) {
        logger.info("DeleteLegalPerson Web Service Is Starting !");
        try {
            for (AccountEntity account : legalPersonEntity.getAccountEntities()) {
                if (accountDao.findByAccountNumber(account.getAccountNumber()).getAccountAmount().compareTo(BigDecimal.ZERO) != 0) {
                    logger.error("Can't Delete Customer Due To *Not Empty Bank Account* : Account Number " + account.getAccountNumber());
                    return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Can't Delete Customer Due To *Not Empty Bank Account* : Account Number " + account.getAccountNumber()));
                }
            }
            personDao.delete(legalPersonEntity);
            logger.info("DeleteLegalPerson Web Service Is Successfully Ended");
            return new ResponseDto(ResponseStatus.Ok, null, "Successfully Deleted", null);
        } catch (Exception e) {
            logger.error("DeleteLegalPerson Web Service Is Exiting With Errors : " + e);
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Error"));
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/deleteRealPerson", method = RequestMethod.POST)
    public ResponseDto<RealPersonEntity> delete(@RequestBody RealPersonEntity realPersonEntity) {
        logger.info("DeleteRealPerson Web Service Is Starting");
        try {
            for (AccountEntity account : realPersonEntity.getAccountEntities()) {
                if (accountDao.findByAccountNumber(account.getAccountNumber()).getAccountAmount().compareTo(BigDecimal.ZERO) != 0) {
                    logger.error("Can't Delete Customer Due To *Not Empty Bank Account* : Account Number " + account.getAccountNumber());
                    return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Can't Delete Customer Due To *Not Empty Bank Account* : Account Number " + account.getAccountNumber()));
                }
            }
            personDao.delete(realPersonEntity);
            logger.info("DeleteRealPerson Web Service Is Successfully Ended !");
            return new ResponseDto(ResponseStatus.Ok, null, "Successfully Deleted!", null);
        } catch (Exception e) {
            logger.error("DeleteRealPerson Web Service Is Exiting With Errors : " + e);
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException("Error "));
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
