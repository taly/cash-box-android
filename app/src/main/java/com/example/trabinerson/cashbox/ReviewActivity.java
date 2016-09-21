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

package com.example.trabinerson.cashbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.trabinerson.cashbox.models.FundingOptionsData;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Shows user final review of transaction.
 *
 * @author trabinerson
 */
public class ReviewActivity extends Activity {

    public static final String EXTRA_MERCHANT = "extra_merchant";
    public static final String EXTRA_AMOUNT = "arg_amount_cents";

    private TextView mTitle;
    private TextView mDescription;
    private View mSpinner;
    private View mOkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mTitle = (TextView) findViewById(R.id.title);
        mDescription = (TextView) findViewById(R.id.description);
        mSpinner = findViewById(R.id.spinner);
        mOkButton = findViewById(R.id.ok_button);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this,QRActivity.class);
                startActivity(intent);

            }
        });

        startSpinner();

        final int amount = getIntent().getIntExtra(EXTRA_AMOUNT, 0);
        // TODO get FI from server instead of this crap
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSpinner();
                setFundingOptions(amount, getDummyData(amount));
            }
        }, 3000);
    }

    private void startSpinner() {
        mTitle.setText("Processing your transaction");
        mDescription.setVisibility(View.GONE);
        mSpinner.setVisibility(View.VISIBLE);
        mOkButton.setVisibility(View.INVISIBLE);
        Animation spin = AnimationUtils.loadAnimation(this, R.anim.spin);
        mSpinner.startAnimation(spin);
    }

    private void stopSpinner() {
        mTitle.setText("Review your transaction");
        mDescription.setVisibility(View.VISIBLE);
        mSpinner.clearAnimation();
        mSpinner.setVisibility(View.GONE);
        mOkButton.setVisibility(View.VISIBLE);
    }

    private void setFundingOptions(int withdrawalAmount, FundingOptionsData data) {
        FundingOptionsData.Option fundingOption = data.options.get(0);
        String description = "You're withdrawing $" + withdrawalAmount + ".\n\n";
        description += "Your PayPal account will be charged $" + withdrawalAmount + ", plus a fee of $" + fundingOption.fee.amount + ".\n\n";
        if (fundingOption.sources.size() == 1) {
            FundingOptionsData.Option.Source source = fundingOption.sources.get(0);
            description += "All of the funds will be taken from your ";
            description += sourceToString(source) + ".";
        } else {
            for (FundingOptionsData.Option.Source source : fundingOption.sources) {
                description += "$" + source.amount + " will be taken from your " + sourceToString(source) + "\n\n";
            }
        }
        mDescription.setText(description);
    }

    private String sourceToString(FundingOptionsData.Option.Source source) {
        switch (source.instrumentType) {
            case BANK_ACCOUNT:
                return "bank account";
            case HOLDING:
                return "PayPal balance";
            case CREDIT:
                return "PayPal credit.";
            case PAYMENT_CARD:
                return source.paymentCard.issuer + " (" + source.paymentCard.last4 + ").";
        }
        return null;
    }

    private FundingOptionsData getDummyData(int amount) {
        FundingOptionsData.Option.Source source1 = new FundingOptionsData.Option.Source();
        source1.instrumentType = SendMoneyConstants.InstrumentType.HOLDING;
        source1.amount = "12";
        source1.currencyCode = "USD";

        FundingOptionsData.Option.Source source2 = new FundingOptionsData.Option.Source();
        source2.instrumentType = SendMoneyConstants.InstrumentType.PAYMENT_CARD;
        source2.amount = "9.1";
        source2.currencyCode = "USD";
        source2.paymentCard = new FundingOptionsData.Option.Source.PaymentCard();
        source2.paymentCard.issuer = "Mastercard";
        source2.paymentCard.last4 = "7890";
        source2.paymentCard.type = SendMoneyConstants.PaymentCardType.CREDIT;

        FundingOptionsData.Option option = new FundingOptionsData.Option();
        option.id = "1234";
        option.sources = Arrays.asList(source1, source2);
        option.fee = new FundingOptionsData.Option.Fee();
        option.fee.amount = "1.1";
        option.fee.currencyCode = "USD";

        FundingOptionsData data = new FundingOptionsData();
        data.options = new ArrayList<>();
        data.options.add(option);

        return data;
    }

}
