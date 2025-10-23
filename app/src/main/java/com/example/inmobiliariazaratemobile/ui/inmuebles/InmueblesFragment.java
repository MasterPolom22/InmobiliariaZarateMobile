package com.example.inmobiliariazaratemobile.ui.inmuebles;




import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliariazaratemobile.R;
import com.example.inmobiliariazaratemobile.databinding.FragmentInmueblesBinding;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;

import java.util.List;

public class InmueblesFragment extends Fragment {
    private FragmentInmueblesBinding b;
    private InmueblesViewModel vm;
    private InmueblesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentInmueblesBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(InmueblesViewModel.class);
        adapter = new InmueblesAdapter();
        b.rvInmuebles.setAdapter(adapter);

        adapter.setOnItemClick(item -> {
            Bundle args = new Bundle();
            args.putSerializable("inmueble", item);
            androidx.navigation.fragment.NavHostFragment
                    .findNavController(this)
                    .navigate(R.id.action_nav_inmuebles_to_inmuebleDetalleFragment, args);
        });
        b.btnNuevoInmueble.setOnClickListener(v ->
                androidx.navigation.fragment.NavHostFragment.findNavController(this)
                        .navigate(R.id.inmuebleAltaFragment)
        );



        vm.inmuebles.observe(getViewLifecycleOwner(), adapter::submitList);
        vm.cargar();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (vm != null) vm.cargar(); // vuelve a pedir /api/Inmuebles
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }

}