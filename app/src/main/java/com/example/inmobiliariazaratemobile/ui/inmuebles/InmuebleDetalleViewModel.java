
package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleDetalleViewModel extends AndroidViewModel {


    public enum Status { IDLE, SAVING, OK, ERROR }

    private final MutableLiveData<InmuebleModel> _inmueble = new MutableLiveData<>();
    public LiveData<InmuebleModel> inmueble = _inmueble;

    private final MutableLiveData<Status> _status = new MutableLiveData<>(Status.IDLE);
    public LiveData<Status> status = _status;

    public InmuebleDetalleViewModel(@NonNull Application app){ super(app); }

    public void init(InmuebleModel i){ _inmueble.setValue(i); }

    public void toggleDisponible(boolean disponible){
        InmuebleModel cur = _inmueble.getValue();
        if(cur == null) return;

        _status.postValue(Status.SAVING);

        var dto = new com.example.inmobiliariazaratemobile.request.dto.InmuebleDisponibleDto(
                cur.getIdInmueble(), disponible);

        ApiClient.getInmoService()
                .actualizarDisponibilidad(ApiClient.bearer(getApplication()), dto)
                .enqueue(new retrofit2.Callback<InmuebleModel>() {
                    @Override public void onResponse(Call<InmuebleModel> c, Response<InmuebleModel> r) {
                        if(r.isSuccessful() && r.body()!=null){
                            _inmueble.postValue(r.body());
                            _status.postValue(Status.OK);
                        } else {
                            // revertir UI local si el server rechazó
                            _inmueble.postValue(cur);
                            _status.postValue(Status.ERROR);
                        }
                    }
                    @Override public void onFailure(Call<InmuebleModel> c, Throwable t) {
                        _inmueble.postValue(cur);
                        _status.postValue(Status.ERROR);
                    }
                });
    }




    public void obtenerInmueble(Bundle args){
        InmuebleModel i = (InmuebleModel) args.getSerializable("inmueble");
        if(i!=null){ _inmueble.setValue(i); _status.setValue(Status.IDLE); }
    }

    public void toggleEditar(){
        Status s = _status.getValue();
        _status.setValue(s==Status.EDIT ? Status.IDLE : Status.EDIT);
    }

    // Aplica los cambios que llegan desde la vista (wiring)
    public void aplicarEdicion(String direccion, String tipo, String uso,
                               Integer ambientes, Double lat, Double lng, Double valor,
                               boolean disponible){
        InmuebleModel cur = _inmueble.getValue(); if(cur==null) return;
        InmuebleModel m = new InmuebleModel();
        m.setIdInmueble(cur.getIdInmueble());
        m.setIdPropietario(cur.getIdPropietario());
        m.setDuenio(cur.getDuenio());
        m.setImagen(cur.getImagen());

        m.setDireccion(direccion);
        m.setTipo(tipo);
        m.setUso(uso);
        m.setAmbientes(ambientes!=null? ambientes : cur.getAmbientes());
        m.setLatitud(lat!=null? lat : cur.getLatitud());
        m.setLongitud(lng!=null? lng : cur.getLongitud());
        m.setValor(valor!=null? valor : cur.getValor());
        m.setDisponible(disponible);

        _inmueble.setValue(m);
    }

    public void guardar(){
        InmuebleModel body = _inmueble.getValue(); if(body==null) return;
        _status.postValue(Status.SAVING);
        String bearer = ApiClient.bearer(getApplication());
        ApiClient.getInmoService().actualizarInmueble(bearer, body)
                .enqueue(new retrofit2.Callback<InmuebleModel>() {
                    @Override public void onResponse(retrofit2.Call<InmuebleModel> c, retrofit2.Response<InmuebleModel> r){
                        if(r.isSuccessful() && r.body()!=null){
                            _inmueble.postValue(r.body());
                            _status.postValue(Status.OK);
                            _status.postValue(Status.IDLE); // salir de edición
                        } else _status.postValue(Status.ERROR);
                    }
                    @Override public void onFailure(retrofit2.Call<InmuebleModel> c, Throwable t){
                        _status.postValue(Status.ERROR);
                    }
                });
    }

    public void actualizarDisponible(boolean disponible){
        InmuebleModel cur = _inmueble.getValue();
        if (cur == null) return;


        aplicarEdicion(
                cur.getDireccion(),
                cur.getTipo(),
                cur.getUso(),
                cur.getAmbientes(),
                cur.getLatitud(),
                cur.getLongitud(),
                cur.getValor(),
                disponible
        );
    }
}

