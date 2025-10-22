package com.example.inmobiliariazaratemobile.ui.inmuebles;

 import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
 import com.example.inmobiliariazaratemobile.R;
 import com.example.inmobiliariazaratemobile.databinding.FragmentInmueblesBinding;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;
import com.example.inmobiliariazaratemobile.databinding.FragmentDetalleInmuebleBinding;
public class InmuebleDetalleFragment extends Fragment {

    private InmuebleDetalleViewModel mViewModel;
    private FragmentDetalleInmuebleBinding binding;

    public static InmuebleDetalleFragment newInstance() {
        return new InmuebleDetalleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(InmuebleDetalleViewModel.class);

        InmuebleModel arg = (InmuebleModel) getArguments().getSerializable("inmueble");
        if(arg != null) mViewModel.init(arg);

        mViewModel.inmueble.observe(getViewLifecycleOwner(), i -> {
            binding.tvIdInmueble.setText(String.valueOf(i.getIdInmueble()));
            binding.tvDireccionI.setText(i.getDireccion());
            binding.tvUsoI.setText(i.getUso());
            binding.tvAmbientesI.setText(String.valueOf(i.getAmbientes()));
            binding.tvLatitudI.setText(String.valueOf(i.getLatitud()));
            binding.tvLongitudI.setText(String.valueOf(i.getLongitud()));
            binding.tvValorI.setText(String.valueOf(i.getValor()));
            Glide.with(this)
                    .load(ApiClient.BASE_URL + i.getImagen())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(binding.imgInmueble);
            binding.checkDisponible.setChecked(i.isDisponible());
        });

        binding.checkDisponible.setOnCheckedChangeListener((btn, checked) ->
                mViewModel.actualizarDisponible(checked)
        );

        mViewModel.status.observe(getViewLifecycleOwner(), s -> {
            // feedback simple sin lógica de negocio
            binding.checkDisponible.setEnabled(s != InmuebleDetalleViewModel.Status.SAVING);
        });

        mViewModel.obtenerInmueble(getArguments());
        return binding.getRoot();

        // evento UI -> VM
        binding.checkDisponible.setOnCheckedChangeListener((btn, checked) -> {
            mViewModel.toggleDisponible(checked);
        });

// feedback mínimo de estado (habilitar/deshabilitar el switch mientras guarda)
        mViewModel.status.observe(getViewLifecycleOwner(), s -> {
            binding.checkDisponible.setEnabled(s != InmuebleDetalleViewModel.Status.SAVING);
        });



}}