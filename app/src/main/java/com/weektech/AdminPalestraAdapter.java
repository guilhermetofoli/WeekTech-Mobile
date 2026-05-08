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

    // interface pras acoes do admin
    public interface OnAdminActionListener {
        void onToggleAtiva(Palestra palestra);
        void onDeletePalestra(Palestra palestra);
        void onEditPalestra(Palestra palestra);
    }

    public AdminPalestraAdapter(OnAdminActionListener listener) {
        this.listener = listener;
    }

    // atualiza a lista toda
    public void setPalestras(List<Palestra> palestras) {
        this.palestras = palestras;
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

        // cliques nos botoes de editar e excluir
        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) listener.onEditPalestra(palestra);
        });

        holder.btnExcluir.setOnClickListener(v -> {
            if (listener != null) listener.onDeletePalestra(palestra);
        });
    }

    @Override
    public int getItemCount() {
        return palestras != null ? palestras.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, palestrante;
        ImageButton btnEditar, btnExcluir;

        ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTituloPalestra);
            palestrante = itemView.findViewById(R.id.txtPalestrante);
            btnEditar = itemView.findViewById(R.id.btnEditarPalestra);
            btnExcluir = itemView.findViewById(R.id.btnExcluirPalestra);
        }
    }
}
