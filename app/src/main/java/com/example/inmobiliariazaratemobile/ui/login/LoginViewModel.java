package com.example.inmobiliariazaratemobile.ui.login;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliariazaratemobile.MainActivity;
import com.example.inmobiliariazaratemobile.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {


    public static class UiModel {
        public final int progressVisibility;   // View.VISIBLE / View.GONE
        public final int errorVisibility;      // View.VISIBLE / View.GONE
        public final String errorText;         // null o texto
        public final boolean loginEnabled;     // enabled

        private UiModel(int p, int e, String t, boolean en) {
            progressVisibility = p; errorVisibility = e; errorText = t; loginEnabled = en;
        }
        public static UiModel idle()    { return new UiModel(View.GONE,  View.GONE,  null, true); }
        public static UiModel loading() { return new UiModel(View.VISIBLE,View.GONE,  null, false); }
        public static UiModel error(String msg){ return new UiModel(View.GONE, View.VISIBLE, msg, true); }
        public static UiModel success(){ return new UiModel(View.GONE, View.GONE, null, true); }
    }

    // Efectos de una sola vez
    public interface UiEffect { void apply(Activity a); }

    // Navegación a Inicio
    public static class NavigateToMain implements UiEffect {
        @Override public void apply(Activity a) {
            a.startActivity(new Intent(a, MainActivity.class));
            a.finish();
        }
    }

    private final MutableLiveData<UiModel> _ui = new MutableLiveData<>(UiModel.idle());
    public final LiveData<UiModel> ui = _ui;

    private final MutableLiveData<UiEffect> _effect = new MutableLiveData<>();
    public final LiveData<UiEffect> effect = _effect;

    public LoginViewModel(@NonNull Application app) { super(app); }


    public void onLoginClicked(String email, String pass) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            _ui.postValue(UiModel.error("Complete usuario y clave"));
            return;
        }
        _ui.postValue(UiModel.loading());

        ApiClient.InmoService api = ApiClient.getInmoService();
        api.loginForm(email, pass).enqueue(new Callback<String>() {
            @Override public void onResponse(Call<String> call, Response<String> res) {
                if (res.isSuccessful() && res.body() != null) {
                    String token = "Bearer " + res.body().trim();
                    ApiClient.guardarToken(getApplication(), token);
                    _ui.postValue(UiModel.success());
                    _effect.postValue(new NavigateToMain());
                } else {
                    _ui.postValue(UiModel.error("Usuario o Contraseña Incorrectas"));
                }
            }
            @Override public void onFailure(Call<String> call, Throwable t) {
                _ui.postValue(UiModel.error("Error de red"));
            }
        });
    }
}
