
package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.inmobiliariazaratemobile.databinding.FragmentDetalleInmuebleBinding;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;

public class InmuebleDetalleFragment extends Fragment {

    private InmuebleDetalleViewModel vm;
    private FragmentDetalleInmuebleBinding b;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InmuebleDetalleViewModel.class);

        InmuebleModel arg = (InmuebleModel) getArguments().getSerializable("inmueble");
        if(arg != null) vm.init(arg);

        vm.inmueble.observe(getViewLifecycleOwner(), i -> {
            if(i==null) return;
            b.tvIdInmueble.setText(String.valueOf(i.getIdInmueble()));
            b.tvDireccionI.setText(i.getDireccion());
            b.tvUsoI.setText(i.getUso());
            b.tvAmbientesI.setText(String.valueOf(i.getAmbientes()));
            b.tvLatitudI.setText(String.valueOf(i.getLatitud()));
            b.tvLongitudI.setText(String.valueOf(i.getLongitud()));
            b.tvValorI.setText(String.valueOf(i.getPrecio()));
            Glide.with(this)
                    .load(ApiClient.BASE_URL + i.getImagen())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(b.imgInmueble);

            // evitar disparar el listener por set programático
            b.checkDisponible.setOnCheckedChangeListener(null);
            b.checkDisponible.setChecked(i.isDisponible());
            b.checkDisponible.setOnCheckedChangeListener((btn, isChecked) -> {
                if (btn.isPressed()) vm.guardarDisponibilidad(isChecked);
            });
        });

        vm.status.observe(getViewLifecycleOwner(), s -> {
            boolean saving = (s == InmuebleDetalleViewModel.Status.SAVING);
            b.checkDisponible.setEnabled(!saving);
        });

        return b.getRoot();
    }
}
