package com.example.inmobiliariazaratemobile.ui.inquilinos;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.inmobiliariazaratemobile.databinding.FragmentInquilinosBinding;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;

public class InquilinosFragment extends Fragment {

    private FragmentInquilinosBinding b;
    private InquilinosViewModel vm;
    private AlquiladoAdapter adapter;

    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentInquilinosBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InquilinosViewModel.class);

        adapter = new AlquiladoAdapter(item -> {
            Bundle args = new Bundle();
            args.putInt("inmuebleId", item.getIdInmueble());
            NavHostFragment.findNavController(this).navigate(
                    com.example.inmobiliariazaratemobile.R.id.action_nav_inquilinos_to_inquilinoDetalleFragment, args
            );
        });

        b.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.recycler.setAdapter(adapter);

        vm.items.observe(getViewLifecycleOwner(), adapter::submitList);
        vm.status.observe(getViewLifecycleOwner(), s -> {
            b.progress.setVisibility(s == InquilinosViewModel.Status.LOADING ? View.VISIBLE : View.GONE);
            b.empty.setVisibility((s == InquilinosViewModel.Status.OK && (adapter.getItemCount()==0)) ? View.VISIBLE : View.GONE);
            b.error.setVisibility(s == InquilinosViewModel.Status.ERROR ? View.VISIBLE : View.GONE);
        });

        if (savedInstanceState == null) vm.cargar();
        return b.getRoot();
    }
}
