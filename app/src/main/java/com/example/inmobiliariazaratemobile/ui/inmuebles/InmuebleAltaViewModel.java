package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
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

    private final MutableLiveData<String> _msg = new MutableLiveData<>(null);
    public final LiveData<String> msg = _msg;

    private final MutableLiveData<Uri> _imagenUri = new MutableLiveData<>(null);
    public final LiveData<Uri> imagenUri = _imagenUri;

    private final MutableLiveData<Boolean> _pickImageEvent = new MutableLiveData<>(false);
    public final LiveData<Boolean> pickImageEvent = _pickImageEvent;

    public InmuebleAltaViewModel(@NonNull Application app){ super(app); }

    public void onSeleccionarImagenClick(){ _pickImageEvent.setValue(true); }
    public void onImagenSeleccionada(Uri uri){ _imagenUri.setValue(uri); _pickImageEvent.setValue(false); }

    public void onGuardarClick(String direccion, String tipo, String uso,
                               String ambientes, String superficie,
                               String latitud, String longitud, String valor) {

        String err = validar(direccion, tipo, uso, valor, ambientes);
        if (err != null) { fail(err); return; }

        String bearer = ApiClient.bearer(getApplication());
        if (bearer == null) { fail("Token no disponible. Inicie sesión."); return; }

        InmuebleModel in = new InmuebleModel();
        in.setDireccion(direccion.trim());
        in.setTipo(tipo.trim());
        in.setUso(uso.trim());
        in.setAmbientes(Integer.parseInt(ambientes));
        try { in.setSuperficie(Double.parseDouble(n(dec(superficie)))); } catch (Exception ignored) {}
        try { in.setLatitud(Double.parseDouble(n(dec(latitud)))); } catch (Exception ignored) {}
        try { in.setLongitud(Double.parseDouble(n(dec(longitud)))); } catch (Exception ignored) {}
        in.setValor(Double.parseDouble(n(dec(valor))));
        in.setDisponible(true);

        // IMPORTANTE: .NET espera string plano en la parte "inmueble"
        String json = new Gson().toJson(in);
        RequestBody bodyJson = RequestBody.create(
                json.getBytes(StandardCharsets.UTF_8),
                MediaType.parse("text/plain")
        );

        Uri uri = _imagenUri.getValue();
        MultipartBody.Part partImg = buildPartFromUri(uri, "imagen");
        if (partImg == null) { fail("No se pudo leer la imagen."); return; }

        _status.postValue(Status.LOADING);
        ApiClient.getInmoService().cargarInmueble(bearer, partImg, bodyJson)
                .enqueue(new retrofit2.Callback<InmuebleModel>() {
                    @Override public void onResponse(Call<InmuebleModel> c, Response<InmuebleModel> r) {
                        if (r.isSuccessful()) { _status.postValue(Status.SUCCESS); return; }
                        String backend = null;
                        try { backend = r.errorBody()!=null ? r.errorBody().string() : null; } catch (Exception ignored) {}
                        fail("Error " + r.code() + (backend!=null ? ": " + backend : ""));
                    }
                    @Override public void onFailure(Call<InmuebleModel> c, Throwable t) {
                        fail("Red: " + t.getMessage());
                    }
                });
    }

    private String validar(String dir, String tipo, String uso, String valor, String amb){
        if (isEmpty(dir))  return "Dirección requerida";
        if (isEmpty(tipo)) return "Tipo requerido";
        if (isEmpty(uso))  return "Uso requerido";
        try { if (Double.parseDouble(n(dec(valor))) <= 0) return "Valor > 0"; }
        catch (Exception e){ return "Valor inválido"; }
        try { if (Integer.parseInt(amb) < 1) return "Ambientes >= 1"; }
        catch (Exception e){ return "Ambientes inválido"; }
        if (_imagenUri.getValue()==null) return "Imagen requerida";
        return null;
    }

    private String dec(String s){ return s==null? "": s.trim().replace(",", "."); }
    private String n(String s){ return s.isEmpty()? "0" : s; }
    private boolean isEmpty(String s){ return s==null || s.trim().isEmpty(); }
    private void fail(String m){ _msg.postValue(m); _status.postValue(Status.ERROR); }

    private MultipartBody.Part buildPartFromUri(Uri uri, String name){
        if (uri == null) return null;
        try {
            ContentResolver cr = getApplication().getContentResolver();
            String mime = cr.getType(uri);
            if (mime == null) {
                String ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
                mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
            }
            byte[] bytes = readAll(cr.openInputStream(uri));
            RequestBody rb = RequestBody.create(bytes, MediaType.parse(mime!=null?mime:"image/*"));
            return MultipartBody.Part.createFormData(name, "inmueble.jpg", rb);
        } catch (Exception e){
            return null;
        }
    }

    private byte[] readAll(InputStream is) throws Exception {
        try (InputStream in = is; ByteArrayOutputStream out = new ByteArrayOutputStream()){
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) >= 0) out.write(buf, 0, n);
            return out.toByteArray();
        }
    }
}
