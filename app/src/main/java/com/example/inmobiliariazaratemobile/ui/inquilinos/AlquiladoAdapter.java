package com.example.inmobiliariazaratemobile.ui.inquilinos;

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
        ItemInquilinoBinding b = ItemInquilinoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        InmuebleModel it = getItem(pos);
        h.b.tvDireccion.setText(it.getDireccion());
        h.b.tvTipo.setText(it.getTipo());
        h.b.tvValor.setText(String.valueOf(it.getPrecio()));
        h.itemView.setOnClickListener(v -> onClick.onItem(it));
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemInquilinoBinding b;
        VH(ItemInquilinoBinding b){ super(b.getRoot()); this.b = b; }
    }
}