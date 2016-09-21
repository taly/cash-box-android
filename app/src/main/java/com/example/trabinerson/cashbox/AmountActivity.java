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
import android.content.Context;
import android.content.Intent;
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

    private static final String STATE_AMOUNT = "state_amount";
    private static final int MAX_AMOUNT = 1000;

    private int mAmount;
    private TextView mAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount);

        if (savedInstanceState != null) {
            mAmount = savedInstanceState.getInt(STATE_AMOUNT);
        } else {
            mAmount = 0;
        }

        mAmountTextView = (TextView) findViewById(R.id.text_amount);
        setupNumPad();
        requestNewAmount(mAmount);

        View nextButton = findViewById(R.id.next_button);
        final Context context = this;
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAmount > 0) {
                    Intent intent = new Intent(context, ReviewActivity.class);
                    Bundle extras = new Bundle();
                    extras.putParcelable(MainActivity.EXTRA_MERCHANT, getIntent().getExtras().getParcelable(MainActivity.EXTRA_MERCHANT));
                    extras.putInt(MainActivity.EXTRA_AMOUNT, mAmount);
                    extras.putParcelable(MainActivity.EXTRA_USER_DATA, getIntent().getExtras().getParcelable(MainActivity.EXTRA_USER_DATA));
                    extras.putParcelable(MainActivity.EXTRA_MERCHANT, getIntent().getExtras().getParcelable(MainActivity.EXTRA_MERCHANT));
                    intent.putExtras(extras);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Amount must be positive", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_AMOUNT, mAmount);
    }

    private void setupNumPad() {
        int[] ids = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.double_zeros};
        for (int id : ids) {
            View view = findViewById(id);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestedAmount = mAmount;
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
                int requestedAmount = (int) Math.floor(mAmount / 10);
                requestNewAmount(requestedAmount);
            }
        });
    }

    private boolean requestNewAmount(int newAmount) {
        if (newAmount > MAX_AMOUNT) {
            Toast.makeText(this, "Max amount: $" + MAX_AMOUNT, Toast.LENGTH_SHORT).show();
            return false;
        }
        mAmount = newAmount;
        mAmountTextView.setText("$" + mAmount);
        return true;
    }

}
