package com.weektech;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProjetoAdapter extends RecyclerView.Adapter<ProjetoAdapter.VH> {

    private List<Projeto> projetos = new ArrayList<>();
    private OnProjetoClickListener listener;

    public interface OnProjetoClickListener {
        void onDeleteClick(Projeto projeto);
    }

    public ProjetoAdapter(OnProjetoClickListener listener) {
        this.listener = listener;
    }

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
        
        // Configura o badge de status
        holder.tvStatusBadge.setText(p.status);
        if ("APROVADO".equals(p.status)) {
            holder.tvStatusBadge.setBackgroundColor(Color.parseColor("#28A745"));
            holder.tvStatusBadge.setTextColor(Color.WHITE);
            holder.btnExcluir.setVisibility(View.GONE);
        } else if ("REPROVADO".equals(p.status)) {
            holder.tvStatusBadge.setBackgroundColor(Color.parseColor("#D32F2F"));
            holder.tvStatusBadge.setTextColor(Color.WHITE);
            // Se reprovado, permite excluir para tentar de novo
            holder.btnExcluir.setVisibility(View.VISIBLE);
        } else {
            // PENDENTE
            holder.tvStatusBadge.setBackgroundColor(Color.parseColor("#E1E4E8"));
            holder.tvStatusBadge.setTextColor(Color.parseColor("#6B7C93"));
            holder.btnExcluir.setVisibility(View.GONE);
        }

        // Agendamento
        if (p.dataApresentacao != null && !p.dataApresentacao.isEmpty() && "APROVADO".equals(p.status)) {
            holder.layoutAgendamento.setVisibility(View.VISIBLE);
            holder.tvAgendamentoInfo.setText("Apresentação: " + p.dataApresentacao + 
                " de " + p.horaInicioApresentacao + " até " + p.horaFimApresentacao);
        } else {
            holder.layoutAgendamento.setVisibility(View.GONE);
        }

        holder.btnExcluir.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(p);
        });
    }

    @Override
    public int getItemCount() { return projetos.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNomeProjeto, tvNome, tvDescricao, tvAgendamentoInfo, tvStatusBadge;
        View layoutAgendamento;
        Button btnExcluir;
        
        VH(@NonNull View v) {
            super(v);
            tvNomeProjeto = v.findViewById(R.id.tvNomeProjeto);
            tvNome        = v.findViewById(R.id.tvNomeAluno);
            tvDescricao   = v.findViewById(R.id.tvDescricaoProjeto);
            tvAgendamentoInfo = v.findViewById(R.id.tvAgendamentoInfo);
            layoutAgendamento = v.findViewById(R.id.layoutAgendamento);
            tvStatusBadge = v.findViewById(R.id.tvStatusBadge);
            btnExcluir = v.findViewById(R.id.btnExcluirProjeto);
        }
    }
}
