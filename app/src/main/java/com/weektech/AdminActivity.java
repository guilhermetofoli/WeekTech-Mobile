package com.weektech;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.weektech.AppDatabase;
import com.weektech.Palestra;

public class AdminActivity extends AppCompatActivity
        implements AdminPalestraAdapter.OnAdminActionListener {

    // Cadastro de nova palestra
    private TextInputEditText etTitulo, etPalestrante, etEmail, etTelefone,
            etBriefing, etCurriculo, etHoraInicio, etHoraFim,
            etLocal, etDescricao;
    private Spinner   spinnerDia, spinnerTempo;
    private Button    btnSalvarPalestra;
    private RecyclerView rvAdminPalestras;
    private AdminPalestraAdapter adminAdapter;
    private PalestraDao palestraDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        palestraDao = AppDatabase.getInstance(this).palestraDao();

        etTitulo     = findViewById(R.id.etAdminTitulo);
        etPalestrante= findViewById(R.id.etAdminPalestrante);
        etEmail      = findViewById(R.id.etAdminEmail);
        etTelefone   = findViewById(R.id.etAdminTelefone);
        etBriefing   = findViewById(R.id.etAdminBriefing);
        etCurriculo  = findViewById(R.id.etAdminCurriculo);
        etHoraInicio = findViewById(R.id.etAdminHoraInicio);
        etHoraFim    = findViewById(R.id.etAdminHoraFim);
        etLocal      = findViewById(R.id.etAdminLocal);
        etDescricao  = findViewById(R.id.etAdminDescricao);
        spinnerDia   = findViewById(R.id.spinnerDia);
        spinnerTempo = findViewById(R.id.spinnerTempo);
        btnSalvarPalestra = findViewById(R.id.btnSalvarPalestra);
        rvAdminPalestras  = findViewById(R.id.rvAdminPalestras);

        // Spinner Dia
        ArrayAdapter<String> adapterDia = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Dia 1", "Dia 2", "Dia 3"});
        adapterDia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDia.setAdapter(adapterDia);

        // Spinner Tempo (40 a 60 min)
        String[] tempos = new String[21];
        for (int i = 0; i <= 20; i++) tempos[i] = (40 + i) + " minutos";
        ArrayAdapter<String> adapterTempo = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tempos);
        adapterTempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTempo.setAdapter(adapterTempo);

        // RecyclerView de palestras cadastradas
        rvAdminPalestras.setLayoutManager(new LinearLayoutManager(this));
        adminAdapter = new AdminPalestraAdapter(this);
        rvAdminPalestras.setAdapter(adminAdapter);

        palestraDao.listarTodas().observe(this, palestras ->
                adminAdapter.setPalestras(palestras));

        btnSalvarPalestra.setOnClickListener(v -> salvarPalestra());

        Button btnVoltar = findViewById(R.id.btnAdminVoltar);
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void salvarPalestra() {
        String titulo     = getText(etTitulo);
        String palestrante= getText(etPalestrante);
        String email      = getText(etEmail);
        String telefone   = getText(etTelefone);
        String briefing   = getText(etBriefing);
        String curriculo  = getText(etCurriculo);
        String horaInicio = getText(etHoraInicio);
        String horaFim    = getText(etHoraFim);
        String local      = getText(etLocal);
        String descricao  = getText(etDescricao);
        int    dia        = spinnerDia.getSelectedItemPosition() + 1;
        int    tempo      = 40 + spinnerTempo.getSelectedItemPosition();

        if (titulo.isEmpty() || palestrante.isEmpty() || horaInicio.isEmpty() || horaFim.isEmpty()) {
            Toast.makeText(this, "Título, palestrante e horários são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSalvarPalestra.setEnabled(false);

        Palestra p = new Palestra(titulo, palestrante, email, telefone,
                briefing, curriculo, tempo, horaInicio, horaFim,
                local, descricao, dia, true);

        AppDatabase.databaseExecutor.execute(() -> {
            long id = palestraDao.inserir(p);
            runOnUiThread(() -> {
                btnSalvarPalestra.setEnabled(true);
                if (id > 0) {
                    Toast.makeText(this, "Palestra cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                    limparCampos();
                } else {
                    Toast.makeText(this, "Erro ao salvar.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onToggleAtiva(Palestra palestra) {
        AppDatabase.databaseExecutor.execute(() -> {
            palestraDao.atualizarStatus(palestra.id, !palestra.ativa);
            runOnUiThread(() -> Toast.makeText(this,
                    palestra.ativa ? "Palestra desativada." : "Palestra ativada.",
                    Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public void onDeletePalestra(Palestra palestra) {
        AppDatabase.databaseExecutor.execute(() -> {
            palestraDao.deletar(palestra);
            runOnUiThread(() -> Toast.makeText(this,
                    "Palestra removida.", Toast.LENGTH_SHORT).show());
        });
    }

    private void limparCampos() {
        etTitulo.setText(""); etPalestrante.setText(""); etEmail.setText("");
        etTelefone.setText(""); etBriefing.setText(""); etCurriculo.setText("");
        etHoraInicio.setText(""); etHoraFim.setText(""); etLocal.setText("");
        etDescricao.setText("");
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}

