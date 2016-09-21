package com.example.trabinerson.cashbox.parsers;

import android.util.Log;

import com.example.trabinerson.cashbox.SendMoneyConstants;
import com.example.trabinerson.cashbox.models.FundingOptionsData;
import com.example.trabinerson.cashbox.models.PaymentData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asaf on 2/4/16.
 */
public class SendMoneyParser {

    public static FundingOptionsData parseFundingOptions(String fundingOptionsResponseBody) {
        FundingOptionsData result = new FundingOptionsData();
        try {
            JSONObject base = new JSONObject(fundingOptionsResponseBody);

            JSONObject fundingOptions = base.getJSONObject("funding_options");
            result.paymentType = SendMoneyConstants.PaymentType.valueOf(fundingOptions.getString("payment_type"));

            JSONObject payee = fundingOptions.getJSONObject("payee");
            result.payee = payee.getString("id");
            result.payeeType = SendMoneyConstants.RecipientType.valueOf(payee.getString("type"));
            result.isPayeePayPal = payee.getBoolean("is_paypal_account");

            if (fundingOptions.has("regulatory_information")) {
                if (fundingOptions.getJSONObject("regulatory_information").has("remittance_transfer_rule")) {
                    result.rtrAction = fundingOptions.getJSONObject("regulatory_information").getJSONObject("remittance_transfer_rule").getString("action");
                }
            }

            result.options = new ArrayList<>();
            JSONArray options = fundingOptions.getJSONArray("options");
            for (int optionIdx = 0; optionIdx < options.length(); ++optionIdx) {
                JSONObject option = options.getJSONObject(optionIdx);
                FundingOptionsData.Option fundingOption = new FundingOptionsData.Option();
                fundingOption.id = option.getString("id");
                fundingOption.fundingMode = SendMoneyConstants.FundingMode.valueOf(option.getString("funding_mode"));

                if (option.has("fee") && option.getJSONObject("fee").has("amount")) {
                    JSONObject fee = option.getJSONObject("fee");
                    fundingOption.fee = new FundingOptionsData.Option.Fee();
                    fundingOption.fee.feePayer = SendMoneyConstants.FeePayer.valueOf(fee.getString("payer"));
                    fundingOption.fee.amount = fee.getJSONObject("amount").getString("value");
                    fundingOption.fee.currencyCode = fee.getJSONObject("amount").getString("currency");
                }

                fundingOption.sources = parseFundingSources(option.getJSONArray("sources"));

                if (option.has("backup_funding_sources")) {
                    fundingOption.backup = parseFundingSources(option.getJSONArray("backup_funding_sources"));
                }

                if (option.has("estimated_funds_arrival")) {
                    try {
                        fundingOption.estimatedFundsArrival = new SimpleDateFormat().parse(option.getString("estimated_funds_arrival"));
                    } catch (ParseException ex) {
                        Log.e("FundingOptionsData", "Can't parse estimated funds arrival: " + option.getString("estimated_funds_arrival"), ex);
                    }
                }

                if (option.has("currency_conversion")) {
                    JSONObject conversion = option.getJSONObject("currency_conversion");
                    fundingOption.currencyConversion = new FundingOptionsData.Option.CurrencyConversion();
                    fundingOption.currencyConversion.exchangeRate = conversion.getString("exchange_rate");
                    fundingOption.currencyConversion.inAmount = conversion.getJSONObject("funds_in").getString("value");
                    fundingOption.currencyConversion.inCurrencyCode = conversion.getJSONObject("funds_in").getString("currency");
                    fundingOption.currencyConversion.outAmount = conversion.getJSONObject("funds_out").getString("value");
                    fundingOption.currencyConversion.outCurrencyCode = conversion.getJSONObject("funds_out").getString("currency");
                }

                if (option.has("overdraft_amount")) {
                    fundingOption.overdraft = new FundingOptionsData.Option.Overdraft();
                    fundingOption.overdraft.amount = option.getJSONObject("overdraft_amount").getString("value");
                    fundingOption.overdraft.currencyCode = option.getJSONObject("overdraft_amount").getString("currency");
                }

                if (option.has("contingencies")) {
                    if (option.getJSONArray("contingencies").length() > 1) {
                        throw new RuntimeException("Unexpected contingencies: " + option.getJSONArray("contingencies").toString());
                    }
                    JSONObject tr = option.getJSONArray("contingencies").getJSONObject(0);
                    if (tr.getString("action").equalsIgnoreCase("PERFORM_DATA_COLLECTION")) {
                        fundingOption.travelRuleRequirements = new FundingOptionsData.Option.TravelRuleRequirements();
                        JSONArray attributes = tr.getJSONObject("data_collection_details").getJSONArray("attributes");
                        for (int i = 0; i < attributes.length(); ++i) {
                            if (attributes.getString(i).equalsIgnoreCase("GOVT_ID")) {
                                fundingOption.travelRuleRequirements.govtId = true;
                            }
                            if (attributes.getString(i).equalsIgnoreCase("DATE_OF_BIRTH")) {
                                fundingOption.travelRuleRequirements.dateOfBirth = true;
                            }
                            if (attributes.getString(i).equalsIgnoreCase("NON_PO_BOX_ADDRESS")) {
                                fundingOption.travelRuleRequirements.nonPoBoxAddress = true;
                            }
                        }
                    } else {
                        throw new RuntimeException("Unknown contingency: " + tr.getString("action"));
                    }
                }

                result.options.add(fundingOption);
            }

        } catch (JSONException ex) {
            throw new RuntimeException(fundingOptionsResponseBody, ex);
        }

        return result;
    }

