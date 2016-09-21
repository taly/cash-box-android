package com.example.trabinerson.cashbox.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.trabinerson.cashbox.SendMoneyConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by asaf on 1/4/16.
 */
public class FundingOptionsData implements Parcelable {
    public SendMoneyConstants.RecipientType payeeType;
    public String payee;
    public boolean isPayeePayPal;
    public SendMoneyConstants.PaymentType paymentType;
    public List<Option> options;
    public String rtrAction;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(payeeType == null ? -1 : payeeType.ordinal());
        dest.writeString(payee);
        dest.writeInt(isPayeePayPal ? 1 : 0);
        dest.writeInt(paymentType == null ? -1 : paymentType.ordinal());
        dest.writeTypedList(options);
        dest.writeString(rtrAction);
    }

    public static final Creator<FundingOptionsData> CREATOR = new Creator<FundingOptionsData>() {
        @Override
        public FundingOptionsData createFromParcel(Parcel source) {
            FundingOptionsData result = new FundingOptionsData();
            int payeeTypeIdx = source.readInt();
            if (payeeTypeIdx >= 0) {
                result.payeeType = SendMoneyConstants.RecipientType.values()[payeeTypeIdx];
            }
            result.payee = source.readString();
            result.isPayeePayPal = source.readInt() == 1;
            int paymentTypeIdx = source.readInt();
            if (paymentTypeIdx >= 0) {
                result.paymentType = SendMoneyConstants.PaymentType.values()[paymentTypeIdx];
            }
            result.options = new ArrayList<>();
            source.readTypedList(result.options, Option.CREATOR);

            result.rtrAction = source.readString();

            return result;
        }

        @Override
        public FundingOptionsData[] newArray(int size) {
            return new FundingOptionsData[size];
        }
    };

    public static class Option implements Parcelable {
        public String id;
        public SendMoneyConstants.FundingMode fundingMode;
        public Fee fee;
        public List<Source> sources;
        public List<Source> backup;
        public Date estimatedFundsArrival;
        public CurrencyConversion currencyConversion;
        public Overdraft overdraft;
        public TravelRuleRequirements travelRuleRequirements;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeInt(fundingMode == null ? -1 : fundingMode.ordinal());
            dest.writeParcelable(fee, 0);
            dest.writeTypedList(sources);
            dest.writeTypedList(backup);
            dest.writeInt(estimatedFundsArrival == null ? 0 : 1);
            if (estimatedFundsArrival != null) {
                dest.writeLong(estimatedFundsArrival.getTime());
            }
            dest.writeParcelable(currencyConversion, 0);
            dest.writeParcelable(overdraft, 0);
            dest.writeParcelable(travelRuleRequirements, 0);
        }

        public static final Creator<Option> CREATOR = new Creator<Option>() {
            @Override
            public Option createFromParcel(Parcel source) {
                Option result = new Option();
                result.id = source.readString();
                int fundingModeIdx = source.readInt();
                if (fundingModeIdx >= 0) {
                    result.fundingMode = SendMoneyConstants.FundingMode.values()[fundingModeIdx];
                }
                result.fee = source.readParcelable(Fee.class.getClassLoader());
                result.sources = new ArrayList<>();
                source.readTypedList(result.sources, Source.CREATOR);
                result.backup = new ArrayList<>();
                source.readTypedList(result.backup, Source.CREATOR);
                if (source.readInt() == 1) {
                    result.estimatedFundsArrival = new Date(source.readLong());
                }
                result.currencyConversion = source.readParcelable(CurrencyConversion.class.getClassLoader());
                result.overdraft = source.readParcelable(Overdraft.class.getClassLoader());
                result.travelRuleRequirements = source.readParcelable(TravelRuleRequirements.class.getClassLoader());

                return result;
            }

            @Override
            public Option[] newArray(int size) {
                return new Option[size];
            }
        };

        public static class Source implements Parcelable {
            public SendMoneyConstants.InstrumentType instrumentType;
            public String id;
            public String amount;
            public String currencyCode;
            public PaymentCard paymentCard;
            public BankAccount bankAccount;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(instrumentType == null ? -1 : instrumentType.ordinal());
                dest.writeString(id);
                dest.writeString(amount);
                dest.writeString(currencyCode);
                dest.writeParcelable(paymentCard, 0);
                dest.writeParcelable(bankAccount, 0);
            }

            public static final Creator<Source> CREATOR = new Creator<Source>() {
                @Override
                public Source createFromParcel(Parcel source) {
                    Source result = new Source();
                    int instrumentTypeIdx = source.readInt();
                    if (instrumentTypeIdx >= 0) {
                        result.instrumentType = SendMoneyConstants.InstrumentType.values()[instrumentTypeIdx];
                    }
                    result.id = source.readString();
                    result.amount = source.readString();
                    result.currencyCode = source.readString();
                    result.paymentCard = source.readParcelable(PaymentCard.class.getClassLoader());
                    result.bankAccount = source.readParcelable(BankAccount.class.getClassLoader());
                    return result;
                }

                @Override
                public Source[] newArray(int size) {
                    return new Source[size];
                }
            };

            public static class PaymentCard implements Parcelable {
                public SendMoneyConstants.PaymentCardType type;
                public String network;
                public String last4;
                public String issuer;

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(type == null ? -1 : type.ordinal());
                    dest.writeString(network);
                    dest.writeString(last4);
                    dest.writeString(issuer);
                }

                public static final Creator<PaymentCard> CREATOR = new Creator<PaymentCard>() {
                    @Override
                    public PaymentCard createFromParcel(Parcel source) {
                        PaymentCard result = new PaymentCard();
                        int typeIdx = source.readInt();
                        if (typeIdx >= 0) {
                            result.type = SendMoneyConstants.PaymentCardType.values()[typeIdx];
                        }
                        result.network = source.readString();
                        result.last4 = source.readString();
                        result.issuer = source.readString();
                        return result;
                    }

                    @Override
                    public PaymentCard[] newArray(int size) {
                        return new PaymentCard[size];
                    }
                };
            }

            public static class BankAccount implements Parcelable {
                public SendMoneyConstants.BankAccountType type;
                public SendMoneyConstants.BankAccountSubType subType;
                public String last4;
                public String issuer;
                public String issuerCountryCode;

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(type == null ? -1 : type.ordinal());
                    dest.writeInt(subType == null ? -1 : subType.ordinal());
                    dest.writeString(last4);
                    dest.writeString(issuer);
                    dest.writeString(issuerCountryCode);
                }

                public static final Creator<BankAccount> CREATOR = new Creator<BankAccount>() {
                    @Override
                    public BankAccount createFromParcel(Parcel source) {
                        BankAccount result = new BankAccount();
                        int typeIdx = source.readInt();
                        if (typeIdx >= 0) {
                            result.type = SendMoneyConstants.BankAccountType.values()[typeIdx];
                        }
                        int subTypeIdx = source.readInt();
                        if (subTypeIdx >= 0) {
                            result.subType = SendMoneyConstants.BankAccountSubType.values()[subTypeIdx];
                        }
                        result.last4 = source.readString();
                        result.issuer = source.readString();
                        result.issuerCountryCode = source.readString();
                        return result;
                    }

                    @Override
                    public BankAccount[] newArray(int size) {
                        return new BankAccount[size];
                    }
                };
            }
        }

        public static class CurrencyConversion implements Parcelable {
            public String inAmount;
            public String inCurrencyCode;
            public String outAmount;
            public String outCurrencyCode;
            public String exchangeRate;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(inAmount);
                dest.writeString(inCurrencyCode);
                dest.writeString(outAmount);
                dest.writeString(outCurrencyCode);
                dest.writeString(exchangeRate);
            }

            public static final Creator<CurrencyConversion> CREATOR = new Creator<CurrencyConversion>() {
                @Override
                public CurrencyConversion createFromParcel(Parcel source) {
                    CurrencyConversion result = new CurrencyConversion();
                    result.inAmount = source.readString();
                    result.inCurrencyCode = source.readString();
                    result.outAmount = source.readString();
                    result.outCurrencyCode = source.readString();
                    result.exchangeRate = source.readString();
                    return result;
                }

                @Override
                public CurrencyConversion[] newArray(int size) {
                    return new CurrencyConversion[size];
                }
            };
        }

        public static class Fee implements Parcelable {
            public SendMoneyConstants.FeePayer feePayer;
            public String amount;
            public String currencyCode;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(feePayer == null ? -1 : feePayer.ordinal());
                dest.writeString(amount);
                dest.writeString(currencyCode);
            }

            public static final Creator<Fee> CREATOR = new Creator<Fee>() {
                @Override
                public Fee createFromParcel(Parcel source) {
                    Fee result = new Fee();
                    int payerIdx = source.readInt();
                    if (payerIdx >= 0) {
                        result.feePayer = SendMoneyConstants.FeePayer.values()[payerIdx];
                    }
                    result.amount = source.readString();
                    result.currencyCode = source.readString();
                    return result;
                }

                @Override
                public Fee[] newArray(int size) {
                    return new Fee[size];
                }
            };
        }

        public static class Overdraft implements Parcelable {
            public String amount;
            public String currencyCode;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(amount);
                dest.writeString(currencyCode);
            }

            public static final Creator<Overdraft> CREATOR = new Creator<Overdraft>() {
                @Override
                public Overdraft createFromParcel(Parcel source) {
                    Overdraft result = new Overdraft();
                    result.amount = source.readString();
                    result.currencyCode = source.readString();
                    return result;
                }

                @Override
                public Overdraft[] newArray(int size) {
                    return new Overdraft[size];
                }
            };
        }

        public static class TravelRuleRequirements implements Parcelable {
            public boolean govtId;
            public boolean dateOfBirth;
            public boolean nonPoBoxAddress;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeBooleanArray(new boolean[] { govtId, dateOfBirth, nonPoBoxAddress });
            }

            public static final Creator<TravelRuleRequirements> CREATOR = new Creator<TravelRuleRequirements>() {
                @Override
                public TravelRuleRequirements createFromParcel(Parcel source) {
                    TravelRuleRequirements result = new TravelRuleRequirements();
                    boolean flags[] = new boolean[3];
                    source.readBooleanArray(flags);
                    result.govtId = flags[0];
                    result.dateOfBirth = flags[1];
                    result.nonPoBoxAddress = flags[2];
                    return result;
                }

                @Override
                public TravelRuleRequirements[] newArray(int size) {
                    return new TravelRuleRequirements[size];
                }
            };
        }
    }
}