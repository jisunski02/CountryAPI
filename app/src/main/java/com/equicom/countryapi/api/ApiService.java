package com.equicom.countryapi.api;

import com.equicom.countryapi.data.CountryDataModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiService {

    @GET("v3.1/all")
    Single<List<CountryDataModel>> getCountryList();

}
