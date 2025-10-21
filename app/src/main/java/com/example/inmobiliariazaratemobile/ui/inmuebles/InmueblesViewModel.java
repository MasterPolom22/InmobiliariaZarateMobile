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

    public void leerInmuebles() {
        String raw = ApiClient.leerToken(getApplication());
        if (raw == null || raw.trim().isEmpty()) {
            Toast.makeText(getApplication(), "Token no encontrado. Iniciá sesión.", Toast.LENGTH_SHORT).show();
            mInmueble.postValue(java.util.Collections.emptyList());
            return;
        }
        String auth = raw.startsWith("Bearer ") ? raw : "Bearer " + raw;

        ApiClient.InmoService api = ApiClient.getInmoService();
        api.getInmuebles(auth).enqueue(new Callback<List<InmuebleModel>>() {
            @Override
            public void onResponse(Call<List<InmuebleModel>> call, Response<List<InmuebleModel>> resp) {
                if (resp.isSuccessful()) {
                    List<InmuebleModel> data = resp.body() != null ? resp.body() : java.util.Collections.emptyList();
                    mInmueble.postValue(data);
                    if (data.isEmpty()) {
                        Toast.makeText(getApplication(), "No hay inmuebles del propietario.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String msg = "HTTP " + resp.code() + " " + resp.message();
                    try {
                        msg += " | " + (resp.errorBody() != null ? resp.errorBody().string() : "");
                    } catch (Exception ignored) {
                    }
                    Toast.makeText(getApplication(), "Error al listar inmuebles: " + msg, Toast.LENGTH_LONG).show();
                    mInmueble.postValue(java.util.Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<List<InmuebleModel>> call, Throwable t) {
                Toast.makeText(getApplication(), "Fallo red/servidor: " + t.getMessage(), Toast.LENGTH_LONG).show();
                mInmueble.postValue(java.util.Collections.emptyList());
            }
        });
    }

    public void cargarInmuebles() {
    }

    public void onInmuebleClick(InmuebleModel inmuebleModel) {
    }
}








