package com.example.inmobiliariazaratemobile.ui.perfil;

import android.app.Application;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliariazaratemobile.model.PropietarioModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    // ---- UI State ----
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
    public final LiveData<PropietarioModel> propietario = _prop;

    private final MutableLiveData<Boolean> _editMode = new MutableLiveData<>(false);
    public final LiveData<Boolean> editMode = _editMode;

    private final MutableLiveData<UiState> _ui = new MutableLiveData<>(UiState.idle());
    public final LiveData<UiState> ui = _ui;

    private final ApiClient.InmoService api = ApiClient.getInmoService();

    // ---- Reglas de validación ----
    private static final Pattern EMAIL = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );
    private static final Pattern SOLO_DIGITOS_MIN7 = Pattern.compile("^\\d{7,}$");

    public PerfilViewModel(@NonNull Application app) { super(app); }

    private String token(){
        String t = ApiClient.leerToken(getApplication());
        return t != null ? t : "";
    }

    // ---- Acciones de caso de uso ----
    public void cargarPerfil(){
        _ui.postValue(UiState.loading("Cargando perfil"));
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

    /** UI llama a este método al clickear el botón. Si estaba en vista, pasa a edición.
     *  Si estaba en edición, intenta guardar con los datos provistos. */
    public void toggleEditarGuardar(PropietarioModel datosDesdeUI){
        boolean edit = Boolean.TRUE.equals(_editMode.getValue());
        if (!edit){
            _editMode.postValue(true);
            return;
        }
        // Estaba en edición → guardar
        actualizarPerfil(datosDesdeUI);
    }

    public void cancelarEdicion(){
        // Restablece datos remotos por si hubo cambios locales no guardados
        _editMode.postValue(false);
        cargarPerfil();
    }

    public void actualizarPerfil(PropietarioModel p){
        String error = validarPerfil(p);
        if (error != null){
            _ui.postValue(UiState.error(error));
            return;
        }

        // Asegurar que NO se actualice la contraseña(tenia un problema con la actualizacon de la contraseña mas o menos chat gpt me tiro esta data-funciona perooooooooo no se si es la mejor practica)
        try {

            p.setClave(null);

        } catch (Exception ignored) {}

        _ui.postValue(UiState.loading("Guardando"));
        api.actualizarProp(token(), p).enqueue(new retrofit2.Callback<PropietarioModel>() {
            @Override public void onResponse(retrofit2.Call<PropietarioModel> call,
                                             retrofit2.Response<PropietarioModel> res) {
                if (res.isSuccessful() && res.body()!=null){
                    _prop.postValue(res.body());
                    _editMode.postValue(false);
                    _ui.postValue(UiState.success("Perfil guardado"));
                } else {
                    _ui.postValue(UiState.error("No se pudo guardar"));
                }
            }
            @Override public void onFailure(retrofit2.Call<PropietarioModel> call, Throwable t) {
                _ui.postValue(UiState.error("Error de red al guardar"));
            }
        });
    }

    public void cambiarPassword(String actual, String nueva){
        String err = validarPassword(actual, nueva);
        if (err != null){
            _ui.postValue(UiState.error(err));
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

    // ---- Lógica/Validaciones (solo ViewModel) ----
    private String validarPerfil(PropietarioModel p){
        if (p == null) return "Datos inválidos";
        if (isEmpty(p.getNombre())) return "Nombre es obligatorio";
        if (isEmpty(p.getApellido())) return "Apellido es obligatorio";
        if (isEmpty(p.getEmail())) return "Email es obligatorio";
        if (!EMAIL.matcher(p.getEmail().trim()).matches()) return "Email no válido";
        String tel = emptyToNull(p.getTelefono());
        if (tel != null && !SOLO_DIGITOS_MIN7.matcher(tel).matches())
            return "Teléfono debe tener solo dígitos (mín. 7)";
        return null;
    }

    private String validarPassword(String actual, String nueva){
        if (isEmpty(actual) || isEmpty(nueva)) return "Complete ambas contraseñas";
        if (actual.equals(nueva)) return "La nueva contraseña debe ser distinta";
        if (nueva.length() < 8) return "La contraseña debe tener al menos 8 caracteres";
        boolean tieneLetra = nueva.matches(".*[A-Za-z].*");
        boolean tieneNumero = nueva.matches(".*\\d.*");
        if (!(tieneLetra && tieneNumero)) return "Use letras y números";
        return null;
    }

    private static boolean isEmpty(String s){ return TextUtils.isEmpty(s) || s.trim().isEmpty(); }
    private static String emptyToNull(String s){ return isEmpty(s) ? null : s.trim(); }
}
