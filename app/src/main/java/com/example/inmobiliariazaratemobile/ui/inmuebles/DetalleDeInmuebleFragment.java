package com.example.inmobiliariazaratemobile.ui.inmuebles;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliariazaratemobile.R;

public class DetalleDeInmuebleFragment extends Fragment {

    private DetalleDeInmuebleViewModel mViewModel;

    public static DetalleDeInmuebleFragment newInstance() {
        return new DetalleDeInmuebleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_de_inmueble, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleDeInmuebleViewModel.class);
        // TODO: Use the ViewModel
    }

}