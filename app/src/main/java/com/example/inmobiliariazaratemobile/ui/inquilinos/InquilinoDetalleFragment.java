package com.example.inmobiliariazaratemobile.ui.inquilinos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliariazaratemobile.R;
import com.example.inmobiliariazaratemobile.databinding.FragmentInquilinoDetalleBinding;
import com.example.inmobiliariazaratemobile.model.InquilinoModel;

public class InquilinoDetalleFragment extends Fragment {

    private FragmentInquilinoDetalleBinding b;
    private InquilinoDetalleViewModel vm;

    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentInquilinoDetalleBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InquilinoDetalleViewModel.class);

        vm.inquilino.observe(getViewLifecycleOwner(), this::render);
        vm.status.observe(getViewLifecycleOwner(), s -> {
            b.progress.setVisibility(s == InquilinoDetalleViewModel.Status.LOADING ? View.VISIBLE : View.GONE);
            b.error.setVisibility(s == InquilinoDetalleViewModel.Status.ERROR ? View.VISIBLE : View.GONE);
            b.content.setVisibility(s == InquilinoDetalleViewModel.Status.OK ? View.VISIBLE : View.GONE);
        });

        int inmuebleId = getArguments()!=null ? getArguments().getInt("inmuebleId", -1) : -1;
        if (savedInstanceState == null && inmuebleId>0) vm.cargarPorInmueble(inmuebleId);

        return b.getRoot();
    }

    private void render(InquilinoModel q){
        if (q == null) return;
        b.tvNombre.setText(q.getNombre());
        b.tvNombre.setText(q.getApellido());
        b.tvDni.setText(q.getDni());
        b.tvTrabajo.setText(q.getLugarTrabajo());
        b.tvTel.setText(q.getTelefono());
        b.tvEmail.setText(q.getEmail());

    }

}