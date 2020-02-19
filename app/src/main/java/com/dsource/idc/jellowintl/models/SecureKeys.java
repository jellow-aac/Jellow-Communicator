package com.dsource.idc.jellowintl.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ekalpa on 3/17/2018.
 */

public class SecureKeys {
    @SerializedName("key")
    private String mKey;

    @SerializedName("salt")
    private String mSalt;

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getSalt() {
        return mSalt;
    }

    public void setSalt(String mSalt) {
        this.mSalt = mSalt;
    }
}
