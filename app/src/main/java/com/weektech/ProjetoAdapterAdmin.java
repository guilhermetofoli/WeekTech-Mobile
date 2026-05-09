package com.weektech;

import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProjetoAdapterAdmin extends RecyclerView.Adapter<ProjetoAdapterAdmin.ViewHolder> {
    private List<Projeto> lista = new ArrayList<>();
    private ProjetoDao projetoDao;
    private OnProjetoStatusChangeListener listener;

    public interface OnProjetoStatusChangeListener {
        void onStatusChanged();
    }

    public ProjetoAdapterAdmin(List<Projeto> lista, ProjetoDao dao, OnProjetoStatusChangeListener listener) {
        if (lista != null) {
            this.lista = lista;
        }
        this.projetoDao = dao;
        this.listener = listener;
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
        holder.txtAluno.setText("Aluno " + projeto.getNome() + " | " + projeto.getRa());
        holder.txtDescricao.setText(projeto.getDescricao());

        holder.editData.setText(projeto.getDataApresentacao());
        holder.editHoraInicio.setText(projeto.getHoraInicioApresentacao());
        holder.editHoraFim.setText(projeto.getHoraFimApresentacao());

        // Configuração visual baseada no STATUS
        if ("REPROVADO".equals(projeto.status)) {
            holder.layoutAgendamento.setVisibility(View.GONE);
            holder.btnReprovar.setVisibility(View.GONE);
            holder.btnAprovar.setText("REAVALIAR (VOLTAR PARA PENDENTES)");
            holder.btnAprovar.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1B3A6B")));
            
            holder.btnAprovar.setOnClickListener(v -> {
                projeto.status = "PENDENTE";
                new Thread(() -> {
                    projetoDao.update(projeto);
                    holder.itemView.post(() -> {
                        Toast.makeText(holder.itemView.getContext(), "Projeto voltou para Pendentes", Toast.LENGTH_SHORT).show();
                        if (listener != null) listener.onStatusChanged();
                    });
                }).start();
            });

        } else {
            // APROVADO ou PENDENTE
            holder.layoutAgendamento.setVisibility(View.VISIBLE);
            holder.btnReprovar.setVisibility(View.VISIBLE);
            holder.btnReprovar.setText("REPROVAR");
            holder.btnAprovar.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#28A745")));
            
            if ("APROVADO".equals(projeto.status)) {
                holder.btnAprovar.setText("ATUALIZAR AGENDA");
            } else {
                holder.btnAprovar.setText("APROVAR");
            }

            // Lógica do Calendário
            holder.editData.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(holder.itemView.getContext(),
                        R.style.CustomDatePickerDialog,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            String date = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                            holder.editData.setText(date);
                        }, year, month, day);

                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.show();
            });

            aplicarMascaraHora(holder.editHoraInicio);
            aplicarMascaraHora(holder.editHoraFim);

            holder.btnReprovar.setOnClickListener(v -> {
                projeto.setStatus("REPROVADO");
                new Thread(() -> {
                    projetoDao.update(projeto);
                    holder.itemView.post(() -> {
                        Toast.makeText(holder.itemView.getContext(), "Projeto Reprovado!", Toast.LENGTH_SHORT).show();
                        if (listener != null) listener.onStatusChanged();
                    });
                }).start();
            });

            holder.btnAprovar.setOnClickListener(v -> {
                String data = holder.editData.getText().toString();
                String inicio = holder.editHoraInicio.getText().toString();
                String fim = holder.editHoraFim.getText().toString();

                if (data.isEmpty() || inicio.isEmpty() || fim.isEmpty()) {
                    Toast.makeText(holder.itemView.getContext(), "Preencha a agenda para aprovar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                projeto.setDataApresentacao(data);
                projeto.setHoraInicioApresentacao(inicio);
                projeto.setHoraFimApresentacao(fim);
                projeto.setStatus("APROVADO");
                
                new Thread(() -> {
                    projetoDao.update(projeto);
                    holder.itemView.post(() -> {
                        Toast.makeText(holder.itemView.getContext(), "Projeto Aprovado!", Toast.LENGTH_SHORT).show();
                        if (listener != null) listener.onStatusChanged();
                    });
                }).start();
            });
        }
    }

    private void aplicarMascaraHora(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;
                String str = s.toString().replaceAll("[^\\d]", "");
                if (str.length() > 4) str = str.substring(0, 4);
                String formatted = str;
                if (str.length() >= 3) {
                    formatted = str.substring(0, 2) + ":" + str.substring(2);
                }
                isUpdating = true;
                editText.setText(formatted);
                editText.setSelection(formatted.length());
                isUpdating = false;
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtAluno, txtDescricao;
        EditText editData, editHoraInicio, editHoraFim;
        Button btnAprovar, btnReprovar;
        View layoutAgendamento;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloProjeto);
            txtAluno = itemView.findViewById(R.id.txtAluno);
            txtDescricao = itemView.findViewById(R.id.txtDescricaoProjeto);
            editData = itemView.findViewById(R.id.editDataApresentacao);
            editHoraInicio = itemView.findViewById(R.id.editHoraInicio);
            editHoraFim = itemView.findViewById(R.id.editHoraFim);
            btnAprovar = itemView.findViewById(R.id.btnAprovar);
            btnReprovar = itemView.findViewById(R.id.btnReprovar);
            layoutAgendamento = itemView.findViewById(R.id.layoutAgendamentoAdmin);
        }
    }
}
