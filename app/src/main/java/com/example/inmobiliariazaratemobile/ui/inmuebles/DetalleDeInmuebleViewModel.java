package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;

public class DetalleDeInmuebleViewModel extends AndroidViewModel {
    private final MutableLiveData<InmuebleModel> mInmueble = new MutableLiveData<>();


    public DetalleDeInmuebleViewModel(@NonNull Application application) {
        super(application);
    }







}