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
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Selector for withdrawal amount.
 *
 * @author trabinerson
 */
public class AmountActivity extends Activity {

    private static final String STATE_AMOUNT_CENTS = "state_amount_cents";
    private static final int MAX_AMOUNT_CENTS = 100000; // $1,000

    private int mAmountCents;
    private TextView mAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount);

        if (savedInstanceState != null) {
            mAmountCents = savedInstanceState.getInt(STATE_AMOUNT_CENTS);
        } else {
            mAmountCents = 0;
        }

        mAmountTextView = (TextView) findViewById(R.id.text_amount);
        setupNumPad();
        requestNewAmount(mAmountCents);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_AMOUNT_CENTS, mAmountCents);
    }

    private void setupNumPad() {
        int[] ids = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.double_zeros};
        for (int id : ids) {
            View view = findViewById(id);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestedAmount = mAmountCents;
                    requestedAmount *= (v.getId() == R.id.double_zeros) ? 100 : 10;
                    requestedAmount += Integer.parseInt(((TextView) v).getText().toString());
                    requestNewAmount(requestedAmount);
                }
            });
        }

        View view = findViewById(R.id.backspace);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestedAmount = (int) Math.floor(mAmountCents / 10);
                requestNewAmount(requestedAmount);
            }
        });
    }

    private boolean requestNewAmount(int newAmountCents) {
        if (newAmountCents > MAX_AMOUNT_CENTS) {
            Toast.makeText(this, "Max amount: $" + MAX_AMOUNT_CENTS/100, Toast.LENGTH_SHORT).show();
            return false;
        }
        mAmountCents = newAmountCents;
        mAmountTextView.setText("$" + String.format("%.2f", mAmountCents / 100f));
        return true;
    }

}
