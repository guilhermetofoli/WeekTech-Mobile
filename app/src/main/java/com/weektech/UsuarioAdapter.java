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

    // pra atualizar a lista de alunos na tela
    public void setUsuarios(List<Usuario> lista) {
        this.usuarios = lista != null ? lista : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Usuario u = usuarios.get(position);
        holder.tvNome.setText(u.nome);
        holder.tvRA.setText("RA: " + u.ra);
        holder.tvCurso.setText(u.curso + " - " + u.serie);
        
        // se o aluno marcou coffee break, mostra a tag verdinha
        if (u.coffeeBreak) {
            holder.tvTagCoffee.setVisibility(View.VISIBLE);
        } else {
            holder.tvTagCoffee.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return usuarios.size(); }

    // linkando as coisas do xml aqui
    static class VH extends RecyclerView.ViewHolder {
        TextView tvNome, tvRA, tvCurso, tvTagCoffee;
        VH(@NonNull View v) {
            super(v);
            tvNome = v.findViewById(R.id.tvNomeUsuario);
            tvRA   = v.findViewById(R.id.tvRaUsuario);
            tvCurso = v.findViewById(R.id.tvCursoUsuario);
            tvTagCoffee = v.findViewById(R.id.tvTagCoffee);
        }
    }
}
