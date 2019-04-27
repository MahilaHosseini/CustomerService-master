package com.customerService.app.utility;


import com.customerService.app.dto.BankFacilitiesDto;
import com.customerService.app.dto.UiTransactionDto;
import com.customerService.app.model.dao.AccountDao;
import com.customerService.app.model.entity.FacilityEntity;


import java.math.BigDecimal;
import java.util.Objects;

public class TransactionValidationUtility {
    private static boolean state;
    private static String errorMassage;


    public static boolean validateTransfer(UiTransactionDto uiTransactionDto, AccountDao accountDao) throws TransactionException {
        errorMassage = "";
        state = true;
        if (Objects.isNull(uiTransactionDto)) {
            errorMassage += "Transaction Doesn't Exist-";
            state = false;
        } else {
            if (Objects.isNull(uiTransactionDto.getAccountNumber())) {
                errorMassage += "SourceAccountNumber Field Is Null-";
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber()))) {
                errorMassage += "Account With Such SourceAccountNumber Doesn't Exist-";
                state = false;
            }
            amountValidation(uiTransactionDto.getAmount());
            if (Objects.isNull(uiTransactionDto.getDestinationAccountNumber())) {
                errorMassage += "DestinationAccountNumber Field Is Null-";
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(uiTransactionDto.getDestinationAccountNumber()))) {
                errorMassage += "Account With Such DestinationAccountNumber Doesn't Exist-";
                state = false;
            } else if (accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber()).getAccountAmount().compareTo( uiTransactionDto.getAmount())==-1) {
                errorMassage += "SourceAccount Balance Is Not Enough-";
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
            errorMassage += "Transaction Doesn't Exist-";
            state = false;
        } else {
            amountValidation(uiTransactionDto.getAmount());
            if (Objects.isNull(uiTransactionDto.getAccountNumber())) {
                errorMassage += "DestinationAccountNumber Field Is Null-";
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber()))) {
                errorMassage += "Account With Such AccountNumber Doesn't Exist-";
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
                errorMassage += "AccountNumber Field Is Null-";
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(bankFacilitiesDto.getAccountNumber()))) {
                errorMassage += "Account With Such AccountNumber Doesn't Exist-";
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
            errorMassage += "Transaction Doesn't Exist-";
            state = false;
        } else {
            amountValidation(uiTransactionDto.getAmount());
            if (Objects.isNull(uiTransactionDto.getAccountNumber())) {
                errorMassage += "SourceAccountNumber Field Is Null-";
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber()))) {
                errorMassage += "Account With Such AccountNumber Doesn't Exist-";
                state = false;
            } else if (accountDao.findByAccountNumber(uiTransactionDto.getAccountNumber()).getAccountAmount().compareTo(uiTransactionDto.getAmount())==-1) {
                errorMassage += "SourceAccount Balance Is Not Enough-";
                state = false;
            }
        }
        if (state)
            return true;
        else {
            throw new TransactionException(errorMassage);
        }
    }

    public static void amountValidation(BigDecimal amount){
        if (Objects.isNull(amount)) {
            errorMassage += "Amount Field Is Null-";
            state = false;
        } else if (amount.compareTo(BigDecimal.ZERO)==-1) {
            errorMassage += "Amount Field Is Negative-";
            state = false;
        }
    }

}
