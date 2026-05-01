
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

import com.weektech.Palestra;

import java.util.ArrayList;
import java.util.List;

public class PalestraAdapter extends RecyclerView.Adapter<PalestraAdapter.ViewHolder> {
    private List<Palestra> palestras = new ArrayList<>();

    private final Context context;

    // Interface de callbacks para a Activity/Fragment
    public interface OnPalestraClickListener {
        void onInscreverClick(Palestra palestra, int position);

        void onInscritoClick(Palestra palestra, int position);

        void onVisualizarClick(Palestra palestra, int position);

        void onCardClick(Palestra palestra, int position);
    }

    private OnPalestraClickListener listener;

    // Construtor
    public PalestraAdapter(Context context, OnPalestraClickListener listener) {
        this.context  = context;
        this.listener = listener;
    }

    // Atualiza a lista e recarrega o RecyclerView
    public void setPalestras(List<Palestra> novaLista) {
        this.palestras = novaLista != null ? novaLista : new ArrayList<>();
        notifyDataSetChanged();
    }

    // Atualiza o status de um card específico sem recarregar tudo
    public void atualizarStatus(int position, String novoStatus) {
        if (position >= 0 && position < palestras.size()) {
            palestras.get(position).statusInscricao = novoStatus;
            notifyItemChanged(position); // anima apenas o item alterado
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do card
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_palestra_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Palestra palestra = palestras.get(position);

        //  Preenche os campos de texto
        holder.tvHoraInicio.setText(palestra.horaInicio);   // "09:00"
        holder.tvHoraFim.setText(palestra.horaFim);         // "10:30"
        holder.tvTitulo.setText(palestra.titulo);
        holder.tvPalestrante.setText(
                "Palestrante: " + palestra.palestrante);
        holder.tvLocal.setText(palestra.local);

        // Configura o botão conforme o status
        configurarBotao(holder.btnAcao, palestra.statusInscricao);

        // Click no botão
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

        // Click no card inteiro → vai para detalhes
        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onCardClick(palestra, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return palestras.size();
    }

    // Método que troca o visual do botão conforme o status
    private void configurarBotao(Button btn, String status) {
        switch (status) {

            // DISPONIVEL: fundo azul sólido
            case Palestra.StatusInscricao.DISPONIVEL:
                btn.setText("Inscrever-se");
                btn.setTextColor(Color.WHITE);
                btn.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#1B3A6B")));
                // Remove borda caso tenha sido definida antes
                if (btn instanceof com.google.android.material.button.MaterialButton) {
                    ((com.google.android.material.button.MaterialButton) btn)
                            .setStrokeWidth(0);
                }
                break;

            // INSCRITO: fundo branco + borda verde + texto verde
            case Palestra.StatusInscricao.INSCRITO:
                btn.setText("Inscrito");
                btn.setTextColor(Color.parseColor("#28A745"));
                btn.setBackgroundTintList(
                        ColorStateList.valueOf(Color.WHITE));
                if (btn instanceof com.google.android.material.button.MaterialButton) {
                    com.google.android.material.button.MaterialButton mb =
                            (com.google.android.material.button.MaterialButton) btn;
                    mb.setStrokeColor(
                            ColorStateList.valueOf(Color.parseColor("#28A745")));
                    mb.setStrokeWidth(4); // ~1.5dp em pixels
                }
                break;

            // VISUALIZAR: fundo branco + borda azul + texto azul
            case Palestra.StatusInscricao.VISUALIZAR:
            default:
                btn.setText("Visualizar");
                btn.setTextColor(Color.parseColor("#1B3A6B"));
                btn.setBackgroundTintList(
                        ColorStateList.valueOf(Color.WHITE));
                if (btn instanceof com.google.android.material.button.MaterialButton) {
                    com.google.android.material.button.MaterialButton mb =
                            (com.google.android.material.button.MaterialButton) btn;
                    mb.setStrokeColor(
                            ColorStateList.valueOf(Color.parseColor("#1B3A6B")));
                    mb.setStrokeWidth(4);
                }
                break;
        }
    }

    // ViewHolder: referências às views do card
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoraInicio; // "09:00"
        TextView tvHoraFim;    // "10:30"
        TextView tvTitulo;
        TextView tvPalestrante;
        TextView tvLocal;
        Button   btnAcao;      // botão dinâmico

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoraInicio  = itemView.findViewById(R.id.tvHoraInicio);
            tvHoraFim     = itemView.findViewById(R.id.tvHoraFim);
            tvTitulo      = itemView.findViewById(R.id.tvTitulo);
            tvPalestrante = itemView.findViewById(R.id.tvPalestrante);
            tvLocal       = itemView.findViewById(R.id.tvLocal);
            btnAcao       = itemView.findViewById(R.id.btnAcao);
        }
    }
}