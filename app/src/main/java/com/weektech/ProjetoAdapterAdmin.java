package com.weektech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProjetoAdapterAdmin extends RecyclerView.Adapter<ProjetoAdapterAdmin.ViewHolder> {
    private List<Projeto> lista = new ArrayList<>();
    private ProjetoDao projetoDao;

    public ProjetoAdapterAdmin(List<Projeto> lista, ProjetoDao dao) {
        if (lista != null) {
            this.lista = lista;
        }
        this.projetoDao = dao;
    }

    public void setProjetos(List<Projeto> lista) {
        this.lista = lista != null ? lista : new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_projeto_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Projeto projeto = lista.get(position);
        holder.txtTitulo.setText(projeto.getTitulo());
        holder.txtAluno.setText("Aluno: " + projeto.getAlunoId());
        holder.editData.setText(projeto.getDataApresentacao());
        holder.editHora.setText(projeto.getHoraApresentacao());

        holder.btnSalvar.setOnClickListener(v -> {
            String data = holder.editData.getText().toString();
            String hora = holder.editHora.getText().toString();
            projeto.setDataApresentacao(data);
            projeto.setHoraApresentacao(hora);
            new Thread(() -> projetoDao.update(projeto)).start();
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtAluno;
        EditText editData, editHora;
        Button btnSalvar;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloProjeto);
            txtAluno = itemView.findViewById(R.id.txtAluno);
            editData = itemView.findViewById(R.id.editDataApresentacao);
            editHora = itemView.findViewById(R.id.editHoraApresentacao);
            btnSalvar = itemView.findViewById(R.id.btnSalvarDataHora);
        }
    }
}
