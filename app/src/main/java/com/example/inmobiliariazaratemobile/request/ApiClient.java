package com.example.inmobiliariazaratemobile.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.*;

import com.example.inmobiliariazaratemobile.model.PropietarioModel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public class ApiClient {
    private static  String BASE_URL = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";


    public static InmoService getInmoService(){
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(InmoService.class);
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
        Call<Void> changePassword(
                @Header("Authorization") String token,
                @Field("currentPassword") String currentPassword,
                @Field("newPassword") String newPassword
        );



    }

    public static void guardarToken(Context context, String token) {

        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString("token", token);

        editor.apply();

    }
    public static String leerToken(Context context) {

        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);

        return sp.getString("token", null);

    }




    public interface InmuebleService {

        @GET("api/Inmuebles")
        Call<List<InmuebleModel>> listar(@Header("Authorization") String bearer);

        @GET("api/Inmuebles/{id}")
        Call<InmuebleModel> obtener(@Header("Authorization") String bearer,
                                    @Path("id") int id);

        @POST("api/Inmuebles")
        Call<InmuebleModel> crear(@Header("Authorization") String bearer,
                                  @Body InmuebleModel body);

        @PUT("api/Inmuebles/{id}")
        Call<InmuebleModel> actualizar(@Header("Authorization") String bearer,
                                       @Path("id") int id,
                                       @Body InmuebleModel body);

        @DELETE("api/Inmuebles/{id}")
        Call<Void> eliminar(@Header("Authorization") String bearer,
                            @Path("id") int id);

        // Opcional si tu API lo tiene
        @PUT("api/Inmuebles/{id}/disponible")
        Call<InmuebleModel> toggleDisponible(@Header("Authorization") String bearer,
                                             @Path("id") int id);




        // Cambiar disponibilidad
        @PUT("api/Inmuebles/{id}/disponible")
        Call<InmuebleModel> setDisponible(@Header("Authorization") String bearer,
                                          @Path("id") int id,
                                          @Query("value") boolean value);

        // Crear con foto (multipart). El backend debe aceptar estos nombres de parte.
        @Multipart
        @POST("api/Inmuebles")
        Call<InmuebleModel> crear(@Header("Authorization") String bearer,
                                  @Part("direccion") RequestBody direccion,
                                  @Part("uso") RequestBody uso,
                                  @Part("tipo") RequestBody tipo,
                                  @Part("ambientes") RequestBody ambientes,
                                  @Part("superficie") RequestBody superficie,
                                  @Part("latitud") RequestBody latitud,
                                  @Part("longitud") RequestBody longitud,
                                  @Part("valor") RequestBody valor,
                                  @Part("disponible") RequestBody disponible, // false por defecto
                                  @Part MultipartBody.Part imagenFile); // opcional
    }

    private static InmuebleService INMUEBLE;




}


