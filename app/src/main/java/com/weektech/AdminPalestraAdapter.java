package com.weektech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.weektech.Palestra;
import java.util.ArrayList;
import java.util.List;

public class AdminPalestraAdapter extends RecyclerView.Adapter<AdminPalestraAdapter.AdminViewHolder> {

    private List<Palestra> palestras = new ArrayList<>();
    private final OnAdminActionListener listener;

    // Interface que a sua AdminActivity vai implementar
    public interface OnAdminActionListener {
        void onToggleAtiva(Palestra palestra);
        void onDeletePalestra(Palestra palestra);
    }

    public AdminPalestraAdapter(OnAdminActionListener listener) {
        this.listener = listener;
    }

    public void setPalestras(List<Palestra> palestras) {
        this.palestras = palestras;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_palestra, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        Palestra p = palestras.get(position);
        holder.tvTitulo.setText(p.titulo);
        holder.btnToggle.setImageResource(p.ativa ?
                android.R.drawable.ic_menu_view :
                android.R.drawable.ic_menu_close_clear_cancel);

        holder.btnDelete.setImageResource(android.R.drawable.ic_menu_delete);
    }

    @Override
    public int getItemCount() {
        return palestras.size();
    }

    static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;
        ImageButton btnToggle, btnDelete;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvAdminItemTitulo);
            btnToggle = itemView.findViewById(R.id.btnAdminToggle);
            btnDelete = itemView.findViewById(R.id.btnAdminDelete);
        }
    }
}