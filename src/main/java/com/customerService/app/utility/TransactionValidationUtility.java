package com.customerService.app.utility;


import com.customerService.app.dto.BankFacilitiesDto;
import com.customerService.app.dto.TransactionDto;
import com.customerService.app.model.dao.AccountDao;


import java.math.BigDecimal;
import java.util.Objects;

public class TransactionValidationUtility {
    private static boolean state;
    private static String errorMassage;


    public static boolean validateTransfer(TransactionDto transactionDto, AccountDao accountDao) throws TransactionException {
        errorMassage = "";
        state = true;
        if (Objects.isNull(transactionDto)) {
            errorMassage += "Transaction Doesn't Exist-";
            state = false;
        } else {
            if (Objects.isNull(transactionDto.getAccountNumber())) {
                errorMassage += "SourceAccountNumber Field Is Null-";
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(transactionDto.getAccountNumber()))) {
                errorMassage += "Account With Such SourceAccountNumber Doesn't Exist-";
                state = false;
            }
            amountValidation(transactionDto.getAmount());
            if (Objects.isNull(transactionDto.getDestinationAccountNumber())) {
                errorMassage += "DestinationAccountNumber Field Is Null-";
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(transactionDto.getDestinationAccountNumber()))) {
                errorMassage += "Account With Such DestinationAccountNumber Doesn't Exist-";
                state = false;
            } else if (accountDao.findByAccountNumber(transactionDto.getAccountNumber()).getAccountAmount().compareTo( transactionDto.getAmount())==-1) {
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

    public static boolean validateDeposit(TransactionDto transactionDto, AccountDao accountDao) throws TransactionException {
        errorMassage = "";
        state = true;
        if (Objects.isNull(transactionDto)) {
            errorMassage += "Transaction Doesn't Exist-";
            state = false;
        } else {
            amountValidation(transactionDto.getAmount());
            if (Objects.isNull(transactionDto.getAccountNumber())) {
                errorMassage += "DestinationAccountNumber Field Is Null-";
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(transactionDto.getAccountNumber()))) {
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
    public static boolean validateFacility(BankFacilitiesDto bankFacilitiesDto, AccountDao accountDao) throws TransactionException {
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


    public static boolean validateRemoval(TransactionDto transactionDto, AccountDao accountDao) throws TransactionException {
        errorMassage = "";
        state = true;
        if (Objects.isNull(transactionDto)) {
            errorMassage += "Transaction Doesn't Exist-";
            state = false;
        } else {
            amountValidation(transactionDto.getAmount());
            if (Objects.isNull(transactionDto.getAccountNumber())) {
                errorMassage += "SourceAccountNumber Field Is Null-";
                state = false;
            } else if (Objects.isNull(accountDao.findByAccountNumber(transactionDto.getAccountNumber()))) {
                errorMassage += "Account With Such AccountNumber Doesn't Exist-";
                state = false;
            } else if (accountDao.findByAccountNumber(transactionDto.getAccountNumber()).getAccountAmount().compareTo(transactionDto.getAmount())==-1) {
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
