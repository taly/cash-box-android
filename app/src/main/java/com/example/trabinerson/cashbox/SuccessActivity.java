package com.example.trabinerson.cashbox;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SuccessActivity extends Activity {

    CheckmarkView cv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        cv = (CheckmarkView)findViewById(R.id.checkmarkView);
        TextView success_text_View = (TextView)findViewById(R.id.success_text_View);
        String text = getResources().getString(R.string.success_text, QRActivity.AMOUNT, QRActivity.MERCHANT);
        success_text_View.setText(text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cv.startAnimation();
    }
}
