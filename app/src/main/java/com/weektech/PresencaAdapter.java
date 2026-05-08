package com.weektech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PresencaAdapter extends RecyclerView.Adapter<PresencaAdapter.ViewHolder> {
    private List<InscricaoComUsuario> lista;

    public PresencaAdapter(List<InscricaoComUsuario> lista) {
        this.lista = lista;
    }

    // atualiza a lista de alunos presentes
    public void setLista(List<InscricaoComUsuario> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // usa o layout padrao de item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_palestra, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InscricaoComUsuario item = lista.get(position);
        
        // se tiver os dados do usuario mostra nome e ra, senao so o ra
        if (item.usuario != null) {
            holder.nomeAluno.setText(item.usuario.nome + " (RA: " + item.usuario.ra + ")");
        } else {
            holder.nomeAluno.setText("RA: " + item.inscricao.raUsuario);
        }
        
        // mostra quando confirmou ou so um texto de ok
        holder.dataConfirmacao.setText(item.inscricao.dataConfirmacao != null ? item.inscricao.dataConfirmacao : "Confirmado");
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomeAluno, dataConfirmacao;

        ViewHolder(View itemView) {
            super(itemView);
            nomeAluno = itemView.findViewById(R.id.txtNomeAluno);
            dataConfirmacao = itemView.findViewById(R.id.txtDataConfirmacao);
        }
    }
}
