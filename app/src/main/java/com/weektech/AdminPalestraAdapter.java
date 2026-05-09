package com.weektech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminPalestraAdapter extends RecyclerView.Adapter<AdminPalestraAdapter.ViewHolder> {

    private List<Palestra> palestras = new ArrayList<>();
    private Context context;
    private OnAdminActionListener listener;

    public interface OnAdminActionListener {
        void onToggleAtiva(Palestra palestra);
        void onDeletePalestra(Palestra palestra);
        void onEditPalestra(Palestra palestra);
    }

    public AdminPalestraAdapter(OnAdminActionListener listener) {
        this.listener = listener;
    }

    public void setPalestras(List<Palestra> palestras) {
        this.palestras = palestras != null ? palestras : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_admin_palestra, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Palestra palestra = palestras.get(position);

        holder.titulo.setText(palestra.titulo);
        holder.palestrante.setText(palestra.palestrante);
        holder.data.setText(palestra.data != null ? palestra.data : "Data não definida");

        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) listener.onEditPalestra(palestra);
        });

        holder.btnExcluir.setOnClickListener(v -> {
            if (listener != null) listener.onDeletePalestra(palestra);
        });
    }

    @Override
    public int getItemCount() {
        return palestras.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, palestrante, data;
        ImageButton btnEditar, btnExcluir;

        ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTituloPalestra);
            palestrante = itemView.findViewById(R.id.txtPalestrante);
            data = itemView.findViewById(R.id.txtDataPalestra);
            btnEditar = itemView.findViewById(R.id.btnEditarPalestra);
            btnExcluir = itemView.findViewById(R.id.btnExcluirPalestra);
        }
    }
}