    public static PaymentData parsePayment(String paymentResponseBody) {
        PaymentData result = new PaymentData();
        try {
            JSONObject base = new JSONObject(paymentResponseBody);
            JSONObject pp = base.getJSONObject("personal_payment");
            result.paymentId = pp.getString("payment_id");
            result.createTime = pp.getString("create_time");
            result.paymentType = SendMoneyConstants.PaymentType.valueOf(pp.getString("payment_type"));
            result.note = pp.optString("note_to_payee");
            result.estimatedFundsArrival = pp.optString("estimated_funds_arrival");
            JSONObject payee = pp.getJSONObject("payee");
            result.recipient = payee.getString("id");
            result.recipientName = payee.optString("name");
            result.recipientType = SendMoneyConstants.RecipientType.valueOf(payee.getString("type"));
            result.recipientCountryCode = payee.optString("country_code");
            result.isPayPalAccount = payee.getBoolean("is_paypal_account");
            JSONObject amount = pp.getJSONObject("amount");
            result.amount = amount.getString("value");
            result.currencyCode = amount.getString("currency");
            JSONObject fee = pp.optJSONObject("fee");
            if (fee != null) {
                JSONObject feeAmount = fee.optJSONObject("amount");
                if (feeAmount != null && feeAmount.has("value")) {
                    result.fee = new PaymentData.Fee();
                    result.fee.payer = SendMoneyConstants.FeePayer.valueOf(fee.getString("payer"));
                    result.fee.amount = feeAmount.getString("value");
                    result.fee.currencyCode = feeAmount.getString("currency");
                }
            }
        } catch (JSONException ex) {
            throw new RuntimeException(paymentResponseBody, ex);
        }

        return result;
    }

    private static List<FundingOptionsData.Option.Source> parseFundingSources(JSONArray sources) throws JSONException {
        List<FundingOptionsData.Option.Source> fundingSources = new ArrayList<>();
        for (int sourceIdx = 0; sourceIdx < sources.length(); ++sourceIdx) {
            JSONObject source = sources.getJSONObject(sourceIdx);
            FundingOptionsData.Option.Source fundingSource = new FundingOptionsData.Option.Source();
            fundingSource.instrumentType = SendMoneyConstants.InstrumentType.valueOf(source.getString("instrument_type"));
            switch (fundingSource.instrumentType) {
                case HOLDING: {
                    JSONObject instrument = source.getJSONObject("holding");
                    fundingSource.id = instrument.getString("id");
                    fundingSource.amount = instrument.getJSONObject("amount").getString("value");
                    fundingSource.currencyCode = instrument.getJSONObject("amount").getString("currency");
                } break;
                case PAYMENT_CARD: {
                    JSONObject instrument = source.getJSONObject("payment_card");
                    fundingSource.id = instrument.getString("id");
                    fundingSource.amount = instrument.getJSONObject("amount").getString("value");
                    fundingSource.currencyCode = instrument.getJSONObject("amount").getString("currency");
                    fundingSource.paymentCard = new FundingOptionsData.Option.Source.PaymentCard();
                    fundingSource.paymentCard.type = SendMoneyConstants.PaymentCardType.valueOf(instrument.getString("type"));
                    fundingSource.paymentCard.network = instrument.getString("network");
                    fundingSource.paymentCard.last4 = instrument.getString("last_4");
                    fundingSource.paymentCard.issuer = instrument.optString("issuer");
                } break;
                case BANK_ACCOUNT: {
                    JSONObject instrument = source.getJSONObject("bank_account");
                    fundingSource.id = instrument.getString("id");
                    fundingSource.amount = instrument.getJSONObject("amount").getString("value");
                    fundingSource.currencyCode = instrument.getJSONObject("amount").getString("currency");
                    fundingSource.bankAccount = new FundingOptionsData.Option.Source.BankAccount();
                    fundingSource.bankAccount.type = SendMoneyConstants.BankAccountType.valueOf(instrument.getString("type"));
                    fundingSource.bankAccount.subType = SendMoneyConstants.BankAccountSubType.valueOf(instrument.getString("subtype"));
                    fundingSource.bankAccount.last4 = instrument.getString("last_4");
                    fundingSource.bankAccount.issuer = instrument.getJSONObject("issuer").getString("name");
                    fundingSource.bankAccount.issuerCountryCode = instrument.getJSONObject("issuer").getString("country_code");
                } break;
                case CREDIT: {
                    JSONObject instrument = source.getJSONObject("credit");
                    fundingSource.id = instrument.getString("id");
                    fundingSource.amount = instrument.getJSONObject("amount").getString("value");
                    fundingSource.currencyCode = instrument.getJSONObject("amount").getString("currency");
                } break;
            }

            fundingSources.add(fundingSource);
        }
        return fundingSources;
    }
}