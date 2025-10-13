package com.example.inmobiliariazaratemobile.ui.login;

import static androidx.lifecycle.AndroidViewModel_androidKt.getApplication;



import android.app.Application;
import android.text.TextUtils;
import androidx.annotation.NonNull;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliariazaratemobile.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;





    public class LoginViewModel extends ViewModel {

        public enum Status { IDLE, LOADING, SUCCESS, ERROR }

        public static class UiState {
            public final Status status;
            public final String message;
            UiState(Status s, String m){ status=s; message=m; }
            static UiState idle(){ return new UiState(Status.IDLE,null); }
            static UiState loading(){ return new UiState(Status.LOADING,"Cargando"); }
            static UiState success(){ return new UiState(Status.SUCCESS,null); }
            static UiState error(String m){ return new UiState(Status.ERROR,m); }
        }

        private final MutableLiveData<UiState> _ui = new MutableLiveData<>(UiState.idle());
        public LiveData<UiState> ui = _ui;

        public LoginViewModel(@NonNull Application app){ super(app); }

        public void login(String email, String pass){
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                _ui.postValue(UiState.error("Complete usuario y clave"));
                return;
            }
            _ui.postValue(UiState.loading());

            ApiClient.InmoService api = ApiClient.getInmoService();
            api.loginForm(email, pass).enqueue(new Callback<String>() {
                @Override public void onResponse(Call<String> call, Response<String> res) {
                    if (res.isSuccessful() && res.body()!=null){
                        String token = "Bearer " + res.body().trim();
                        ApiClient.guardarToken(getApplication(), token);
                        _ui.postValue(UiState.success());
                    } else {
                        _ui.postValue(UiState.error("Credenciales inválidas"));
                    }
                }
                @Override public void onFailure(Call<String> call, Throwable t) {
                    _ui.postValue(UiState.error("Error de red"));
                }
            });
        }

    }


