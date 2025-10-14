package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;
import com.example.inmobiliariazaratemobile.request.ApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;

import java.util.List;

public class InmueblesViewModel extends AndroidViewModel {



    public enum Status { IDLE, LOADING, SUCCESS, ERROR }
    public static class UiState {
        public final Status status; public final String message;
        UiState(Status s, String m){ status=s; message=m; }
        public static UiState idle(){ return new UiState(Status.IDLE,null); }
        public static UiState loading(String m){ return new UiState(Status.LOADING,m); }
        public static UiState success(String m){ return new UiState(Status.SUCCESS,m); }
        public static UiState error(String m){ return new UiState(Status.ERROR,m); }
    }

    private final ApiClient.InmuebleService api = (ApiClient.InmuebleService) ApiClient.getInmoService();

    private final MutableLiveData<List<InmuebleModel>> _items = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<InmuebleModel>> items = _items;

    private final MutableLiveData<UiState> _ui = new MutableLiveData<>(UiState.idle());
    public final LiveData<UiState> ui = _ui;

    public InmueblesViewModel(@NonNull Application app){ super(app); }

    private String bearer(){
        String t = ApiClient.leerToken(getApplication());
        return t == null ? "" : t;
    }

    // LISTAR
    public void cargar(){
        _ui.postValue(UiState.loading("Cargando inmuebles"));
        api.listar(bearer()).enqueue(new Callback<List<InmuebleModel>>() {
            @Override public void onResponse(Call<List<InmuebleModel>> call, Response<List<InmuebleModel>> res) {
                if (res.isSuccessful() && res.body()!=null){
                    _items.postValue(res.body());
                    _ui.postValue(UiState.idle());
                } else _ui.postValue(UiState.error("No se pudo cargar"));
            }
            @Override public void onFailure(Call<List<InmuebleModel>> call, Throwable t) {
                _ui.postValue(UiState.error("Error de red al listar"));
            }
        });
    }

    // HABILITAR / DESHABILITAR
    public void setDisponible(InmuebleModel item, boolean value){
        if (item == null || item.getIdInmueble() <= 0){
            _ui.postValue(UiState.error("Inmueble inválido"));
            return;
        }
        _ui.postValue(UiState.loading(value ? "Habilitando" : "Deshabilitando"));
        api.setDisponible(bearer(), item.getIdInmueble(), value)
                .enqueue(new Callback<InmuebleModel>() {
                    @Override public void onResponse(Call<InmuebleModel> call, Response<InmuebleModel> res) {
                        if (res.isSuccessful() && res.body()!=null){
                            // Actualiza en la lista local
                            List<InmuebleModel> cur = _items.getValue();
                            if (cur != null){
                                for (int i=0;i<cur.size();i++){
                                    if (cur.get(i).getIdInmueble() == item.getIdInmueble()){
                                        cur.set(i, res.body());
                                        break;
                                    }
                                }
                                _items.postValue(new ArrayList<>(cur));
                            }
                            _ui.postValue(UiState.success(value ? "Habilitado" : "Deshabilitado"));
                        } else _ui.postValue(UiState.error("No se pudo actualizar disponibilidad"));
                    }
                    @Override public void onFailure(Call<InmuebleModel> call, Throwable t) {
                        _ui.postValue(UiState.error("Error de red al actualizar"));
                    }
                });
    }

    // CREAR NUEVO (con foto) -> disponible=false por defecto
    public void crearNuevo(InputNuevo data, Uri imagenUri){
        String err = validar(data);
        if (err != null){ _ui.postValue(UiState.error(err)); return; }

        _ui.postValue(UiState.loading("Creando inmueble"));

        // Partes texto
        RequestBody direccion = rbText(data.direccion);
        RequestBody uso = rbText(data.uso);
        RequestBody tipo = rbText(data.tipo);
        RequestBody ambientes = rbText(String.valueOf(data.ambientes));
        RequestBody superficie = rbText(String.valueOf(data.superficie));
        RequestBody latitud = rbText(String.valueOf(data.latitud));
        RequestBody longitud = rbText(String.valueOf(data.longitud));
        RequestBody valor = rbText(String.valueOf(data.valor));
        RequestBody disponible = rbText("false"); // por defecto

        // Parte archivo (opcional)
        MultipartBody.Part imgPart = null;
        if (imagenUri != null){
            try {
                imgPart = buildFilePart("imagenFile", imagenUri);
            } catch (IOException e) {
                _ui.postValue(UiState.error("No se pudo leer la imagen"));
                return;
            }
        }

        api.crear(bearer(), direccion, uso, tipo, ambientes, superficie, latitud, longitud, valor, disponible, imgPart)
                .enqueue(new Callback<InmuebleModel>() {
                    @Override public void onResponse(Call<InmuebleModel> call, Response<InmuebleModel> res) {
                        if (res.isSuccessful() && res.body()!=null){
                            List<InmuebleModel> cur = _items.getValue();
                            if (cur == null) cur = new ArrayList<>();
                            cur.add(0, res.body());
                            _items.postValue(new ArrayList<>(cur));
                            _ui.postValue(UiState.success("Inmueble creado"));
                        } else _ui.postValue(UiState.error("No se pudo crear"));
                    }
                    @Override public void onFailure(Call<InmuebleModel> call, Throwable t) {
                        _ui.postValue(UiState.error("Error de red al crear"));
                    }
                });
    }

    // ----- Helpers -----
    public static class InputNuevo {
        public String direccion, uso, tipo;
        public int ambientes;
        public double superficie, latitud, longitud, valor;
    }

    private String validar(InputNuevo d){
        if (d == null) return "Datos inválidos";
        if (isEmpty(d.direccion)) return "Dirección obligatoria";
        if (isEmpty(d.uso)) return "Uso obligatorio";
        if (isEmpty(d.tipo)) return "Tipo obligatorio";
        if (d.ambientes <= 0) return "Ambientes debe ser > 0";
        if (d.superficie <= 0) return "Superficie debe ser > 0";
        // lat/long opcionales; si los usas, rangos:
        if (d.latitud != 0 && (d.latitud < -90 || d.latitud > 90)) return "Latitud inválida";
        if (d.longitud != 0 && (d.longitud < -180 || d.longitud > 180)) return "Longitud inválida";
        if (d.valor < 0) return "Valor no puede ser negativo";
        return null;
    }

    private static boolean isEmpty(String s){ return TextUtils.isEmpty(s) || s.trim().isEmpty(); }
    private static RequestBody rbText(String v){ return RequestBody.create(v==null?"":v, MediaType.parse("text/plain")); }

    private MultipartBody.Part buildFilePart(String partName, Uri uri) throws IOException {
        ContentResolver cr = getApplication().getContentResolver();
        String fileName = guessFileName(cr, uri);
        MediaType mt = MediaType.parse(cr.getType(uri) != null ? cr.getType(uri) : "image/*");

        // Lee bytes del ContentResolver
        byte[] bytes;
        try (InputStream is = cr.openInputStream(uri)) {
            if (is == null) throw new IOException("InputStream nulo");
            bytes = is.readAllBytes();
        }
        RequestBody body = RequestBody.create(bytes, mt);
        return MultipartBody.Part.createFormData(partName, fileName, body);
    }

    private String guessFileName(ContentResolver cr, Uri uri){
        String name = "foto.jpg";
        Cursor c = cr.query(uri, null, null, null, null);
        if (c != null){
            int i = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (i >= 0 && c.moveToFirst()){
                String n = c.getString(i);
                if (!TextUtils.isEmpty(n)) name = n;
            }
            c.close();
        }
        return name;
    }
}