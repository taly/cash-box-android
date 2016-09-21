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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * Shows user final review of transaction.
 *
 * @author trabinerson
 */
public class ReviewActivity extends Activity {

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

        startSpinner();
    }

    private void startSpinner() {
        mTitle.setText("Processing your transaction");
        mDescription.setVisibility(View.GONE);
        mSpinner.setVisibility(View.VISIBLE);
        mOkButton.setVisibility(View.GONE);
        Animation spin = AnimationUtils.loadAnimation(this, R.anim.spin);
        mSpinner.startAnimation(spin);
    }

    private void stopSpinner() {
        mTitle.setText("Review your transaction");
        mDescription.setVisibility(View.VISIBLE);
        mSpinner.setVisibility(View.GONE);
        mOkButton.setVisibility(View.VISIBLE);
    }

}
