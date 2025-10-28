package com.example.inmobiliariazaratemobile.ui.inquilinos;

import static androidx.lifecycle.AndroidViewModel_androidKt.getApplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class InquilinosViewModel extends ViewModel {
    public enum Status { IDLE, LOADING, OK, ERROR }

    private final MutableLiveData<List<InmuebleModel>> _items = new MutableLiveData<>();
    public final LiveData<List<InmuebleModel>> items = _items;

    private final MutableLiveData<Status> _status = new MutableLiveData<>(Status.IDLE);
    public final LiveData<Status> status = _status;

    public InquilinosViewModel(@NonNull Application app){ super(app); }

    public void cargar() {
        String bearer = ApiClient.bearer(getApplication());
        if (bearer == null) { _status.postValue(Status.ERROR); return; }

        _status.postValue(Status.LOADING);
        ApiClient.getInmoService().listarConContratoVigente(bearer)
                .enqueue(new retrofit2.Callback<List<InmuebleModel>>() {
                    @Override public void onResponse(Call<List<InmuebleModel>> c, Response<List<InmuebleModel>> r) {
                        if (r.isSuccessful() && r.body()!=null) {
                            _items.postValue(r.body());
                            _status.postValue(Status.OK);
                        } else _status.postValue(Status.ERROR);
                    }
                    @Override public void onFailure(Call<List<InmuebleModel>> c, Throwable t) {
                        _status.postValue(Status.ERROR);
                    }
                });
    }

}