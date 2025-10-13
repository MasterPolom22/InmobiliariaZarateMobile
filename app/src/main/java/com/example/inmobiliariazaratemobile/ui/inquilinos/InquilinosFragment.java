package com.example.inmobiliariazaratemobile.ui.inquilinos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.inmobiliariazaratemobile.R;
import com.example.inmobiliariazaratemobile.databinding.FragmentInmueblesBinding;
import com.example.inmobiliariazaratemobile.databinding.FragmentInquilinosBinding;
import com.example.inmobiliariazaratemobile.ui.inmuebles.InmueblesViewModel;

public class InquilinosFragment extends Fragment {

    private InquilinosViewModel mViewModel;

    public static InquilinosFragment newInstance() {
        return new InquilinosFragment();
    }

    private FragmentInquilinosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InquilinosViewModel inquilinosViewModel =
                new ViewModelProvider(this).get(InquilinosViewModel.class);

        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textInquilinos;
        inquilinosViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}