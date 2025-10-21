// InmuebleDetalleViewModel.java
package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.app.Application;
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

    // Cargar el inmueble recibido por argumento (Bundle/Navigation)
    public void init(InmuebleModel i){
        _inmueble.setValue(i);
    }

    // Solo cambia el flag en memoria (la Vista dispara este método)
    public void setDisponible(boolean disponible){
        InmuebleModel cur = _inmueble.getValue();
        if(cur == null) return;
        InmuebleModel copy = new InmuebleModel();
        copy.setIdInmueble(cur.getIdInmueble());
        copy.setDireccion(cur.getDireccion());
        copy.setUso(cur.getUso());
        copy.setTipo(cur.getTipo());
        copy.setAmbientes(cur.getAmbientes());
        copy.setSuperficie(cur.getSuperficie());
        copy.setLatitud(cur.getLatitud());
        copy.setLongitud(cur.getLongitud());
        copy.setValor(cur.getValor());
        copy.setImagen(cur.getImagen());
        copy.setIdPropietario(cur.getIdPropietario());
        copy.setDuenio(cur.getDuenio());
        copy.setDisponible(disponible);
        _inmueble.setValue(copy);
    }

    // PUT completo (sirve para solo habilitar/deshabilitar o actualizar todo)
    public void guardarCambios(){
        InmuebleModel cur = _inmueble.getValue();
        if(cur == null) return;

        _status.postValue(Status.SAVING);
        String bearer = ApiClient.bearer(getApplication());
        ApiClient.getInmoService().actualizarInmueble(bearer, cur)
                .enqueue(new Callback<InmuebleModel>() {
                    @Override public void onResponse(Call<InmuebleModel> call, Response<InmuebleModel> resp) {
                        if(resp.isSuccessful() && resp.body()!=null){
                            _inmueble.postValue(resp.body());
                            _status.postValue(Status.OK);
                        }else{
                            _status.postValue(Status.ERROR);
                        }
                    }
                    @Override public void onFailure(Call<InmuebleModel> call, Throwable t) {
                        _status.postValue(Status.ERROR);
                    }
                });
    }
}
