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

    // atualiza a lista de projetos quando carregar do banco
    public void setProjetos(List<Projeto> lista) {
        this.projetos = lista != null ? lista : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infla o cardzinho do projeto
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_projeto, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Projeto p = projetos.get(position);
        
        // coloca os textos básicos
        holder.tvNomeProjeto.setText(p.nomeProjeto);
        holder.tvNome.setText(p.nome + "  ·  RA: " + p.ra);
        holder.tvDescricao.setText(p.descricao);
        
        // mostra a data que o cara cadastrou se tiver
        if (p.dataCriacao != null) {
            holder.tvDataCriacao.setText(p.dataCriacao);
            holder.tvDataCriacao.setVisibility(View.VISIBLE);
        } else {
            holder.tvDataCriacao.setVisibility(View.GONE);
        }

        // se o admin ja marcou a apresentacao, mostra os detalhes no card
        if (p.dataApresentacao != null && !p.dataApresentacao.isEmpty()) {
            holder.layoutAgendamento.setVisibility(View.VISIBLE);
            holder.tvAgendamentoInfo.setText("Apresentação: " + p.dataApresentacao + 
                " de " + p.horaInicioApresentacao + " até " + p.horaFimApresentacao);
        } else {
            holder.layoutAgendamento.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return projetos.size(); }

    // guarda as referencias dos componentes do layout
    static class VH extends RecyclerView.ViewHolder {
        TextView tvNomeProjeto, tvNome, tvDescricao, tvDataCriacao, tvAgendamentoInfo;
        View layoutAgendamento;
        
        VH(@NonNull View v) {
            super(v);
            tvNomeProjeto = v.findViewById(R.id.tvNomeProjeto);
            tvNome        = v.findViewById(R.id.tvNomeAluno);
            tvDescricao   = v.findViewById(R.id.tvDescricaoProjeto);
            tvDataCriacao = v.findViewById(R.id.tvDataCriacao);
            tvAgendamentoInfo = v.findViewById(R.id.tvAgendamentoInfo);
            layoutAgendamento = v.findViewById(R.id.layoutAgendamento);
        }
    }
}
