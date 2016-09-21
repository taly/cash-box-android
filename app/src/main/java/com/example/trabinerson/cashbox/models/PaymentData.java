/*
 * Copyright (c) 2016 PayPal, Inc.
 *
 * All rights reserved.
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

package com.example.trabinerson.cashbox.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.trabinerson.cashbox.SendMoneyConstants;


/**
 * Created by asaf on 1/14/16.
 */
public class PaymentData implements Parcelable {
    public String paymentId;
    public String createTime;
    public SendMoneyConstants.PaymentType paymentType;
    public String note;
    public String estimatedFundsArrival;
    public String recipient;
    public SendMoneyConstants.RecipientType recipientType;
    public String recipientName;
    public String recipientCountryCode;
    public boolean isPayPalAccount;
    public String amount;
    public String currencyCode;
    public Fee fee;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paymentId);
        dest.writeString(createTime);
        dest.writeInt(paymentType == null ? -1 : paymentType.ordinal());
        dest.writeString(note);
        dest.writeString(estimatedFundsArrival);
        dest.writeString(recipient);
        dest.writeInt(recipientType == null ? -1 : recipientType.ordinal());
        dest.writeString(recipientName);
        dest.writeString(recipientCountryCode);
        dest.writeInt(isPayPalAccount ? 1 : 0);
        dest.writeString(amount);
        dest.writeString(currencyCode);
        dest.writeParcelable(fee, 0);
    }

    public static final Creator<PaymentData> CREATOR = new Creator<PaymentData>() {
        @Override
        public PaymentData createFromParcel(Parcel source) {
            PaymentData result = new PaymentData();
            result.paymentId = source.readString();
            result.createTime = source.readString();
            int paymentTypeIdx = source.readInt();
            if (paymentTypeIdx >= 0) {
                result.paymentType = SendMoneyConstants.PaymentType.values()[paymentTypeIdx];
            }
            result.note = source.readString();
            result.estimatedFundsArrival = source.readString();
            result.recipient = source.readString();
            int recipientTypeIdx = source.readInt();
            if (recipientTypeIdx >= 0) {
                result.recipientType = SendMoneyConstants.RecipientType.values()[recipientTypeIdx];
            }
            result.recipientName = source.readString();
            result.recipientCountryCode = source.readString();
            result.isPayPalAccount = source.readInt() == 1;
            result.amount = source.readString();
            result.currencyCode = source.readString();
            result.fee = source.readParcelable(Fee.class.getClassLoader());
            return result;
        }

        @Override
        public PaymentData[] newArray(int size) {
            return new PaymentData[size];
        }
    };

    public static class Fee implements Parcelable {
        public SendMoneyConstants.FeePayer payer;
        public String amount;
        public String currencyCode;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(payer == null ? -1 : 1);
            dest.writeString(amount);
            dest.writeString(currencyCode);
        }

        public static final Creator<Fee> CREATOR = new Creator<Fee>() {
            @Override
            public Fee createFromParcel(Parcel source) {
                Fee result = new Fee();
                int payerIdx = source.readInt();
                if (payerIdx >= 0) {
                    result.payer = SendMoneyConstants.FeePayer.values()[payerIdx];
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
}