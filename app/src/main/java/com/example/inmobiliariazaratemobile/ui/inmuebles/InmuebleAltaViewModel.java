package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.*;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;
import com.google.gson.Gson;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class InmuebleAltaViewModel extends AndroidViewModel {

    public enum Status { IDLE, LOADING, SUCCESS, ERROR }
    private final MutableLiveData<Status> _status = new MutableLiveData<>(Status.IDLE);
    public final LiveData<Status> status = _status;

    private final MutableLiveData<Uri> _imagenUri = new MutableLiveData<>(null);
    public final LiveData<Uri> imagenUri = _imagenUri;

    public InmuebleAltaViewModel(@NonNull Application app){ super(app); }

    public void onImagenSeleccionada(Uri uri){ _imagenUri.setValue(uri); }

    // Validaciones mínimas de cliente
    private String validar(String direccion, String tipo, String uso, String valor, String ambientes){
        if (direccion==null || direccion.trim().isEmpty()) return "Dirección requerida";
        if (tipo==null || tipo.trim().isEmpty()) return "Tipo requerido";
        if (uso==null || uso.trim().isEmpty()) return "Uso requerido";
        try { if (Double.parseDouble(valor) <= 0) return "Valor > 0"; } catch (Exception e){ return "Valor inválido"; }
        try { if (Integer.parseInt(ambientes) < 1) return "Ambientes >= 1"; } catch (Exception e){ return "Ambientes inválido"; }
        if (_imagenUri.getValue()==null) return "Imagen requerida";
        return null;
    }

    public void guardar(String direccion, String tipo, String uso, String ambientes,
                        String superficie, String latitud, String longitud, String valor){
        String err = validar(direccion, tipo, uso, valor, ambientes);
        if (err != null){ _status.postValue(Status.ERROR); return; }

        InmuebleModel in = new InmuebleModel();
        in.setDireccion(direccion.trim());
        in.setTipo(tipo.trim());
        in.setUso(uso.trim());
        try { in.setAmbientes(Integer.parseInt(ambientes)); } catch (Exception ignored) {}
        try { in.setSuperficie(Double.parseDouble(superficie)); } catch (Exception ignored) {}
        try { in.setLatitud(Double.parseDouble(latitud)); } catch (Exception ignored) {}
        try { in.setLongitud(Double.parseDouble(longitud)); } catch (Exception ignored) {}
        try { in.setValor(Double.parseDouble(valor)); } catch (Exception ignored) {}
        in.setDisponible(true);

        // JSON
        String json = new Gson().toJson(in);
        RequestBody bodyJson = RequestBody.create(
                json.getBytes(StandardCharsets.UTF_8),
                MediaType.parse("application/json; charset=utf-8")
        );

        // Imagen
        Uri uri = _imagenUri.getValue();
        MultipartBody.Part partImg = buildPartFromUri(uri, "imagen");

        _status.postValue(Status.LOADING);
        String bearer = ApiClient.bearer(getApplication());
        ApiClient.getInmoService().cargarInmueble(bearer, partImg, bodyJson)
                .enqueue(new retrofit2.Callback<InmuebleModel>() {
                    @Override public void onResponse(Call<InmuebleModel> c, Response<InmuebleModel> r) {
                        _status.postValue(r.isSuccessful() ? Status.SUCCESS : Status.ERROR);
                    }
                    @Override public void onFailure(Call<InmuebleModel> c, Throwable t) {
                        _status.postValue(Status.ERROR);
                    }
                });
    }

    private MultipartBody.Part buildPartFromUri(Uri uri, String name){
        try {
            ContentResolver cr = getApplication().getContentResolver();
            String mime = cr.getType(uri);
            if (mime == null) {
                String ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
                mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
            }
            InputStream is = cr.openInputStream(uri);
            byte[] bytes = is.readAllBytes(); // API 26+
            RequestBody rb = RequestBody.create(bytes, MediaType.parse(mime!=null?mime:"image/*"));
            return MultipartBody.Part.createFormData(name, "inmueble.jpg", rb);
        } catch (Exception e){
            return MultipartBody.Part.createFormData(name, "inmueble.jpg",
                    RequestBody.create(new byte[0], MediaType.parse("application/octet-stream")));
        }
    }
}
