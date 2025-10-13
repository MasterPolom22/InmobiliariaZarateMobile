package com.example.inmobiliariazaratemobile.ui.perfil;

import android.app.Application;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliariazaratemobile.R;
import com.example.inmobiliariazaratemobile.model.PropietarioModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    public enum Status { IDLE, LOADING, SUCCESS, ERROR }
    public static class UiState {
        public final Status status; public final String message;
        UiState(Status s, String m){ status=s; message=m; }
        static UiState idle(){ return new UiState(Status.IDLE,null); }
        static UiState loading(String m){ return new UiState(Status.LOADING,m); }
        static UiState success(String m){ return new UiState(Status.SUCCESS,m); }
        static UiState error(String m){ return new UiState(Status.ERROR,m); }
    }

    private final MutableLiveData<PropietarioModel> _prop = new MutableLiveData<>();
    public LiveData<PropietarioModel> propietario = _prop;

    private final MutableLiveData<Boolean> _editMode = new MutableLiveData<>(false);
    public LiveData<Boolean> editMode = _editMode;

    private final MutableLiveData<UiState> _ui = new MutableLiveData<>(UiState.idle());
    public LiveData<UiState> ui = _ui;

    private final ApiClient.InmoService api = ApiClient.getInmoService();

    public PerfilViewModel(@NonNull Application app) { super(app); }

    private String token(){
        String t = ApiClient.leerToken(getApplication());
        return t != null ? t : "";
    }

    public void cargarPerfil(){
        _ui.postValue(UiState.loading("Cargando"));
        api.getPropietario(token()).enqueue(new Callback<PropietarioModel>() {
            @Override public void onResponse(Call<PropietarioModel> call, Response<PropietarioModel> res) {
                if (res.isSuccessful() && res.body()!=null){
                    _prop.postValue(res.body());
                    _ui.postValue(UiState.idle());
                } else {
                    _ui.postValue(UiState.error("No se pudo obtener el perfil"));
                }
            }
            @Override public void onFailure(Call<PropietarioModel> call, Throwable t) {
                _ui.postValue(UiState.error("Error de red al obtener perfil"));
            }
        });
    }

    public void toggleEditarGuardar(PropietarioModel editado){
        Boolean edit = _editMode.getValue()!=null && _editMode.getValue();
        if (!edit){
            _editMode.postValue(true); // pasa a edición
        } else {
            // Guardar
            actualizarPerfil(editado);
        }
    }

    public void actualizarPerfil(PropietarioModel p){
        if (p == null){
            _ui.postValue(UiState.error("Datos inválidos"));
            return;
        }
        if (TextUtils.isEmpty(p.getNombre()) || TextUtils.isEmpty(p.getApellido())
                || TextUtils.isEmpty(p.getEmail())){
            _ui.postValue(UiState.error("Complete nombre, apellido y email"));
            return;
        }
        _ui.postValue(UiState.loading("Guardando"));
        api.actualizarProp(token(), p).enqueue(new Callback<PropietarioModel>() {
            @Override public void onResponse(Call<PropietarioModel> call, Response<PropietarioModel> res) {
                if (res.isSuccessful() && res.body()!=null){
                    _prop.postValue(res.body());
                    _editMode.postValue(false);
                    _ui.postValue(UiState.success("Guardado"));
                } else {
                    _ui.postValue(UiState.error("No se pudo guardar"));
                }
            }
            @Override public void onFailure(Call<PropietarioModel> call, Throwable t) {
                _ui.postValue(UiState.error("Error de red al guardar"));
            }
        });
    }

    public void cambiarPassword(String actual, String nueva){
        if (TextUtils.isEmpty(actual) || TextUtils.isEmpty(nueva)){
            _ui.postValue(UiState.error("Complete ambas contraseñas"));
            return;
        }
        _ui.postValue(UiState.loading("Actualizando contraseña"));
        api.changePassword(token(), actual, nueva).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> res) {
                if (res.isSuccessful()){
                    _ui.postValue(UiState.success("Contraseña actualizada"));
                } else {
                    _ui.postValue(UiState.error("No se pudo cambiar la contraseña"));
                }
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {
                _ui.postValue(UiState.error("Error de red al cambiar contraseña"));
            }
        });
    }



}