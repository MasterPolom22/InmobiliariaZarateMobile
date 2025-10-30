package com.example.inmobiliariazaratemobile.ui.inquilinos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.example.inmobiliariazaratemobile.model.*;
import com.example.inmobiliariazaratemobile.request.ApiClient;
import retrofit2.*;

public class InquilinoDetalleViewModel extends AndroidViewModel {
    public enum Status { IDLE, LOADING, OK, ERROR }

    private final MutableLiveData<InquilinoModel> _inquilino = new MutableLiveData<>();
    public final LiveData<InquilinoModel> inquilino = _inquilino;

    private final MutableLiveData<Status> _status = new MutableLiveData<>(Status.IDLE);
    public final LiveData<Status> status = _status;

    public InquilinoDetalleViewModel(@NonNull Application app){ super(app); }

    public void cargarPorInmueble(int inmuebleId){
        String bearer = ApiClient.bearer(getApplication());
        if (bearer == null) { _status.postValue(Status.ERROR); return; }
        _status.postValue(Status.LOADING);

        ApiClient.getInmoService().contratoVigente(bearer, inmuebleId)
                .enqueue(new Callback<ContratoModel>() {
                    @Override public void onResponse(Call<ContratoModel> c, Response<ContratoModel> r) {
                        if (r.isSuccessful() && r.body()!=null && r.body().getInquilino()!=null) {
                            _inquilino.postValue(r.body().getInquilino());
                            _status.postValue(Status.OK);
                        } else {
                            _status.postValue(Status.ERROR);
                        }
                    }
                    @Override public void onFailure(Call<ContratoModel> c, Throwable t) {
                        _status.postValue(Status.ERROR);
                    }
                });
    }
}