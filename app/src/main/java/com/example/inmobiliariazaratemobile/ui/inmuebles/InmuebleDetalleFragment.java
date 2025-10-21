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
import com.example.inmobiliariazaratemobile.databinding.FragmentInmueblesBinding;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;
import com.example.inmobiliariazaratemobile.databinding.FragmentDetalleInmuebleBinding;
public class InmuebleDetalleFragment extends Fragment {


    private FragmentDetalleInmuebleBinding b;
    private InmuebleDetalleViewModel vm;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(InmuebleDetalleViewModel.class);

        // Recibir el inmueble del Bundle
        InmuebleModel arg = (InmuebleModel) getArguments().getSerializable("inmueble");
        if(arg != null) vm.init(arg);

        // Observa el inmueble y pinta
        vm.inmueble.observe(getViewLifecycleOwner(), i -> {
            if(i == null) return;
            b.etDireccion.setText(i.getDireccion());
            b.etTipo.setText(i.getTipo());
            b.etUso.setText(i.getUso());
            b.etAmbientes.setText(String.valueOf(i.getAmbientes()));
            b.etValor.setText(String.valueOf(i.getValor()));
            b.swDisponible.setChecked(i.isDisponible());

            // Imagen
            String raw = i.getImagen();
            String url = (raw == null || raw.isEmpty())
                    ? null
                    : (raw.startsWith("http") ? raw : ApiClient.BASE_URL + raw.replaceFirst("^/",""));
            Glide.with(b.img.getContext())
                    .load(url)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(b.img);
        });

        // Eventos de UI delegados al VM (sin ifs de negocio)
        b.swDisponible.setOnCheckedChangeListener((buttonView, isChecked) -> vm.setDisponible(isChecked));
        b.btnGuardar.setOnClickListener(v -> {
            // Actualiza campos editables al objeto y guarda
            InmuebleModel cur = vm.inmueble.getValue();
            if(cur != null){
                InmuebleModel copy = new InmuebleModel();
                copy.setIdInmueble(cur.getIdInmueble());
                copy.setIdPropietario(cur.getIdPropietario());
                copy.setDuenio(cur.getDuenio());
                copy.setImagen(cur.getImagen());

                copy.setDireccion(String.valueOf(b.etDireccion.getText()));
                copy.setTipo(String.valueOf(b.etTipo.getText()));
                copy.setUso(String.valueOf(b.etUso.getText()));
                try { copy.setAmbientes(Integer.parseInt(String.valueOf(b.etAmbientes.getText()))); } catch (Exception ignored) { copy.setAmbientes(cur.getAmbientes()); }
                try { copy.setValor(Double.parseDouble(String.valueOf(b.etValor.getText()))); } catch (Exception ignored) { copy.setValor(cur.getValor()); }
                copy.setSuperficie(cur.getSuperficie());
                copy.setLatitud(cur.getLatitud());
                copy.setLongitud(cur.getLongitud());
                copy.setDisponible(b.swDisponible.isChecked());

                vm.init(copy);      // refresca LiveData local
                vm.guardarCambios(); // hace el PUT
            }
        });

        // Feedback mínimo de estado
        vm.status.observe(getViewLifecycleOwner(), s -> {
            if(s == InmuebleDetalleViewModel.Status.OK) {
                b.btnGuardar.setEnabled(true);
            } else if (s == InmuebleDetalleViewModel.Status.SAVING) {
                b.btnGuardar.setEnabled(false);
            } else if (s == InmuebleDetalleViewModel.Status.ERROR) {
                b.btnGuardar.setEnabled(true);
            }
        });
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

}