package com.equicom.countryapi.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryDataModel {

    @SerializedName("name")
    private NameDataModel nameDataModel;
    @SerializedName("region")
    private String region;
    @SerializedName("capital")
    private List<String> capital;


    public CountryDataModel(NameDataModel nameDataModel, String region, List<String> capital) {
        this.nameDataModel = nameDataModel;
        this.region = region;
        this.capital = capital;
    }

    public NameDataModel getNameDataModel() {
        return nameDataModel;
    }

    public String getRegion() {
        return region;
    }

    public List<String> getCapital() {
        return capital;
    }
}
