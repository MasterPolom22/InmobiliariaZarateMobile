package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.inmobiliariazaratemobile.databinding.FragmentInmuebleAltaBinding;

public class InmuebleAltaFragment extends Fragment {

    private FragmentInmuebleAltaBinding b;
    private InmuebleAltaViewModel vm;
    private ActivityResultLauncher<String> pickImage;

    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentInmuebleAltaBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InmuebleAltaViewModel.class);

        pickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) vm.onImagenSeleccionada(uri);
        });

        vm.imagenUri.observe(getViewLifecycleOwner(), uri -> {
            Glide.with(this)
                    .load(uri)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(b.imgPreview);
        });

        vm.status.observe(getViewLifecycleOwner(), s -> {
            boolean loading = s == InmuebleAltaViewModel.Status.LOADING;
            b.btnGuardar.setEnabled(!loading);
            if (s == InmuebleAltaViewModel.Status.SUCCESS) {
                NavHostFragment.findNavController(this).popBackStack();
            }
        });

        b.btnSeleccionar.setOnClickListener(v -> pickImage.launch("image/*"));
        b.btnGuardar.setOnClickListener(v ->
                vm.guardar(
                        String.valueOf(b.etDireccion.getText()),
                        String.valueOf(b.etTipo.getText()),
                        String.valueOf(b.etUso.getText()),
                        String.valueOf(b.etAmbientes.getText()),
                        String.valueOf(b.etSuperficie.getText()),
                        String.valueOf(b.etLatitud.getText()),
                        String.valueOf(b.etLongitud.getText()),
                        String.valueOf(b.etValor.getText())
                )
        );

        return b.getRoot();
    }
}
