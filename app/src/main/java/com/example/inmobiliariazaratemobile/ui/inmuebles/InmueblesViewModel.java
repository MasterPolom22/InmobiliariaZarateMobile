package com.example.inmobiliariazaratemobile.ui.inmuebles;


import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {
    private final MutableLiveData<String> mText = new MutableLiveData<>();
    private final MutableLiveData<List<InmuebleModel>> mInmueble = new MutableLiveData<>();


    public InmueblesViewModel(@NonNull Application application) {
        super(application);
        leerInmuebles();
    }

    public LiveData<String> getmText() {
        return mText;
    }

    public LiveData<List<InmuebleModel>> getmInmueble() {
        return mInmueble;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void leerInmuebles(){
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmoService api = ApiClient.getInmoService();
        Call<List<InmuebleModel>> llamada = api.getInmuebles("Bearer "+token);
        llamada.enqueue(new Callback<List<InmuebleModel>>() {
            @Override
            public void onResponse(Call<List<InmuebleModel>> call, Response<List<InmuebleModel>> response) {
                if (response.isSuccessful()){
                    mInmueble.postValue(response.body());
                } else {
                    Toast.makeText(getApplication(), "No hay inmuebles disponibles: "+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<InmuebleModel>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en servidor: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }









}