package com.example.inmobiliariazaratemobile.ui.inmuebles;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inmobiliariazaratemobile.R;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.*;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.VH> {
    private final List<InmuebleModel> data = new ArrayList<>();

    public void submit(List<InmuebleModel> items){
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_inmueble, p, false);
        return new VH(view);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        InmuebleModel it = data.get(pos);
        h.lblDir.setText(it.getDireccion());
        h.lblTipoUso.setText(it.getTipo() + " · " + it.getUso());
        h.lblValor.setText("$ " + (long) it.getValor());
        h.switchDisponible.setChecked(it.isDisponible());
        h.switchDisponible.setEnabled(false);
        h.img.setImageResource(R.mipmap.ic_launcher_round);
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img; TextView lblDir, lblTipoUso, lblValor; SwitchMaterial switchDisponible;
        VH(@NonNull View v){
            super(v);
            img = v.findViewById(R.id.img);
            lblDir = v.findViewById(R.id.lblDir);
            lblTipoUso = v.findViewById(R.id.lblTipoUso);
            lblValor = v.findViewById(R.id.lblValor);
            switchDisponible = v.findViewById(R.id.switchDisponible);
        }
    }
}