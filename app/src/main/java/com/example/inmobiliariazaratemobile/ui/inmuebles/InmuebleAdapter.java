package com.example.inmobiliariazaratemobile.ui.inmuebles;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliariazaratemobile.R;
import com.example.inmobiliariazaratemobile.model.InmuebleModel;

import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder>{
    private List<InmuebleModel> lista;
    private Context  context;



    private InmuebleAdapter(List<InmuebleModel> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }




    @NonNull
    @Override
    public InmuebleAdapter.InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inmueble, parent, false);
        return new InmuebleViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleAdapter.InmuebleViewHolder holder, int position) {
        String urlBase = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net";
        InmuebleModel i = lista.get(position);
        holder.tvDireccion.setText(i.getDireccion());
        holder.tvTipo.setText(i.getTipo());
        holder.tvPrecio.setText(String.valueOf(i.getValor()));
        Glide.with(context)
                .load(urlBase + i.getImagen())
                .placeholder(R.drawable.ima)
                .error("null")
                .into(holder.imgInmueble);

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }



    public class  InmuebleViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDireccion, tvTipo, tvPrecio;
        private ImageView imgInmueble;
        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);

            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTipo = itemView.findViewById(R.id.tvTipoUso);
            tvPrecio = itemView.findViewById(R.id.tvValor);
            imgInmueble = itemView.findViewById(R.id.imgInmueble);

        }
    }
}
