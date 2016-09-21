package com.example.trabinerson.cashbox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;

public class QRActivity extends Activity {

    public static final String EXTRA_QR_TEXT = "QR_TEXT";
    private Merchant mMerchant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            mMerchant = b.getParcelable(EXTRA_QR_TEXT);
        Bitmap myBitmap = QRCode.from(mMerchant.getName()).bitmap();
        ImageView myImage = (ImageView) findViewById(R.id.qr_code);
        myImage.setImageBitmap(myBitmap);
        }
    }
    public void navigateAction(View view)
    {
        Uri gmmIntentUri = Uri.parse("geo:"+mMerchant.getGeometry().getLocation().getLat()+","+mMerchant.getGeometry().getLocation().getLng());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
