package com.weektech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProjetoAdapter extends RecyclerView.Adapter<ProjetoAdapter.VH> {

    private List<Projeto> projetos = new ArrayList<>();

    public void setProjetos(List<Projeto> lista) {
        this.projetos = lista != null ? lista : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_projeto, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Projeto p = projetos.get(position);
        holder.tvNomeProjeto.setText(p.nomeProjeto);
        holder.tvNome.setText(p.nome + "  ·  RA: " + p.ra);
        holder.tvDescricao.setText(p.descricao);
    }

    @Override
    public int getItemCount() { return projetos.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNomeProjeto, tvNome, tvDescricao;
        VH(@NonNull View v) {
            super(v);
            tvNomeProjeto = v.findViewById(R.id.tvNomeProjeto);
            tvNome        = v.findViewById(R.id.tvNomeAluno);
            tvDescricao   = v.findViewById(R.id.tvDescricaoProjeto);
        }
    }
}
