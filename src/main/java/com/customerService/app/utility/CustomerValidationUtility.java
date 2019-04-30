package com.customerService.app.utility;

import com.customerService.app.model.entity.AccountEntity;
import com.customerService.app.model.entity.CallNumberEntity;
import com.customerService.app.model.entity.LegalPersonEntity;
import com.customerService.app.model.entity.RealPersonEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class CustomerValidationUtility implements EnvironmentAware {

    private static boolean validationState = true;
    private static Logger logger = LoggerFactory.getLogger(CustomerValidationUtility.class);
    private static String errorMessage;


    private static Environment environment;
    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }


    public static boolean legalPersonValidation(LegalPersonEntity legalPersonEntity) throws LegalPersonException {
        validationState = true;
        if (Objects.isNull(legalPersonEntity)) {
            errorMessage = environment.getProperty("customerService.error.person.nullobj");
            logger.error("object is null");

            validationState = false;
        } else {
            errorMessage = "";
            if (Objects.isNull(legalPersonEntity.getRegistrationCode())) {
                errorMessage += environment.getProperty("customerService.error.person.nullRegistrationCode");
                logger.error("RegistrationCode is null");
                validationState = false;
            } else if (!Pattern.matches("[0-9]{10}", legalPersonEntity.getRegistrationCode())) {
                errorMessage += environment.getProperty("customerService.error.person.registrationCodeValid");
                logger.error("RegistrationCode is not valid");
                validationState = false;
            }

            try {
                validationState = nameValidation(legalPersonEntity.getName());
            } catch (Exception e) {
                logger.error("error on legal person name field: " + e);

            }
            try {
                validationState = addressValidation(legalPersonEntity.getAddress());
            } catch (Exception e) {
                logger.error("error on legal person address field: " + e);
            }
            try {
                validationState = eMailValidation(legalPersonEntity.geteMailAddress());
            } catch (Exception e) {
                logger.error("error on legal person  eMailAddress field: " + e);
            }
            try {
                validationState = numberValidation(legalPersonEntity.getNumbers());
            } catch (Exception e) {
                logger.error("error on legal person numbers field: " + e);
            }

        }
        if (validationState)
            return true;
        else {
            throw new LegalPersonException(errorMessage);
        }
    }

    public static boolean realPersonValidation(RealPersonEntity realPersonEntity) throws RealPersonException {
        validationState = true;
        if (Objects.isNull(realPersonEntity)) {
            errorMessage = environment.getProperty("customerService.error.person.nullobj");
            logger.error("object is null");
            validationState = false;
        } else {
            errorMessage = "";
            if (Objects.isNull(realPersonEntity.getNationalCode())) {
                errorMessage += environment.getProperty("customerService.error.person.nullNationalCode");
                logger.error("NationalCode is null");
                validationState = false;
            } else if (!Pattern.matches("[0-9]{10}", realPersonEntity.getNationalCode())) {
                errorMessage += environment.getProperty("customerService.error.person.nationalCodeValid");
                logger.error("NationalCode is not valid");
                validationState = false;
            }

            if (!Objects.isNull(realPersonEntity.getLastName()) && !Pattern.matches("[a-z,A-Z]{3,20}", realPersonEntity.getLastName())) {
                errorMessage += environment.getProperty("customerService.error.person.lastNameValid");
                logger.error("Last Name is not valid");
                validationState = false;
            }
            try {
                validationState = nameValidation(realPersonEntity.getName());
            } catch (Exception e) {
                logger.error("error on real person name field: " + e);

            }
            try {
                validationState = addressValidation(realPersonEntity.getAddress());
            } catch (Exception e) {
                logger.error("error on real person address field: " + e);
            }
            try {
                validationState = eMailValidation(realPersonEntity.geteMailAddress());
            } catch (Exception e) {
                logger.error("error on real person  eMailAddress field: " + e);
            }
            try {
                validationState = numberValidation(realPersonEntity.getNumbers());
            } catch (Exception e) {
                logger.error("error on real person numbers field: " + e);
            }
        }

        if (validationState)
            return true;
        else {
            System.out.println(errorMessage);
            throw new RealPersonException(errorMessage);
        }
    }

    public static boolean eMailValidation(String mailAddress) {
        if (!Objects.isNull(mailAddress) && !Pattern.matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", mailAddress)) {
            errorMessage += environment.getProperty("customerService.error.person.emailAddressValid");
            logger.error("emailAddress is not valid");
            validationState = false;
        }
        return validationState;
    }

    public static boolean addressValidation(String address) {
        if (!Objects.isNull(address) && !Pattern.matches("[a-z,A-Z,0-9,\\,]{6,50}", address)) {
            errorMessage += environment.getProperty("customerService.error.person.addressValid");
            logger.error("address is not valid");
            validationState = false;
        }
        return validationState;
    }

    public static boolean nameValidation(String name) {
        if (Objects.isNull(name)) {
            logger.error("name is null");
            validationState = false;
            errorMessage += environment.getProperty("customerService.error.person.Name");
        } else if (!Pattern.matches("[a-z,A-Z]{3,20}", name)) {
            errorMessage += environment.getProperty("customerService.error.person.nameValid");
            logger.error("Name is not valid");
            validationState = false;
        }
        return validationState;
    }

    public static boolean numberValidation(List<CallNumberEntity> numbers) {
        if (!Objects.isNull(numbers)) {
            for (CallNumberEntity callNumberEntity : numbers) {
                if (!Pattern.matches("[0-9]{11}", callNumberEntity.getNumber())) {
                    errorMessage += (callNumberEntity.getType() + environment.getProperty("customerService.error.person.numberValid"));
                    logger.error(callNumberEntity.getType() + "Number is not valid");
                    validationState = false;
                }
            }
        }
        return validationState;
    }

    public static boolean accountValidation(List<AccountEntity> accountEntities) throws AccountException {
        if (!Objects.isNull(accountEntities)) {
            for (AccountEntity accountEntity : accountEntities) {
                if (accountEntity.getAccountAmount().compareTo(new BigDecimal(10000)) == -1) {
                    errorMessage += environment.getProperty("customerService.error.transaction.balanceValid");
                    logger.error("Balance is Not Valid");
                    validationState = false;
                } else {
                    accountEntities.forEach(sd -> {
                        sd.setMinimumOfTheDay(sd.getAccountAmount());
                        sd.setMinimumOfMonth(sd.getAccountAmount());
                    });
                }
            }
        }
        if (validationState)
            return true;
        else {
            throw new AccountException(errorMessage);
        }
    }

    public static boolean accountGenerationValidation(AccountEntity accountEntity) throws AccountException {
        if (!Objects.isNull(accountEntity)) {
            if (accountEntity.getAccountAmount().compareTo(new BigDecimal(10000)) == -1) {
                errorMessage += environment.getProperty("customerService.error.transaction.balanceValid");
                logger.error("Balance is Not Valid");
                validationState = false;
            } else {
                accountEntity.setMinimumOfTheDay(accountEntity.getAccountAmount());
                accountEntity.setMinimumOfMonth(accountEntity.getAccountAmount());
            }

        }
        if (validationState)
            return true;
        else {
            throw new AccountException(errorMessage);
        }
    }

}
