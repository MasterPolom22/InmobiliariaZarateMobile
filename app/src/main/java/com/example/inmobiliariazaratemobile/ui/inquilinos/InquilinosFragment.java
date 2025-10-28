package com.example.inmobiliariazaratemobile.ui.inquilinos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.inmobiliariazaratemobile.R;
import com.example.inmobiliariazaratemobile.databinding.FragmentInmueblesBinding;
import com.example.inmobiliariazaratemobile.databinding.FragmentInquilinosBinding;
import com.example.inmobiliariazaratemobile.ui.inmuebles.InmueblesViewModel;

public class InquilinosFragment extends Fragment {

    private FragmentInquilinosBinding b;
    private InquilinosViewModel vm;
    private InmuebleVigAdapter adapter;

    @Override public View onCreateView(LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentInquilinosBinding.inflate(inf, c, false);
        vm = new ViewModelProvider(this).get(InquilinosViewModel.class);

        adapter = new InmuebleVigAdapter(item -> {
            // Navega al detalle del inquilino del inmueble seleccionado
            // Pasa idInmueble o el objeto completo (Serializable)
            Bundle args = new Bundle();
            args.putInt("inmuebleId", item.getIdInmueble());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_inquilinos_to_inquilinoDetalle, args);
        });
        b.rv.setAdapter(adapter);
        b.swipe.setOnRefreshListener(vm::cargar);

        vm.items.observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            b.swipe.setRefreshing(false);
            b.empty.setVisibility(list==null || list.isEmpty()? View.VISIBLE: View.GONE);
        });
        vm.status.observe(getViewLifecycleOwner(), st -> b.progress.setVisibility(st==InquilinosViewModel.Status.LOADING? View.VISIBLE: View.GONE));

        vm.cargar();
        return b.getRoot();
    }

}