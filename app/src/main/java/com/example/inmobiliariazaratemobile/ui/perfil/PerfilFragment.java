package com.example.inmobiliariazaratemobile.ui.perfil;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliariazaratemobile.databinding.FragmentPerfilBinding;
import com.example.inmobiliariazaratemobile.model.PropietarioModel;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding b;
    private PerfilViewModel vm;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentPerfilBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(PerfilViewModel.class);

        b.btnEditarGuardar.setOnClickListener(v -> vm.onEditarGuardarClick(leerUI()));
        b.btnCancelar.setOnClickListener(v -> vm.onCancelarClick());
        b.btnCambiarPass.setOnClickListener(v -> vm.onCambiarPassClick());

        vm.uiModel.observe(getViewLifecycleOwner(), this::render);
        vm.effect.observe(getViewLifecycleOwner(), e -> {
            if (e != null) e.apply(requireContext(), b, vm);
        });

        vm.cargarPerfil();
    }

    private void render(PerfilViewModel.UiModel m){
        b.txtId.setText(m.idText);
        b.txtNombre.setText(m.nombreText);
        b.txtApellido.setText(m.apellidoText);
        b.txtEmail.setText(m.emailText);
        b.txtTelefono.setText(m.telefonoText);

        b.txtNombre.setEnabled(m.editEnabled);
        b.txtApellido.setEnabled(m.editEnabled);
        b.txtEmail.setEnabled(m.editEnabled);
        b.txtTelefono.setEnabled(m.editEnabled);

        b.btnEditarGuardar.setText(m.btnEditarGuardarText);
        b.btnCancelar.setVisibility(m.btnCancelarVisibility);

        b.progress.setVisibility(m.progressVisibility);
        b.lblMsg.setVisibility(m.msgVisibility);
        b.lblMsg.setText(m.msgText);
    }

    private PropietarioModel leerUI(){
        PropietarioModel p = new PropietarioModel();
        p.setNombre(b.txtNombre.getText().toString().trim());
        p.setApellido(b.txtApellido.getText().toString().trim());
        p.setEmail(b.txtEmail.getText().toString().trim());
        p.setTelefono(b.txtTelefono.getText().toString().trim());
        return p;
    }
}
