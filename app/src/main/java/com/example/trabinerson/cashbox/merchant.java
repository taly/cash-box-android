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

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * TODO: Write Javadoc for merchant.
 *
 * @author slorber
 */
public class Merchant implements Parcelable {

    @SerializedName("name")
    private String mName;
    @SerializedName("vicinity")
    private String mVicinity;
    @SerializedName("geometry")
    private geometry mGeometry;
    @SerializedName("rating")
    private float mRating;


    protected Merchant(Parcel in) {
        mName = in.readString();
        mVicinity = in.readString();
        mRating = in.readFloat();

    }

    public static final Creator<Merchant> CREATOR = new Creator<Merchant>() {
        @Override
        public Merchant createFromParcel(Parcel in) {
            return new Merchant(in);
        }

        @Override
        public Merchant[] newArray(int size) {
            return new Merchant[size];
        }
    };


    public String getName() {
        return mName;
    }

    public String getVicinity() {
        return mVicinity;
    }
    public geometry getGeometry() {
        return mGeometry;
    }

    public float getRating() {
        return mRating;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mVicinity);
        dest.writeFloat(mRating);

    }
     class geometry{
         location location = new location();

    }
    class location {
        public double lat;
        public double lng;

        public double getLat() {
            return lat;
        }
        public double getLng() {
            return lng;
        }
    }
}

