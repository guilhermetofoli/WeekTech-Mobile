package com.weektech;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.weektech.util.SessionManager;

import java.util.Calendar;
import java.util.Locale;

public class AdminActivity extends AppCompatActivity
        implements AdminPalestraAdapter.OnAdminActionListener {

    private TextInputEditText etTitulo, etPalestrante, etHoraInicio, etHoraFim,
            etLocal, etDescricao, etData;
    private Spinner   spinnerTempo;
    private Button    btnSalvarPalestra;
    private RecyclerView rvAdminPalestras;
    private AdminPalestraAdapter adminAdapter;
    private PalestraDao palestraDao;
    private Palestra palestraSendoEditada = null;
    private BottomNavigationView bottomNav;
    private SessionManager session;
    private TextView tvFormTitle;
    private NestedScrollView nestedScrollView;

    private int[] temposValores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        session = new SessionManager(this);
        palestraDao = AppDatabase.getInstance(this).palestraDao();

        etTitulo      = findViewById(R.id.etAdminTitulo);
        etPalestrante = findViewById(R.id.etAdminPalestrante);
        etHoraInicio  = findViewById(R.id.etAdminHoraInicio);
        etHoraFim     = findViewById(R.id.etAdminHoraFim);
        etLocal       = findViewById(R.id.etAdminLocal);
        etDescricao   = findViewById(R.id.etAdminDescricao);
        etData        = findViewById(R.id.etAdminData);
        spinnerTempo  = findViewById(R.id.spinnerTempo);
        btnSalvarPalestra = findViewById(R.id.btnSalvarPalestra);
        rvAdminPalestras  = findViewById(R.id.rvAdminPalestras);
        bottomNav     = findViewById(R.id.bottomNav);
        tvFormTitle   = findViewById(R.id.tvAdminFormTitle);
        nestedScrollView = findViewById(R.id.nestedScrollViewAdmin);

        rvAdminPalestras.setLayoutManager(new LinearLayoutManager(this));
        adminAdapter = new AdminPalestraAdapter(this);
        rvAdminPalestras.setAdapter(adminAdapter);

        palestraDao.listarTodas().observe(this, palestras ->
                adminAdapter.setPalestras(palestras));

        prepararTempos();
        configurarDatePicker();
        
        btnSalvarPalestra.setOnClickListener(v -> salvarPalestra());

        findViewById(R.id.btnGerenciarAlunos).setOnClickListener(v -> {
            startActivity(new Intent(this, ListaAlunosActivity.class));
        });

        aplicarMascaras();
        configurarNavegacao();
    }

    private void configurarDatePicker() {
        if (etData == null) return;
        etData.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(this,
                    R.style.CustomDatePickerDialog,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                        etData.setText(date);
                    }, year, month, day);

            datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePicker.show();
        });
    }

    private void prepararTempos() {
        int maxMinutos = 300;
        int passo = 15;
        int quantidade = (maxMinutos / passo) + 1;
        
        String[] temposLabels = new String[quantidade];
        temposValores = new int[quantidade];

        for (int i = 0; i < quantidade - 1; i++) {
            int totalMinutos = (i + 1) * passo;
            temposValores[i] = totalMinutos;
            
            if (totalMinutos < 60) {
                temposLabels[i] = totalMinutos + " minutos";
            } else {
                int horas = totalMinutos / 60;
                int minutos = totalMinutos % 60;
                if (minutos == 0) {
                    temposLabels[i] = horas + "h";
                } else {
                    temposLabels[i] = horas + "h " + minutos + "min";
                }
            }
        }
        
        temposLabels[quantidade - 1] = "Dia Inteiro";
        temposValores[quantidade - 1] = 1440;

        ArrayAdapter<String> adapterTempo = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, temposLabels);
        adapterTempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTempo.setAdapter(adapterTempo);
    }

    private void aplicarMascaras() {
        aplicarMascaraHora(etHoraInicio);
        aplicarMascaraHora(etHoraFim);
    }

    private void aplicarMascaraHora(TextInputEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;

                String str = s.toString().replaceAll("\\D", "");
                if (str.length() > 4) str = str.substring(0, 4);

                if (!str.isEmpty()) {
                    int h1 = Integer.parseInt(str.substring(0, 1));
                    if (h1 > 2) str = "2";
                }
                if (str.length() >= 2) {
                    int hora = Integer.parseInt(str.substring(0, 2));
                    if (hora > 23) str = "23";
                }
                if (str.length() >= 3) {
                    int m1 = Integer.parseInt(str.substring(2, 3));
                    if (m1 > 5) str = str.substring(0, 2) + "5";
                }

                String formatted = str;
                if (str.length() >= 3) {
                    formatted = str.substring(0, 2) + ":" + str.substring(2);
                }

                isUpdating = true;
                editText.setText(formatted);
                editText.setSelection(formatted.length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void configurarNavegacao() {
        bottomNav.setSelectedItemId(R.id.nav_admin);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_schedule) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_projetos) {
                startActivity(new Intent(this, ProjetoActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, PerfilActivity.class));
                finish();
                return true;
            } else return id == R.id.nav_admin;
        });
    }

    private void salvarPalestra() {
        String titulo     = getText(etTitulo);
        String palestrante= getText(etPalestrante);
        String horaInicio = getText(etHoraInicio);
        String horaFim    = getText(etHoraFim);
        String local      = getText(etLocal);
        String descricao  = getText(etDescricao);
        String data       = getText(etData);
        int    tempo      = temposValores[spinnerTempo.getSelectedItemPosition()];

        if (titulo.isEmpty() || palestrante.isEmpty() || horaInicio.isEmpty() || horaFim.isEmpty() || data.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (horaInicio.length() < 5 || horaFim.length() < 5) {
            Toast.makeText(this, "Preencha o horário completo!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSalvarPalestra.setEnabled(false);

        Palestra p = (palestraSendoEditada != null) ? palestraSendoEditada : new Palestra();
        p.titulo = titulo;
        p.palestrante = palestrante;
        p.tempo = tempo;
        p.horaInicio = horaInicio;
        p.horaFim = horaFim;
        p.local = local;
        p.descricao = descricao;
        p.data = data;
        p.ativa = true;

        AppDatabase.databaseExecutor.execute(() -> {
            if (palestraSendoEditada == null) {
                palestraDao.inserir(p);
            } else {
                palestraDao.update(p);
            }
            runOnUiThread(() -> {
                btnSalvarPalestra.setEnabled(true);
                Toast.makeText(this, "Palestra salva com sucesso!", Toast.LENGTH_SHORT).show();
                limparCampos();
                palestraSendoEditada = null;
                btnSalvarPalestra.setText("SALVAR PALESTRA");
                tvFormTitle.setText("CADASTRAR NOVA PALESTRA");
            });
        });
    }

    @Override
    public void onEditPalestra(Palestra palestra) {
        palestraSendoEditada = palestra;
        etTitulo.setText(palestra.titulo);
        etPalestrante.setText(palestra.palestrante);
        etHoraInicio.setText(palestra.horaInicio);
        etHoraFim.setText(palestra.horaFim);
        etLocal.setText(palestra.local);
        etDescricao.setText(palestra.descricao);
        etData.setText(palestra.data);
        
        for (int i = 0; i < temposValores.length; i++) {
            if (temposValores[i] == palestra.tempo) {
                spinnerTempo.setSelection(i);
                break;
            }
        }
        
        btnSalvarPalestra.setText("ATUALIZAR PALESTRA");
        tvFormTitle.setText("EDITAR PALESTRA");
        nestedScrollView.smoothScrollTo(0, 0);
    }

    @Override
    public void onToggleAtiva(Palestra palestra) {
        AppDatabase.databaseExecutor.execute(() -> {
            palestraDao.atualizarStatus(palestra.id, !palestra.ativa);
        });
    }

    @Override
    public void onDeletePalestra(Palestra palestra) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Palestra")
                .setMessage("Deseja realmente excluir esta palestra?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    AppDatabase.databaseExecutor.execute(() -> {
                        palestraDao.deletar(palestra);
                    });
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void limparCampos() {
        etTitulo.setText(""); etPalestrante.setText("");
        etHoraInicio.setText(""); etHoraFim.setText(""); etLocal.setText("");
        etDescricao.setText(""); etData.setText("");
    }

    private String getText(TextInputEditText et) {
        return et != null && et.getText() != null ? et.getText().toString().trim() : "";
    }
}
