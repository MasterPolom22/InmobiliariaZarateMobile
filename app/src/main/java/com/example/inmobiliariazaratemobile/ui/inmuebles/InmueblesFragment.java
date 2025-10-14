package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.inmobiliariazaratemobile.R;

import com.example.inmobiliariazaratemobile.databinding.FragmentInmueblesBinding;

public class InmueblesFragment extends Fragment {

    private FragmentInmueblesBinding b;
    private InmueblesViewModel vm;
    private InmuebleAdapter adapter = new InmuebleAdapter();

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, @Nullable ViewGroup c, @Nullable Bundle s) {
        b = FragmentInmueblesBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        vm = new ViewModelProvider(this).get(InmueblesViewModel.class);

        b.rvInmuebles.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.rvInmuebles.setAdapter(adapter);

        vm.items.observe(getViewLifecycleOwner(), adapter::submit);

        vm.cargar();
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}