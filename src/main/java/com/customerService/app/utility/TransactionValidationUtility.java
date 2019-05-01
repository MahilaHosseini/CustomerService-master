package com.customerService.app.utility;


import com.customerService.app.dto.BankFacilitiesDto;
import com.customerService.app.dto.UiTransactionDto;
import com.customerService.app.model.dao.AccountDao;
import com.customerService.app.exception.TransactionException;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.Objects;

@Component
public class TransactionValidationUtility implements EnvironmentAware {
    private static boolean state;
    private static String errorMassage;

    private static Environment environment;

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    public static boolean validateTransfer(UiTransactionDto uiTransactionDto, AccountDao accountDao) throws TransactionException {
        errorMassage = "";
        state = true;
        if (Objects.isNull(uiTransactionDto)) {
            errorMassage += environment.getProperty("customerService.error.transaction.nullTransaction");
            state = false;
        } else {
            if (Objects.isNull(uiTransactionDto.getAccountNumber())) {
                errorMassage += environment.getProperty("customerService.error.transaction.nullSourceField");
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber()))) {
                errorMassage += environment.getProperty("customerService.error.transaction.noSourceAccount");
                state = false;
            }
            amountValidation(uiTransactionDto.getAmount());
            if (Objects.isNull(uiTransactionDto.getDestinationAccountNumber())) {
                errorMassage += environment.getProperty("customerService.error.transaction.nullDestinationField");
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(uiTransactionDto.getDestinationAccountNumber()))) {
                errorMassage += environment.getProperty("customerService.error.transaction.noDestinationAccount");
                state = false;
            } else if (accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber()).getAccountAmount().compareTo(uiTransactionDto.getAmount()) == -1) {
                errorMassage += environment.getProperty("customerService.error.transaction.balanceNotEnough");
                state = false;
            }
        }
        if (state)
            return true;
        else {
            throw new TransactionException(errorMassage);
        }
    }

    public static boolean validateDeposit(UiTransactionDto uiTransactionDto, AccountDao accountDao) throws TransactionException {
        errorMassage = "";
        state = true;
        if (Objects.isNull(uiTransactionDto)) {
            errorMassage += environment.getProperty("customerService.error.transaction.nullTransaction");
            state = false;
        } else {
            amountValidation(uiTransactionDto.getAmount());
            if (Objects.isNull(uiTransactionDto.getAccountNumber())) {
                errorMassage += environment.getProperty("customerService.error.transaction.nullDestinationField");
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber()))) {
                errorMassage += environment.getProperty("customerService.error.transaction.noDestinationAccount");
                state = false;
            }
        }

        if (state)
            return true;
        else {
            throw new TransactionException(errorMassage);
        }
    }

    public static boolean validateFacilityDto(BankFacilitiesDto bankFacilitiesDto, AccountDao accountDao) throws TransactionException {
        errorMassage = "";
        state = true;
        if (Objects.isNull(bankFacilitiesDto)) {
            errorMassage += "Facility Request Details Does Not Exist";
            state = false;
        } else {
            if (Objects.isNull(bankFacilitiesDto.getAccountNumber())) {
                errorMassage += environment.getProperty("customerService.error.transaction.nullSourceField");
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(bankFacilitiesDto.getAccountNumber()))) {
                errorMassage += environment.getProperty("customerService.error.transaction.noSourceAccount");
                state = false;
            }
        }
        if (state)
            return true;
        else {
            throw new TransactionException(errorMassage);
        }
    }

    public static boolean validateRemoval(UiTransactionDto uiTransactionDto, AccountDao accountDao) throws TransactionException {
        errorMassage = "";
        state = true;
        if (Objects.isNull(uiTransactionDto)) {
            errorMassage += environment.getProperty("customerService.error.transaction.nullTransaction");
            state = false;
        } else {
            amountValidation(uiTransactionDto.getAmount());
            if (Objects.isNull(uiTransactionDto.getAccountNumber())) {
                errorMassage += environment.getProperty("customerService.error.transaction.nullSourceField");
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber()))) {
                errorMassage += environment.getProperty("customerService.error.transaction.noSourceAccount");
                state = false;
            } else if (accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber()).getAccountAmount().compareTo(uiTransactionDto.getAmount()) == -1) {
                errorMassage += environment.getProperty("customerService.error.transaction.balanceNotEnough");
                state = false;
            }
        }
        if (state)
            return true;
        else {
            throw new TransactionException(errorMassage);
        }
    }

    public static void amountValidation(BigDecimal amount) {
        if (Objects.isNull(amount)) {
            errorMassage += environment.getProperty("customerService.error.transaction.nullAmount");
            state = false;
        } else if (amount.compareTo(BigDecimal.ZERO) == -1) {
            errorMassage += environment.getProperty("customerService.error.transaction.negativeAmount");
            state = false;
        }
    }

}
