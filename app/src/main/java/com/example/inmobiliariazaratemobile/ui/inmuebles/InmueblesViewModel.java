package com.example.inmobiliariazaratemobile.ui.inmuebles;


import android.app.Application;
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

    private final MutableLiveData<List<InmuebleModel>> _inmuebles = new MutableLiveData<>();
    public LiveData<List<InmuebleModel>> inmuebles = _inmuebles;

    public InmueblesViewModel(@NonNull Application app){ super(app); }

    public void cargar() {
        String bearer = ApiClient.bearer(getApplication()); // "Bearer <token>"
        ApiClient.getInmoService().listarInmuebles(bearer)
                .enqueue(new Callback<List<InmuebleModel>>() {
                    @Override public void onResponse(Call<List<InmuebleModel>> call, Response<List<InmuebleModel>> r) {
                        _inmuebles.postValue(r.isSuccessful() && r.body()!=null ? r.body() : java.util.Collections.emptyList());
                    }
                    @Override public void onFailure(Call<List<InmuebleModel>> call, Throwable t) {
                        _inmuebles.postValue(java.util.Collections.emptyList());
                    }
                });
    }}
