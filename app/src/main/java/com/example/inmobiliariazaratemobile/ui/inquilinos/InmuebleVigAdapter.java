package com.example.inmobiliariazaratemobile.ui.inquilinos;

import static android.support.v4.media.MediaBrowserCompatApi23.getItem;
import static com.example.inmobiliariazaratemobile.ui.inmuebles.InmueblesAdapter.DIFF;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliariazaratemobile.databinding.ItemInmuebleBinding;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;

public class InmuebleVigAdapter ListAdapter<InmuebleVigModel, InmuebleVigVH>{
    public interface OnClick { void onClick(InmuebleModel i); }
    private final OnClick onClick;
    public InmuebleVigAdapter(OnClick oc){ super(DIFF); this.onClick = oc; }

    static final DiffUtil.ItemCallback<InmuebleModel> DIFF = new DiffUtil.ItemCallback<InmuebleModel>() {
        public boolean areItemsTheSame(InmuebleModel a, InmuebleModel b){ return a.getIdInmueble()==b.getIdInmueble(); }
        public boolean areContentsTheSame(InmuebleModel a, InmuebleModel b){ return a.equals(b); } // o compara campos clave
    };

    @NonNull @Override public InmuebleVH onCreateViewHolder(@NonNull ViewGroup p, int v){
        ItemInmuebleBinding b = ItemInmuebleBinding.inflate(LayoutInflater.from(p.getContext()), p, false);
        return new InmuebleVH(b);
    }
    @Override public void onBindViewHolder(@NonNull InmuebleVH h, int pos){
        InmuebleModel i = getItem(pos);
        h.bind(i);
        h.itemView.setOnClickListener(v -> onClick.onClick(i));
    }
}
class InmuebleVH extends RecyclerView.ViewHolder {
    private final ItemInmuebleBinding b;
    InmuebleVH(ItemInmuebleBinding b){ super(b.getRoot()); this.b = b; }
    void bind(InmuebleModel i){
        b.tvDireccion.setText(i.getDireccion());
        b.tvTipoUso.setText(i.getTipo()+" • "+i.getUso());
        b.tvPrecio.setText(String.format("$ %.2f", i.getValor()));
    }
}
