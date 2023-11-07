package com.equicom.countryapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.equicom.countryapi.api.ApiService;
import com.equicom.countryapi.api.RetrofitClient;
import com.equicom.countryapi.data.CountryDataModel;
import com.equicom.countryapi.data.NameDataModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();
    List<String> capitalList = new ArrayList<>();
    List<CountryDataModel> countryList = new ArrayList<>();
    List<String> regionList = new ArrayList<>();

    AppCompatButton btnClear, btnSubmit;
    EditText etName, etSearch;
    Spinner spinnerRegion;
    LinearLayout linearSpinner;
    TextView tvError;
    Spinner sItems;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnClear = findViewById(R.id.btnClear);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvError = findViewById(R.id.tvError);
        etName = findViewById(R.id.etName);
        etSearch = findViewById(R.id.etSearch);
        spinnerRegion = findViewById(R.id.spinnerRegion);
        linearSpinner = findViewById(R.id.linearSpinner);

        apiService = RetrofitClient.getClient(Constants.BASE_URL).create(ApiService.class);

        getRegions();


        btnSubmit.setOnClickListener(v->{
            if(!isValidName()){
                return;
            }

            if(isValidName()){

                if (Constants.selectedItem.equals("Please select your region")) {
                    linearSpinner.setBackgroundResource(R.drawable.edittext_bg_error);
                }

                else {
                    linearSpinner.setBackgroundResource(R.drawable.edittext_bg);
                }
            }

        });

        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String region  = adapter.getItem(position);

                if (position == -1) {
                    // Do Nothing
                } else {



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getRegions(){
        regionList.clear();
        regionList.add("Please select your region");
        disposable.add(
                apiService.getCountryList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<CountryDataModel>>() {
                            @Override
                            public void onSuccess(List<CountryDataModel> response) {

                                for(int a = 0; a<response.size(); a++){
                                    CountryDataModel countryDataModel = response.get(a);

//                                    for(int b = a; b<countryDataModel.getCapital().size(); b++){
//                                        String capital = countryDataModel.getCapital().get(b);
//                                        capitalList.add(capital);
//                                    }

                                    String country = countryDataModel.getNameDataModel().getCommon();


                                    String region = countryDataModel.getRegion();
                                    NameDataModel nameDataModel = new NameDataModel(country);
                                    CountryDataModel countryDataModel1 = new CountryDataModel(nameDataModel, region, capitalList);

                                    countryList.add(countryDataModel);

                                    boolean isExist = isExistRegion(region);
                                    if(!isExist){
                                        regionList.add(region);
                                    }

                                    adapter = new ArrayAdapter<String>(
                                            MainActivity.this, android.R.layout.simple_spinner_item, regionList);

                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    sItems = findViewById(R.id.spinnerRegion);
                                    sItems.setAdapter(adapter);
                                    Constants.selectedItem = sItems.getSelectedItem().toString();

                                }
                            }
                            @Override
                            public void onError(Throwable e) {

                                Log.e("Failed", "Failed to fetch countries");
                            }
                        }));
    }

    private boolean isExistRegion(String strRegion) {

        for (int i = 0; i < regionList.size(); i++) {
            String region = regionList.get(i);
            if (region.equals(strRegion)) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidName(){
        String name = etName.getText().toString().trim();
        final String namePattern = "[a-zA-Z. ]+";

        if(name.isEmpty()){
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("Name can't be empty");
        }
        else if (!name.matches(namePattern)) {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("Cannot contain alphanumeric characters");
            return false;
        }
        else{
            tvError.setVisibility(View.GONE);
            etName.setError(null);
            return true;
        }

        return true;
    }
}