package com.customerService.app.controller;

import com.customerService.app.DemoApplication;
import com.customerService.app.dto.*;
import com.customerService.app.dto.ResponseStatus;
import com.customerService.app.facade.FacadeLayer;
import com.customerService.app.model.dao.AccountDao;
import com.customerService.app.model.entity.*;
import com.customerService.app.utility.*;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
public class WebServiceController {
    private static Logger logger = LoggerFactory.getLogger(DemoApplication.class);
    private AccountDao accountDao;

    private FacadeLayer facade;

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private Environment environment;

    public WebServiceController(FacadeLayer facade, AccountDao accountDao) {
        this.facade = facade;
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
                return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(environment.getProperty("customerService.error.System.userOrPass")));
            default:
                logger.error("Login System Error");
                return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(environment.getProperty("customerService.error.System")));
        }
    }

    @RequestMapping(value = "/ws/logout", method = RequestMethod.POST)
    public ResponseDto<String> logOut() {
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.System.success"), null);
    }


    @RequestMapping(value = "/ws/addRealContact", method = RequestMethod.POST)
    public ResponseDto<RealPersonDto> addContact(@RequestBody RealPersonDto realPersonDto) throws RealPersonException {
        logger.info("addContact web service is running!");
        facade.addContact(realPersonDto);
        logger.info("addContact web service Successfully ended !");
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.System.success"), null);
    }

    @RequestMapping(value = "/ws/addLegalContact", method = RequestMethod.POST)
    public ResponseDto<LegalPersonDto> addLegalContact(@RequestBody LegalPersonDto legalPersonDto) throws LegalPersonException {
        logger.info("addLegalContact web service Successfully ended !");
        facade.addLegalContact(legalPersonDto);
        logger.info("addLegalContact web service Successfully ended !");
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.System.success"), null);
    }

    @RequestMapping(value = "/ws/saveRealPerson", method = RequestMethod.POST)
    public ResponseDto<RealPersonDto> save(@RequestBody RealPersonDto realPersonDto) throws RealPersonException, AccountException {

        logger.info("saveRealPerson web service is starting !");
        facade.save(realPersonDto);
        logger.info("saveRealPerson web service Successfully ended !");
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.System.success"), null);
    }

    @RequestMapping(value = "/ws/saveLegalPerson", method = RequestMethod.POST)
    public ResponseDto<LegalPersonDto> saveLegal(@RequestBody LegalPersonDto legalPersonDto) throws
            LegalPersonException, AccountException {
        logger.info("SaveLegalPerson Web Service Is Starting !");
        facade.saveLegal(legalPersonDto);
        logger.info("SaveLegalPerson Web Service Has Successfully Ended !");
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.System.success"), null);
    }


    @RequestMapping(value = "/ws/addAccount", method = RequestMethod.POST)
    public ResponseDto<PersonEntity> addAccount(@RequestBody UiAccountDto uiAccountDto) throws AccountException {
        logger.info("addAccount web service is starting !");
        facade.addAccount(uiAccountDto);
        logger.info("addAccount web service Successfully ended !");
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.System.success"), null);


    }


    @RequestMapping(value = "/ws/deleteLegalPerson", method = RequestMethod.POST)
    public ResponseDto<LegalPersonDto> deleteLegal(@RequestBody LegalPersonDto legalPersonDto) throws Exception {
        logger.info("DeleteLegalPerson Web Service Is Starting !");
        facade.deleteLegal(legalPersonDto);
        logger.info("DeleteLegalPerson Web Service Is Successfully Ended");
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.System.success"), null);
    }

    @Transactional(rollbackOn = Exception.class)
    @RequestMapping(value = "/ws/deleteRealPerson", method = RequestMethod.POST)
    public ResponseDto<RealPersonDto> delete(@RequestBody RealPersonDto realPersonDto) throws Exception {
        logger.info("DeleteRealPerson Web Service Is Starting");
        facade.delete(realPersonDto);
        logger.info("DeleteRealPerson Web Service Is Successfully Ended !");
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.System.success"), null);

    }

    @RequestMapping(value = "/ws/uniqueRealSearch", method = RequestMethod.POST)
    public ResponseDto<RealPersonDto> uniqueRealSearch(@RequestParam String nationalCode) throws Exception {
        logger.info("UniqueRealSearch Web Service Is Starting !");
        RealPersonDto realPersonDto = facade.uniqueRealSearch(nationalCode);
        logger.info("UniqueRealSearch Web Service Is Successfully Ended !");
        return new ResponseDto(ResponseStatus.Ok, realPersonDto, environment.getProperty("customerService.ok.System.success"), null);

    }

    @RequestMapping(value = "/ws/uniqueLegalSearch", method = RequestMethod.POST)
    public ResponseDto<LegalPersonDto> uniqueLegalSearch(@RequestParam String registrationCode) throws Exception {
        logger.info("UniqueLegalSearch Web Service Is Starting !");
        LegalPersonDto legalPersonDto = facade.uniqueLegalSearch(registrationCode);
        logger.info("UniqueLegalSearch Web Service Is Successfully Ended !");
        return new ResponseDto(ResponseStatus.Ok, legalPersonDto, environment.getProperty("customerService.ok.System.success"), null);
    }

    @RequestMapping(value = "/ws/realPersonSearch", method = RequestMethod.POST)
    public ResponseDto<List<RealPersonDto>> realSearch(@RequestBody SearchDto searchDto) throws Exception {
        logger.info("realPersonSearch web service is starting !");
        List<RealPersonDto> realPersonDtos = facade.realSearch(searchDto);
        logger.info("realPersonSearch web service is Successfully ended !");
        return new ResponseDto(ResponseStatus.Ok, realPersonDtos, environment.getProperty("customerService.ok.System.success"), null);
    }

    @RequestMapping(value = "/ws/legalPersonSearch", method = RequestMethod.POST)
    public ResponseDto<List<LegalPersonDto>> legalSearch(@RequestBody SearchDto searchDto) throws Exception {
        logger.info("realPersonSearch web service is starting !");
        List<LegalPersonDto> legalPersonDtos = facade.legalSearch(searchDto);
        logger.info("realPersonSearch web service is Successfully ended !");
        return new ResponseDto(ResponseStatus.Ok, legalPersonDtos, environment.getProperty("customerService.ok.System.success"), null);
    }

    @RequestMapping(value = "/ws/activiti/startProcess", method = RequestMethod.POST)
    public ResponseDto startProcess(@RequestBody BankFacilitiesDto bankFacilitiesDto) throws TransactionException {
        if (TransactionValidationUtility.validateFacilityDto(bankFacilitiesDto, accountDao)) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("bankFacilityAccNum", bankFacilitiesDto.getAccountNumber());
            variables.put("bankFacilityAccAmount", bankFacilitiesDto.getAmount());
            variables.put("bankFacilityType", bankFacilitiesDto.getFacilityType());
            variables.put("bankFacilityId", bankFacilitiesDto.getTaskId());
            runtimeService.startProcessInstanceByKey("Facility", variables);
            logger.info("Activiti Process Started successfully !");
            return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.activiti.start"), null);
        } else {
            logger.error("Activiti Process exited with error  !");
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(environment.getProperty("customerService.error.activiti")));
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
            return new ResponseDto(ResponseStatus.Ok, url, environment.getProperty("customerService.ok.System.redirect"), null);
        } else {
            logger.error("Error getting Url By FormKey : null formKey");
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(environment.getProperty("customerService.error.activiti.nullFormKey")));
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
        return new ResponseDto(ResponseStatus.Ok, taskDtos, environment.getProperty("customerService.ok.System.success"), null);
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
            return new ResponseDto(ResponseStatus.Ok, bankFacilitiesDto, environment.getProperty("customerService.ok.System.success"), null);
        } else {
            logger.error("Error Getting Task By TaskId : null TaskId");
            return new ResponseDto(ResponseStatus.Error, null, null, new ResponseException(environment.getProperty("customerService.error.activiti.nullTaskId")));
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
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.System.success"), null);
    }

    @RequestMapping(value = "/ws/activiti/rejection", method = RequestMethod.POST)
    public ResponseDto rejection(@RequestBody BankFacilitiesDto bankFacilitiesDto) throws TransactionException {
        facade.rejection(bankFacilitiesDto);
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.System.success"), null);

    }

    @RequestMapping(value = "/ws/activiti/payment", method = RequestMethod.POST)
    public ResponseDto payment(@RequestBody BankFacilitiesDto bankFacilitiesDto) throws TransactionException {
        facade.payment(bankFacilitiesDto);
        logger.info("Depositing Facility Demand Done Successfully!");
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.transaction.deposit"), null);
    }

    private String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    @RequestMapping(value = "/ws/transfer", method = RequestMethod.POST)
    @Transactional(rollbackOn = Exception.class)
    public ResponseDto<TransactionEntity> transfer(@RequestBody UiTransactionDto uiTransactionDto) throws TransactionException {
        logger.info("Transfer Transaction web Service Started!");
        facade.transfer(uiTransactionDto);
        logger.info("Transfer Transaction web Service Ended Successfully!");
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.transaction.transfer"), null);

    }


    @RequestMapping(value = "/ws/deposit", method = RequestMethod.POST)
    @Transactional(rollbackOn = Exception.class)
    public ResponseDto deposit(@RequestBody UiTransactionDto uiTransactionDto) throws TransactionException {
        logger.info("Transfer Transaction web Service Started!");
        facade.deposit(uiTransactionDto);
        logger.info("Deposit Transaction web Service Ended Successfully!");
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.transaction.deposit"), null);
    }

    @RequestMapping(value = "/ws/search", method = RequestMethod.POST)
    public ResponseDto<AccountEntity> search(@RequestParam String accountNumber) throws Exception {
        logger.info("transaction search web service is starting !");
        AccountEntity accountEntity = facade.search(accountNumber);
        logger.info("transaction search web service has Successfully ended !");
        return new ResponseDto(ResponseStatus.Ok, accountEntity, environment.getProperty("customerService.ok.System.success"), null);

    }

    @RequestMapping(value = "/ws/removal", method = RequestMethod.POST)
    @Transactional(rollbackOn = Exception.class)
    public ResponseDto<TransactionEntity> removal(@RequestBody UiTransactionDto uiTransactionDto) throws TransactionException {
        logger.info("Removal Transaction Started!");
        facade.removal(uiTransactionDto);
        logger.info("Removal Transaction Ended Successfully!");
        return new ResponseDto(ResponseStatus.Ok, null, environment.getProperty("customerService.ok.transaction.removal"), null);

    }

}
