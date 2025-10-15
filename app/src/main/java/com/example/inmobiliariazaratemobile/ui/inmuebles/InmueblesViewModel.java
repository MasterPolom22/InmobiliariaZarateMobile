package com.example.inmobiliariazaratemobile.ui.inmuebles;
import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {


    public InmueblesViewModel(@NonNull Application application) {
        super(application);
    }
}