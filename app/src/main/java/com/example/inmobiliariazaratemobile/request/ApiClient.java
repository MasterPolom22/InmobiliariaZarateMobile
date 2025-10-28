package com.example.inmobiliariazaratemobile.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.example.inmobiliariazaratemobile.model.PropietarioModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiClient {
    public static final String BASE_URL = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";

    private static Retrofit retrofit;

    private static Retrofit getRetrofit(){
        if (retrofit == null){
            Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static InmoService getInmoService(){
        return getRetrofit().create(InmoService.class);
    }

    public static void guardarToken(Context context, String token) {
        String limpio = token == null ? null : token.replace("\"", "").trim();
        context.getSharedPreferences("token.xml", Context.MODE_PRIVATE)
                .edit().putString("token", limpio).apply();
    }

    public static String leerToken(Context context) {
        return context.getSharedPreferences("token.xml", Context.MODE_PRIVATE)
                .getString("token", null);
    }

    public static String bearer(Context ctx){
        String t = leerToken(ctx);
        if (t == null) return null;
        t = t.trim();
        // Si ya viene con "Bearer " no lo agregues otra vez
        return t.startsWith("Bearer ") ? t : "Bearer " + t;
    }

    public interface InmoService{
        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> loginForm(@Field("Usuario") String usuario, @Field("Clave") String clave);

        @GET("api/Propietarios")
        Call<PropietarioModel> getPropietario(@Header("Authorization") String token);

        @PUT("api/Propietarios/actualizar")
        Call<PropietarioModel> actualizarProp(@Header("Authorization") String token, @Body PropietarioModel p);

        @FormUrlEncoded
        @PUT("api/Propietarios/changePassword")
        Call<Void> changePassword(@Header("Authorization") String token,
                                  @Field("currentPassword") String currentPassword,
                                  @Field("newPassword") String newPassword);

        @PUT("api/Inmuebles/actualizar")
        Call<InmuebleModel> actualizarDisponible(@Header("Authorization") String token,
                                                 @Body InmuebleModel inmueble);

        @GET("api/Inmuebles")
        Call<List<InmuebleModel>> listarInmuebles(@Header("Authorization") String token);



        //  usar pasos 2 y 3
        @GET("api/Inmuebles/GetContratoVigente")
        Call<List<InmuebleModel>> listarConContrato(@Header("Authorization") String bearer);

        @GET("api/Inmuebles/GetContratoVigente")
        Call<List<InmuebleModel>> listarConContratoVigente(@Header("Authorization") String token);



        @Multipart
        @POST("api/Inmuebles/cargar")
        Call<InmuebleModel> cargarInmueble(
                @Header("Authorization") String token,
                @Part MultipartBody.Part imagen,
                @Part("inmueble") RequestBody inmueble
        );









    }
}