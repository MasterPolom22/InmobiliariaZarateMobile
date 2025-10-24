package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.view.*;
import android.os.Bundle;
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

        vm.imagenUri.observe(getViewLifecycleOwner(), uri ->
                Glide.with(this).load(uri)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.stat_notify_error)
                        .into(b.imgPreview)
        );

        vm.pickImageEvent.observe(getViewLifecycleOwner(), fire -> {
            if (Boolean.TRUE.equals(fire)) pickImage.launch("image/*");
        });

        vm.status.observe(getViewLifecycleOwner(), s -> {
            boolean loading = s == InmuebleAltaViewModel.Status.LOADING;
            b.btnGuardar.setEnabled(!loading);
            if (s == InmuebleAltaViewModel.Status.SUCCESS) {
                NavHostFragment.findNavController(this).popBackStack();
            }
        });

        vm.msg.observe(getViewLifecycleOwner(), m -> {
            if (m != null && !m.isEmpty())
                android.widget.Toast.makeText(requireContext(), m, android.widget.Toast.LENGTH_SHORT).show();
        });

        b.btnSeleccionar.setOnClickListener(v -> vm.onSeleccionarImagenClick());
        b.btnGuardar.setOnClickListener(v -> vm.onGuardarClick(
                sval(b.etDireccion), sval(b.etTipo), sval(b.etUso),
                sval(b.etAmbientes), sval(b.etSuperficie),
                sval(b.etLatitud), sval(b.etLongitud), sval(b.etValor)
        ));

        return b.getRoot();
    }

    private String sval(android.widget.TextView t){ return String.valueOf(t.getText()); }
}
