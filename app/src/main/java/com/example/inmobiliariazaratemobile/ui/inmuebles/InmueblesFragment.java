package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.inmobiliariazaratemobile.databinding.FragmentInmueblesBinding;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;

import java.util.ArrayList;
import java.util.List;

public class InmueblesFragment extends Fragment {

    private FragmentInmueblesBinding b;
    private InmueblesViewModel vm;
    private InmuebleAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentInmueblesBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(InmueblesViewModel.class);

        b.rvInmuebles.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adapter = new InmuebleAdapter(new ArrayList<>(), vm::onInmuebleClick);
        b.rvInmuebles.setAdapter(adapter);

        vm.getmInmueble().observe(getViewLifecycleOwner(), this::renderLista);

        vm.cargarInmuebles();
    }

    private void renderLista(List<InmuebleModel> lista) {
        adapter.setItems(lista);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}
