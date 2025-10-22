// reemplaza TODO el ViewModel por esto
package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;

import retrofit2.Call;
import retrofit2.Response;

public class InmuebleDetalleViewModel extends AndroidViewModel {

    public enum Status { IDLE, SAVING, OK, ERROR }

    private final MutableLiveData<InmuebleModel> _inmueble = new MutableLiveData<>();
    public final LiveData<InmuebleModel> inmueble = _inmueble;

    private final MutableLiveData<Status> _status = new MutableLiveData<>(Status.IDLE);
    public final LiveData<Status> status = _status;

    public InmuebleDetalleViewModel(@NonNull Application app){ super(app); }

    public void init(InmuebleModel i){ if(i!=null) _inmueble.setValue(i); }

    // módulo: SOLO disponibilidad
    public void guardarDisponibilidad(boolean disponible){
        InmuebleModel cur = _inmueble.getValue();
        if(cur == null) return;

        InmuebleModel body = new InmuebleModel();
        body.setIdInmueble(cur.getIdInmueble());
        body.setDireccion(cur.getDireccion());
        body.setUso(cur.getUso());
        body.setTipo(cur.getTipo());
        body.setAmbientes(cur.getAmbientes());
        body.setSuperficie(cur.getSuperficie());
        body.setLatitud(cur.getLatitud());
        body.setLongitud(cur.getLongitud());
        body.setValor(cur.getValor());
        body.setImagen(cur.getImagen());
        body.setIdPropietario(cur.getIdPropietario());
        body.setDuenio(cur.getDuenio());
        body.setDisponible(disponible);

        _status.postValue(Status.SAVING);
        String bearer = ApiClient.bearer(getApplication());
        ApiClient.getInmoService().actualizarDisponible(bearer, body)
                .enqueue(new retrofit2.Callback<InmuebleModel>() {
                    @Override public void onResponse(Call<InmuebleModel> c, Response<InmuebleModel> r) {
                        if(r.isSuccessful() && r.body()!=null){
                            _inmueble.postValue(r.body());
                            _status.postValue(Status.OK);
                        }else{
                            _status.postValue(Status.ERROR);
                        }
                    }
                    @Override public void onFailure(Call<InmuebleModel> c, Throwable t) {
                        _status.postValue(Status.ERROR);
                    }
                });
    }
}
