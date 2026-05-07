package com.weektech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.VH> {

    private List<Usuario> usuarios = new ArrayList<>();

    public void setUsuarios(List<Usuario> lista) {
        this.usuarios = lista != null ? lista : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_palestra, parent, false); // Reutilizando layout simples que tem 2 textviews
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Usuario u = usuarios.get(position);
        holder.tvNome.setText(u.nome + (u.coffeeBreak ? " ☕" : ""));
        holder.tvRA.setText("RA: " + u.ra + " | " + u.curso);
    }

    @Override
    public int getItemCount() { return usuarios.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNome, tvRA;
        VH(@NonNull View v) {
            super(v);
            tvNome = v.findViewById(R.id.txtNomeAluno);
            tvRA   = v.findViewById(R.id.txtDataConfirmacao);
        }
    }
}
