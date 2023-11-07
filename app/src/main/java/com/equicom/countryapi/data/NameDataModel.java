package com.equicom.countryapi.data;

import com.google.gson.annotations.SerializedName;

public class NameDataModel {

    @SerializedName("common")
    private String common;

    public NameDataModel(String common) {
        this.common = common;
    }

    public String getCommon() {
        return common;
    }
}
