package com.example.trabinerson.cashbox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.trabinerson.cashbox.models.UserData;

import net.glxn.qrgen.android.QRCode;

public class QRActivity extends Activity {

    public static final String EXTRA_FUNDING_OPTION_ID = "extra_funding_option_id";
    public static int AMOUNT;
    public static String MERCHANT;

    private static final String DELIMITER = "\t";

    private Merchant mMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            mMerchant = b.getParcelable(MainActivity.EXTRA_MERCHANT);
            Bitmap myBitmap = QRCode.from(getQrString()).bitmap();
            ImageView myImage = (ImageView) findViewById(R.id.qr_code);
            myImage.setImageBitmap(myBitmap);
            MERCHANT = mMerchant.getName();
        }
    }

    public void navigateAction(View view) {
        Uri gmmIntentUri = Uri.parse("geo:"+mMerchant.getGeometry().getLocation().getLat()+","+mMerchant.getGeometry().getLocation().getLng());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private String getQrString() {
        Bundle extras = getIntent().getExtras();
        String fundingOptionId = extras.getString(EXTRA_FUNDING_OPTION_ID);
        UserData userData = extras.getParcelable(MainActivity.EXTRA_USER_DATA);
        int amount = extras.getInt(MainActivity.EXTRA_AMOUNT);
        AMOUNT = amount;
        String qrString = userData.email + DELIMITER + userData.fullName + DELIMITER + amount + DELIMITER + fundingOptionId;
        return qrString;
    }
}
