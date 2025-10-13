package com.example.inmobiliariazaratemobile.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliariazaratemobile.MainActivity;
import com.example.inmobiliariazaratemobile.databinding.ActivityLoginBinding;

import com.example.inmobiliariazaratemobile.R;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding b;
    private LoginViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        vm = new ViewModelProvider(this).get(LoginViewModel.class);

        b.btnLogin.setOnClickListener(v ->
                vm.login(
                        b.txtEmail.getText().toString().trim(),
                        b.txtPass.getText().toString().trim()
                )
        );

        vm.ui.observe(this, state -> {
            if (state == null) return;
            switch (state.status){
                case LOADING:
                    b.progress.setVisibility(View.VISIBLE);
                    b.lblError.setVisibility(View.GONE);
                    b.btnLogin.setEnabled(false);
                    break;
                case SUCCESS:
                    b.progress.setVisibility(View.GONE);
                    b.btnLogin.setEnabled(true);
                    b.lblError.setVisibility(View.GONE);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    break;
                case ERROR:
                    b.progress.setVisibility(View.GONE);
                    b.btnLogin.setEnabled(true);
                    b.lblError.setVisibility(View.VISIBLE);
                    b.lblError.setText(state.message);
                    break;
                default:
                    b.progress.setVisibility(View.GONE);
                    b.btnLogin.setEnabled(true);
                    b.lblError.setVisibility(View.GONE);
            }
        });
    }
}