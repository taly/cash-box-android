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

import com.google.gson.annotations.SerializedName;

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
        mGeometry = in.readParcelable(geometry.class.getClassLoader());


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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mVicinity);
        parcel.writeFloat(mRating);
        parcel.writeParcelable(mGeometry,i);

    }


    static class geometry implements Parcelable{

        @SerializedName("location")
        private location mLocation;

        protected geometry(Parcel in) {
            mLocation = in.readParcelable(location.class.getClassLoader());
        }

        public static final Creator<geometry> CREATOR = new Creator<geometry>() {
            @Override
            public geometry createFromParcel(Parcel in) {
                return new geometry(in);
            }

            @Override
            public geometry[] newArray(int size) {
                return new geometry[size];
            }
        };

        public location getLocation() {
            return mLocation;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(mLocation, i);
        }
    }

    static class location implements Parcelable{
        public double lat;
        public double lng;

        protected location(Parcel in) {
            lat = in.readDouble();
            lng = in.readDouble();
        }

        public static final Creator<location> CREATOR = new Creator<location>() {
            @Override
            public location createFromParcel(Parcel in) {
                return new location(in);
            }

            @Override
            public location[] newArray(int size) {
                return new location[size];
            }
        };

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeDouble(lat);
            parcel.writeDouble(lng);
        }
    }
}

