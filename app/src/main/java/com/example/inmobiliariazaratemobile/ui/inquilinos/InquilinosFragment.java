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

import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inmobiliariazaratemobile.databinding.ItemInquilinoBinding;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;

public class AlquiladoAdapter extends ListAdapter<InmuebleModel, AlquiladoAdapter.VH> {

    public interface OnClick { void onItem(InmuebleModel item); }
    private final OnClick onClick;

    public AlquiladoAdapter(OnClick onClick) {
        super(DIFF);
        this.onClick = onClick;
    }

    private static final DiffUtil.ItemCallback<InmuebleModel> DIFF =
            new DiffUtil.ItemCallback<InmuebleModel>() {
                @Override public boolean areItemsTheSame(@NonNull InmuebleModel a, @NonNull InmuebleModel b) {
                    return a.getIdInmueble()==b.getIdInmueble();
                }
                @Override public boolean areContentsTheSame(@NonNull InmuebleModel a, @NonNull InmuebleModel b) {
                    return a.getDireccion().equals(b.getDireccion())
                            && a.getTipo().equals(b.getTipo())
                            && a.getPrecio()==b.getPrecio();
                }
            };

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInquiladoBinding b = ItemInquiladoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        InmuebleModel it = getItem(pos);
        h.b.tvDireccion.setText(it.getDireccion());
        h.b.tvTipo.setText(it.getTipo());
        h.b.tvValor.setText(String.valueOf(it.getValor()));
        h.itemView.setOnClickListener(v -> onClick.onItem(it));
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemInquiladoBinding b;
        VH(ItemInquiladoBinding b){ super(b.getRoot()); this.b = b; }
    }
}