package com.example.inmobiliariazaratemobile.ui.inmuebles;




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
import android.widget.TextView;
import com.example.inmobiliariazaratemobile.databinding.FragmentInmueblesBinding;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;

import java.util.List;

public class InmueblesFragment extends Fragment {
    private FragmentInmueblesBinding binding;
    private InmueblesViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(InmueblesViewModel.class);
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);

        vm.getmInmueble().observe(getViewLifecycleOwner(), new Observer<List<InmuebleModel>>() {
            @Override
            public void onChanged(List<InmuebleModel> inmuebleModels) {
                InmuebleAdapter adapter = new InmuebleAdapter(inmuebleModels, getContext());
                GridLayoutManager glm = new GridLayoutManager(getContext(),2);
                RecyclerView rv = binding.rvInmuebles;

                rv.setAdapter(adapter);
                rv.setLayoutManager(glm);



            }
        });

        return binding.getRoot();
    }

}