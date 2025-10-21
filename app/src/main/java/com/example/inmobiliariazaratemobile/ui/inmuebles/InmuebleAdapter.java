package com.example.inmobiliariazaratemobile.ui.inmuebles;


import static com.example.inmobiliariazaratemobile.request.ApiClient.BASE_URL;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliariazaratemobile.R;
import com.example.inmobiliariazaratemobile.databinding.ItemInmuebleBinding;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;

import java.util.ArrayList;
import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.VH>{
    public interface OnInmuebleClick { void onClick(InmuebleModel item); }

    private final List<InmuebleModel> items = new ArrayList<>();
    private final OnInmuebleClick onClick;

    public InmuebleAdapter(List<InmuebleModel> lista, OnInmuebleClick onClick) {
        if (lista != null) items.addAll(lista);
        this.onClick = onClick;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInmuebleBinding b = ItemInmuebleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        InmuebleModel i = items.get(position);
        h.bind(i, onClick);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<InmuebleModel> nuevos) {
        items.clear();
        if (nuevos != null) items.addAll(nuevos);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        private final ItemInmuebleBinding b;

        VH(ItemInmuebleBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(InmuebleModel i, OnInmuebleClick onClick) {
            b.tvDireccion.setText(i.getDireccion());
            b.tvTipoUso.setText(i.getTipo());
            b.tvValor.setText(String.valueOf(i.getValor()));

            Glide.with(b.getRoot())
                    .load(BASE_URL + i.getImagen())
                    .placeholder(R.drawable.ima)
                    .error(R.drawable.ima)
                    .into(b.imgInmueble);

            b.idCard.setOnClickListener(v -> onClick.onClick(i));
        }
    }
}
