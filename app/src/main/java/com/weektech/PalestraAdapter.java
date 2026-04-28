// adapter/PalestraAdapter.java
package com.weektech.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weektech.R;
import com.weektech.model.Palestra;

import java.util.List;

/**
 * Adapter responsável por ligar os dados (Palestra)
 * com a RecyclerView (lista na tela)
 */
public class PalestraAdapter extends RecyclerView.Adapter<PalestraAdapter.PalestraViewHolder> {

    // Lista de palestras exibidas
    private List<Palestra> palestras;

    /**
     * Interface para capturar eventos de clique
     * (usada pela Activity)
     */
    public interface OnItemClickListener {
        void onInscricaoClick(Palestra palestra); // botão inscrever
        void onItemClick(Palestra palestra);      // clique no card
    }

    private OnItemClickListener listener;

    /**
     * Construtor do Adapter
     */
    public PalestraAdapter(List<Palestra> palestras, OnItemClickListener listener) {
        this.palestras = palestras;
        this.listener  = listener;
    }

    /**
     * Atualiza a lista de palestras e recarrega a tela
     */
    public void setPalestras(List<Palestra> palestras) {
        this.palestras = palestras;
        notifyDataSetChanged(); // atualiza RecyclerView
    }

    /**
     * Cria a estrutura do item (layout XML)
     */
    @NonNull
    @Override
    public PalestraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_palestra, parent, false);
        return new PalestraViewHolder(view);
    }

    /**
     * Liga os dados da palestra com os componentes da tela
     */
    @Override
    public void onBindViewHolder(@NonNull PalestraViewHolder holder, int position) {

        // Pega a palestra da posição atual
        Palestra p = palestras.get(position);

        // Preenche os campos do layout
        holder.tvTitulo.setText(p.titulo);
        holder.tvPalestrante.setText(p.palestrante);
        holder.tvHorario.setText(p.horario);
        holder.tvLocal.setText(p.local);

        // Evento de clique no botão "Inscrever-se"
        holder.btnInscrever.setOnClickListener(v ->
                listener.onInscricaoClick(p)
        );

        // Evento de clique no card inteiro
        holder.itemView.setOnClickListener(v ->
                listener.onItemClick(p)
        );
    }

    /**
     * Retorna quantidade de itens da lista
     */
    @Override
    public int getItemCount() {
        return palestras != null ? palestras.size() : 0;
    }

    /**
     * ViewHolder: representa cada item da lista
     */
    static class PalestraViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo, tvPalestrante, tvHorario, tvLocal;
        Button btnInscrever;

        /**
         * Construtor que conecta os elementos do XML
         */
        PalestraViewHolder(View itemView) {
            super(itemView);

            tvTitulo      = itemView.findViewById(R.id.tvTitulo);
            tvPalestrante = itemView.findViewById(R.id.tvPalestrante);
            tvHorario     = itemView.findViewById(R.id.tvHorario);
            tvLocal       = itemView.findViewById(R.id.tvLocal);
            btnInscrever  = itemView.findViewById(R.id.btnInscrever);
        }
    }
}
