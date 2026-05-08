package com.weektech;

import android.content.Context;
import android.content.res.ColorStateList;
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

public class PalestraAdapter extends RecyclerView.Adapter<PalestraAdapter.ViewHolder> {
    private List<Palestra> palestras = new ArrayList<>();
    private final Context context;
    private boolean isAdmin = false;

    public interface OnPalestraClickListener {
        void onInscreverClick(Palestra palestra, int position);
        void onInscritoClick(Palestra palestra, int position);
        void onVisualizarClick(Palestra palestra, int position);
        void onCardClick(Palestra palestra, int position);
        void onVisualizarPresencasClick(Palestra palestra);
    }

    private OnPalestraClickListener listener;

    public PalestraAdapter(Context context, OnPalestraClickListener listener) {
        this.context  = context;
        this.listener = listener;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        notifyDataSetChanged();
    }

    public void setPalestras(List<Palestra> novaLista) {
        this.palestras = novaLista != null ? novaLista : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void atualizarStatus(int position, String novoStatus) {
        if (position >= 0 && position < palestras.size()) {
            palestras.get(position).statusInscricao = novoStatus;
            notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_palestra_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Palestra palestra = palestras.get(position);

        holder.tvHoraInicio.setText(palestra.horaInicio);
        holder.tvHoraFim.setText(palestra.horaFim);
        holder.tvTitulo.setText(palestra.titulo);
        holder.tvPalestrante.setText("Palestrante: " + palestra.palestrante);
        holder.tvLocal.setText(palestra.local);
        
        // Exibir descrição se disponível para "detalhamento completo"
        if (palestra.descricao != null && !palestra.descricao.isEmpty()) {
            holder.tvDescricao.setVisibility(View.VISIBLE);
            holder.tvDescricao.setText(palestra.descricao);
        } else {
            holder.tvDescricao.setVisibility(View.GONE);
        }

        if (isAdmin) {
            holder.btnAcao.setText("Visualizar Presenças");
            holder.btnAcao.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B3A6B")));
            holder.btnAcao.setTextColor(Color.WHITE);
            holder.btnAcao.setOnClickListener(v -> {
                if (listener != null) listener.onVisualizarPresencasClick(palestra);
            });
        } else {
            configurarBotao(holder.btnAcao, palestra.statusInscricao);
            holder.btnAcao.setOnClickListener(v -> {
                if (listener == null) return;
                switch (palestra.statusInscricao) {
                    case Palestra.StatusInscricao.DISPONIVEL:
                        listener.onInscreverClick(palestra, holder.getAdapterPosition());
                        break;
                    case Palestra.StatusInscricao.INSCRITO:
                        listener.onInscritoClick(palestra, holder.getAdapterPosition());
                        break;
                    case Palestra.StatusInscricao.VISUALIZAR:
                        listener.onVisualizarClick(palestra, holder.getAdapterPosition());
                        break;
                }
            });
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                if (isAdmin) {
                    listener.onVisualizarPresencasClick(palestra);
                } else {
                    listener.onCardClick(palestra, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return palestras.size();
    }

    private void configurarBotao(Button btn, String status) {
        switch (status) {
            case Palestra.StatusInscricao.DISPONIVEL:
                btn.setText("Inscrever-se");
                btn.setTextColor(Color.WHITE);
                btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B3A6B")));
                break;
            case Palestra.StatusInscricao.INSCRITO:
                btn.setText("Inscrito");
                btn.setTextColor(Color.parseColor("#28A745"));
                btn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                break;
            case Palestra.StatusInscricao.VISUALIZAR:
            default:
                btn.setText("Visualizar");
                btn.setTextColor(Color.parseColor("#1B3A6B"));
                btn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                break;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoraInicio, tvHoraFim, tvTitulo, tvPalestrante, tvLocal, tvDescricao;
        Button btnAcao;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoraInicio  = itemView.findViewById(R.id.tvHoraInicio);
            tvHoraFim     = itemView.findViewById(R.id.tvHoraFim);
            tvTitulo      = itemView.findViewById(R.id.tvTitulo);
            tvPalestrante = itemView.findViewById(R.id.tvPalestrante);
            tvLocal       = itemView.findViewById(R.id.tvLocal);
            tvDescricao   = itemView.findViewById(R.id.tvDescricao); // Novo campo
            btnAcao       = itemView.findViewById(R.id.btnAcao);
        }
    }
}
