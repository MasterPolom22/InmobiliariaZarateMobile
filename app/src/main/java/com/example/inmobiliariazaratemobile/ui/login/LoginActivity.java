package com.example.inmobiliariazaratemobile.ui.login;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.inmobiliariazaratemobile.databinding.ActivityLoginBinding;

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
                vm.onLoginClicked(
                        b.txtEmail.getText().toString().trim(),
                        b.txtPass.getText().toString().trim()
                )
        );


        vm.ui.observe(this, this::render);

        // Efectos de una sola vez
        vm.effect.observe(this, e -> e.apply(this));
    }


    private void render(LoginViewModel.UiModel m){
        b.progress.setVisibility(m.progressVisibility);
        b.lblError.setVisibility(m.errorVisibility);
        b.lblError.setText(m.errorText);
        b.btnLogin.setEnabled(m.loginEnabled);
    }
}
