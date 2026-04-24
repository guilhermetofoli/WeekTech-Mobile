package com.weektech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PalestraAdapter extends RecyclerView.Adapter<PalestraAdapter.PalestraViewHolder> {

    private List<Palestra> lista;

    public PalestraAdapter(List<Palestra> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public PalestraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_palestra, parent, false);
        return new PalestraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PalestraViewHolder holder, int position) {
        Palestra p = lista.get(position);
        holder.tvTitulo.setText(p.getTitulo());
        holder.tvPalestrante.setText(p.getPalestrante());
        holder.tvData.setText(p.getData());
        holder.tvLocal.setText(p.getLocal());
        holder.imgPalestrante.setImageResource(p.getFotoResId());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class PalestraViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvPalestrante, tvData, tvLocal;
        ImageView imgPalestrante;

        public PalestraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloPalestra);
            tvPalestrante = itemView.findViewById(R.id.tvNomePalestrante);
            tvData = itemView.findViewById(R.id.tvData);
            tvLocal = itemView.findViewById(R.id.tvLocal);
            imgPalestrante = itemView.findViewById(R.id.imgPalestrante);
        }
    }
}