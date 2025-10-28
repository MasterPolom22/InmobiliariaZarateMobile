package com.example.inmobiliariazaratemobile.ui.inmuebles;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliariazaratemobile.databinding.ItemInmuebleBinding;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.example.inmobiliariazaratemobile.request.ApiClient;

public class InmueblesAdapter extends ListAdapter<InmuebleModel, InmueblesAdapter.VH> {

    public interface OnItemClick { void onClick(InmuebleModel item); }
    private OnItemClick onItemClick;
    public void setOnItemClick(OnItemClick l){ this.onItemClick = l; }

    public InmueblesAdapter() {
        super(DIFF);
    }

    private static final DiffUtil.ItemCallback<InmuebleModel> DIFF =
            new DiffUtil.ItemCallback<InmuebleModel>() {
                @Override public boolean areItemsTheSame(@NonNull InmuebleModel a, @NonNull InmuebleModel b) {
                    return a.getIdInmueble() == b.getIdInmueble();
                }
                @Override public boolean areContentsTheSame(@NonNull InmuebleModel a, @NonNull InmuebleModel b) {
                    return a.getDireccion().equals(b.getDireccion())
                            && a.getPrecio() == b.getPrecio()
                            && a.isDisponible() == b.isDisponible();
                }
            };

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInmuebleBinding b = ItemInmuebleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        InmuebleModel it = getItem(pos);
        h.txtDireccion.setText(it.getDireccion());
        h.txtTipoUso.setText(it.getTipo() + " • " + it.getUso() + " • " + it.getAmbientes() + " amb");
        h.txtValor.setText("$ " + String.format(java.util.Locale.US,"%.2f", it.getPrecio()));

        String url = it.getImagen();
        Glide.with(h.img.getContext())
                .load(ApiClient.BASE_URL + it.getImagen())
                .placeholder(android.R.drawable.ic_menu_gallery) // antes: R.drawable.ic_home_24
                .error(android.R.drawable.stat_notify_error)
                .into(h.img);

        h.itemView.setOnClickListener(v -> { if(onItemClick!=null) onItemClick.onClick(it); });
    }

    static class VH extends RecyclerView.ViewHolder {
        final ImageView img;
        final TextView txtDireccion, txtTipoUso, txtValor;
        VH(ItemInmuebleBinding b){
            super(b.getRoot());
            img = b.img;
            txtDireccion = b.txtDireccion;
            txtTipoUso = b.txtTipoUso;
            txtValor = b.txtValor;
        }
    }
}