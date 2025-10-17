package com.example.inmobiliariazaratemobile.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.app.AlertDialog;
import android.text.InputType;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliariazaratemobile.databinding.FragmentPerfilBinding;
import com.example.inmobiliariazaratemobile.model.PropietarioModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;

import java.util.regex.Pattern;

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

    public static class UiModel {
        public final String idText;
        public final String nombreText;
        public final String apellidoText;
        public final String emailText;
        public final String telefonoText;

        public final boolean editEnabled;
        public final String btnEditarGuardarText;
        public final int btnCancelarVisibility;

        public final int progressVisibility;
        public final int msgVisibility;
        public final String msgText;

        UiModel(String idText, String nombreText, String apellidoText, String emailText, String telefonoText,
                boolean editEnabled, String btnEditarGuardarText, int btnCancelarVisibility,
                int progressVisibility, int msgVisibility, String msgText) {
            this.idText = idText;
            this.nombreText = nombreText;
            this.apellidoText = apellidoText;
            this.emailText = emailText;
            this.telefonoText = telefonoText;
            this.editEnabled = editEnabled;
            this.btnEditarGuardarText = btnEditarGuardarText;
            this.btnCancelarVisibility = btnCancelarVisibility;
            this.progressVisibility = progressVisibility;
            this.msgVisibility = msgVisibility;
            this.msgText = msgText;
        }
    }

    public interface UiEffect {
        void apply(Context ctx, FragmentPerfilBinding b, PerfilViewModel vm);
    }

    public static class ToastEffect implements UiEffect {
        private final String message;
        public ToastEffect(String m){ this.message = m; }
        @Override public void apply(Context ctx, FragmentPerfilBinding b, PerfilViewModel vm) {
            if (message != null && !message.isEmpty()) Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static class ShowChangePasswordDialogEffect implements UiEffect {
        @Override public void apply(Context ctx, FragmentPerfilBinding b, PerfilViewModel vm) {
            EditText etActual = new EditText(ctx);
            etActual.setHint("Actual");
            etActual.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etActual.setImeOptions(EditorInfo.IME_ACTION_NEXT);

            EditText etNueva = new EditText(ctx);
            etNueva.setHint("Nueva");
            etNueva.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            LinearLayout ll = new LinearLayout(ctx);
            ll.setOrientation(LinearLayout.VERTICAL);
            int pad = (int) (16 * ctx.getResources().getDisplayMetrics().density);
            ll.setPadding(pad, pad, pad, 0);
            ll.addView(etActual);
            ll.addView(etNueva);

            new AlertDialog.Builder(ctx)
                    .setTitle("Cambiar contraseña")
                    .setView(ll)
                    .setPositiveButton("Guardar", (d, w) ->
                            vm.onConfirmCambioPass(
                                    etActual.getText().toString().trim(),
                                    etNueva.getText().toString().trim()
                            )
                    )
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
    }

    private final MutableLiveData<PropietarioModel> _prop = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _editMode = new MutableLiveData<>(false);
    private final MutableLiveData<UiState> _ui = new MutableLiveData<>(UiState.idle());

    public final LiveData<UiModel> uiModel;
    public final LiveData<UiEffect> effect;
    private final MutableLiveData<UiEffect> _effect = new MutableLiveData<>();

    private final ApiClient.InmoService api = ApiClient.getInmoService();

    private static final Pattern EMAIL = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );
    private static final Pattern SOLO_DIGITOS_MIN3 = Pattern.compile("^\\d{7,}$");

    public PerfilViewModel(@NonNull Application app) {
        super(app);
        effect = _effect;

        MutableLiveData<UiModel> uiModelMutable = new MutableLiveData<>(composeUi(null, false, UiState.idle()));
        uiModel = uiModelMutable;

        _prop.observeForever(p -> uiModelMutable.postValue(composeUi(p, val(_editMode.getValue()), val(_ui.getValue()))));
        _editMode.observeForever(e -> uiModelMutable.postValue(composeUi(_prop.getValue(), val(e), val(_ui.getValue()))));
        _ui.observeForever(s -> uiModelMutable.postValue(composeUi(_prop.getValue(), val(_editMode.getValue()), val(s))));
    }

    private static boolean val(Boolean b){ return b != null && b; }
    private static UiState val(UiState s){ return s != null ? s : UiState.idle(); }
    private static String nn(String s){ return s == null ? "" : s; }

    private UiModel composeUi(PropietarioModel p, boolean edit, UiState s){
        String idText = p == null ? "" : String.valueOf(p.getIdPropietario());
        String nombre = p == null ? "" : nn(p.getNombre());
        String apellido = p == null ? "" : nn(p.getApellido());
        String email = p == null ? "" : nn(p.getEmail());
        String telefono = p == null ? "" : nn(p.getTelefono());

        String btnText = edit ? "Guardar" : "Editar";
        int cancelarVis = edit ? View.VISIBLE : View.GONE;

        int progressVis = s.status == Status.LOADING ? View.VISIBLE : View.GONE;
        int msgVis = s.status == Status.ERROR ? View.VISIBLE : View.GONE;
        String msg = s.message;

        return new UiModel(
                idText, nombre, apellido, email, telefono,
                edit, btnText, cancelarVis,
                progressVis, msgVis, msg
        );
    }

    private String token(){
        String t = ApiClient.leerToken(getApplication());
        return t != null ? t : "";
    }

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

    public void onEditarGuardarClick(PropietarioModel datosDesdeUI){
        boolean edit = val(_editMode.getValue());
        if (!edit){
            _editMode.postValue(true);
            return;
        }
        actualizarPerfil(datosDesdeUI);
    }

    public void onCancelarClick(){
        _editMode.postValue(false);
        cargarPerfil();
    }

    public void onCambiarPassClick(){
        _effect.postValue(new ShowChangePasswordDialogEffect());
    }

    public void onConfirmCambioPass(String actual, String nueva){
        cambiarPassword(actual, nueva);
    }

    public void actualizarPerfil(PropietarioModel p){
        String error = validarPerfil(p);
        if (error != null){
            _ui.postValue(UiState.error(error));
            return;
        }
        try { p.setClave(null); } catch (Exception ignored) {}
        _ui.postValue(UiState.loading("Guardando"));
        api.actualizarProp(token(), p).enqueue(new Callback<PropietarioModel>() {
            @Override public void onResponse(Call<PropietarioModel> call, Response<PropietarioModel> res) {
                if (res.isSuccessful() && res.body()!=null){
                    _prop.postValue(res.body());
                    _editMode.postValue(false);
                    _ui.postValue(UiState.success("Perfil guardado"));
                    _effect.postValue(new ToastEffect("Perfil guardado"));
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
                    _effect.postValue(new ToastEffect("Contraseña actualizada"));
                } else {
                    _ui.postValue(UiState.error("No se pudo cambiar la contraseña"));
                }
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {
                _ui.postValue(UiState.error("Error de red al cambiar contraseña"));
            }
        });
    }

    private String validarPerfil(PropietarioModel p){
        if (p == null) return "Datos inválidos";
        if (isEmpty(p.getNombre())) return "Nombre es obligatorio";
        if (isEmpty(p.getApellido())) return "Apellido es obligatorio";
        if (isEmpty(p.getEmail())) return "Email es obligatorio";
        if (!EMAIL.matcher(p.getEmail().trim()).matches()) return "Email no válido";
        String tel = emptyToNull(p.getTelefono());
        if (tel != null && !SOLO_DIGITOS_MIN3.matcher(tel).matches()) return "Teléfono debe tener solo dígitos (mín. 7)";
        return null;
    }

    private String validarPassword(String actual, String nueva){
        if (isEmpty(actual) || isEmpty(nueva)) return "Complete ambas contraseñas";
        if (actual.equals(nueva)) return "La nueva contraseña debe ser distinta";
        if (nueva.length() < 3) return "La contraseña debe tener al menos 8 caracteres";
        boolean tieneLetra = nueva.matches(".*[A-Za-z].*");
        boolean tieneNumero = nueva.matches(".*\\d.*");
        if (!(tieneLetra || tieneNumero)) return "Use letras y números";
        return null;
    }

    private static boolean isEmpty(String s){ return TextUtils.isEmpty(s) || s.trim().isEmpty(); }
    private static String emptyToNull(String s){ return isEmpty(s) ? null : s.trim(); }
}
