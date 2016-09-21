package com.example.trabinerson.cashbox;

/**
 * Created by asaf on 1/4/16.
 */
public class SendMoneyConstants {

    public enum RecipientType {
        EMAIL,
        PHONE,
        ACCOUNT_NUMBER
    }

    public enum PaymentType {
        PERSONAL,
        PURCHASE
    }

    public enum FeePayer {
        PAYER,
        PAYEE
    }

    public enum FundingMode {
        INSTANT,
        DELAYED,
        MANUAL
    }

    public enum InstrumentType {
        HOLDING,
        PAYMENT_CARD,
        BANK_ACCOUNT,
        CREDIT
    }

    public enum PaymentCardType {
        CREDIT,
        DEBIT
    }

    public enum BankAccountType {
        CHECKING,
        SAVINGS,
        NORMAL,
        BUSINESS_CHECKING,
        BUSINESS_SAVINGS,
        UNKNOWN
    }

    public enum BankAccountSubType {
        ECHECK,
        INSTANT_ACH,
        UNCONFIRMED_ACH,
        MANUAL_EFT,
        INSTANT_EFT,
        ELV,
        UNCONFIRMED_ELV
    }

    public enum IDType {
        PASSPORT,
        SSN,
        ITIN,
        EIN,
        CNF,
        CPNJ,
        SIN,
        NA
    }

    public enum DateOfBirthConfirmationAuthority {
        UNKNOWN,
        USER,
        EXPERIAN,
        ADMIN,
        BUYER_CREDIT,
        TRANSACTIONAL_BUYER_CREDIT,
        AUTHFLOW,
        ZOOT
    }

    public enum DateOfBirthConfirmationStatus {
        UNKNOWN,
        VALID
    }
}