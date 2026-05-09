package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class AdminActivity extends AppCompatActivity
        implements AdminPalestraAdapter.OnAdminActionListener {

    // variaveis da tela
    private TextInputEditText etTitulo, etPalestrante, etHoraInicio, etHoraFim,
            etLocal, etDescricao;
    private Spinner   spinnerDia, spinnerTempo;
    private Button    btnSalvarPalestra;
    private RecyclerView rvAdminPalestras;
    private AdminPalestraAdapter adminAdapter;
    private PalestraDao palestraDao;
    private Palestra palestraSendoEditada = null; // guarda qual palestra to editando
    private BottomNavigationView bottomNav;
    private SessionManager session;
    private TextView tvFormTitle;
    private NestedScrollView nestedScrollView;

    // Listas para o Spinner de Tempo
    private String[] temposLabels;
    private int[] temposValores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        session = new SessionManager(this);
        palestraDao = AppDatabase.getInstance(this).palestraDao();

        // ligando os componentes do xml
        etTitulo      = findViewById(R.id.etAdminTitulo);
        etPalestrante = findViewById(R.id.etAdminPalestrante);
        etHoraInicio  = findViewById(R.id.etAdminHoraInicio);
        etHoraFim     = findViewById(R.id.etAdminHoraFim);
        etLocal       = findViewById(R.id.etAdminLocal);
        etDescricao   = findViewById(R.id.etAdminDescricao);
        spinnerDia    = findViewById(R.id.spinnerDia);
        spinnerTempo  = findViewById(R.id.spinnerTempo);
        btnSalvarPalestra = findViewById(R.id.btnSalvarPalestra);
        rvAdminPalestras  = findViewById(R.id.rvAdminPalestras);
        bottomNav     = findViewById(R.id.bottomNav);
        tvFormTitle   = findViewById(R.id.tvAdminFormTitle);
        nestedScrollView = findViewById(R.id.nestedScrollViewAdmin);

        // configura o spinner dos dias
        ArrayAdapter<String> adapterDia = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Dia 1", "Dia 2", "Dia 3"});
        adapterDia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDia.setAdapter(adapterDia);

        // spinner do tempo (presets)
        String[] temposLabelsPreset = {"15 minutos", "30 minutos", "45 minutos", "60 minutos", "1h 15min", "1h 30min"};
        // Note: these presets might be overwritten by prepararTempos() later if they are not compatible.
        // Actually prepararTempos() is called below.

        // configura a lista de palestras
        rvAdminPalestras.setLayoutManager(new LinearLayoutManager(this));
        adminAdapter = new AdminPalestraAdapter(this);
        rvAdminPalestras.setAdapter(adminAdapter);

        // observa as palestras do banco
        palestraDao.listarTodas().observe(this, palestras ->
                adminAdapter.setPalestras(palestras));

        prepararTempos();
        btnSalvarPalestra.setOnClickListener(v -> salvarPalestra());

        // abre a tela de gerenciar alunos
        findViewById(R.id.btnGerenciarAlunos).setOnClickListener(v -> {
            startActivity(new Intent(this, ListaAlunosActivity.class));
        });

        aplicarMascaras();
        configurarNavegacao();
    }

    private void prepararTempos() {
        // Gera tempos de 15 em 15 minutos até 5 horas (300 min) + Dia Inteiro
        int maxMinutos = 300;
        int passo = 15;
        int quantidade = (maxMinutos / passo) + 1; // +1 para o "Dia Inteiro"
        
        temposLabels = new String[quantidade];
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
        
        // Adiciona a opção de Dia Inteiro
        temposLabels[quantidade - 1] = "Dia Inteiro";
        temposValores[quantidade - 1] = 1440; // 24 horas

        ArrayAdapter<String> adapterTempo = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, temposLabels);
        adapterTempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTempo.setAdapter(adapterTempo);
    }

    // aplica mascara de hora (HH:mm) nos campos de horario
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

                String str = s.toString().replaceAll("[^\\d]", "");
                if (str.length() > 4) str = str.substring(0, 4);

                // Validação de limites (00:00 a 23:59)
                if (str.length() >= 1) {
                    int h1 = Integer.parseInt(str.substring(0, 1));
                    if (h1 > 2) str = "2"; // Primeiro dígito da hora max 2
                }
                if (str.length() >= 2) {
                    int hora = Integer.parseInt(str.substring(0, 2));
                    if (hora > 23) str = "23"; // Hora max 23
                }
                if (str.length() >= 3) {
                    int m1 = Integer.parseInt(str.substring(2, 3));
                    if (m1 > 5) str = str.substring(0, 2) + "5"; // Primeiro dígito do minuto max 5
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

    // menu de baixo
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
            } else if (id == R.id.nav_admin) {
                return true;
            }
            return false;
        });
    }

    // funcao pra salvar ou atualizar
    private void salvarPalestra() {
        String titulo     = getText(etTitulo);
        String palestrante= getText(etPalestrante);
        String horaInicio = getText(etHoraInicio);
        String horaFim    = getText(etHoraFim);
        String local      = getText(etLocal);
        String descricao  = getText(etDescricao);
        int    dia        = spinnerDia.getSelectedItemPosition() + 1;
        int    tempo      = temposValores[spinnerTempo.getSelectedItemPosition()];

        if (titulo.isEmpty() || palestrante.isEmpty() || horaInicio.isEmpty() || horaFim.isEmpty()) {
            Toast.makeText(this, "Preencha os campos obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação extra de segurança para formato HH:mm completo
        if (horaInicio.length() < 5 || horaFim.length() < 5) {
            Toast.makeText(this, "Preencha o horário completo (ex: 09:00)", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSalvarPalestra.setEnabled(false);

        // se for edicao usa a mesma, senao cria nova
        Palestra p = (palestraSendoEditada != null) ? palestraSendoEditada : new Palestra();
        p.titulo = titulo;
        p.palestrante = palestrante;
        p.tempo = tempo;
        p.horaInicio = horaInicio;
        p.horaFim = horaFim;
        p.local = local;
        p.descricao = descricao;
        p.dia = dia;
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
        // preenche o form com os dados pra editar
        palestraSendoEditada = palestra;
        etTitulo.setText(palestra.titulo);
        etPalestrante.setText(palestra.palestrante);
        etHoraInicio.setText(palestra.horaInicio);
        etHoraFim.setText(palestra.horaFim);
        etLocal.setText(palestra.local);
        etDescricao.setText(palestra.descricao);
        spinnerDia.setSelection(palestra.dia - 1);
        
        // seleciona o tempo correto no spinner baseado no valor do banco
        for (int i = 0; i < temposValores.length; i++) {
            if (temposValores[i] == palestra.tempo) {
                spinnerTempo.setSelection(i);
                break;
            }
        }
        
        btnSalvarPalestra.setText("ATUALIZAR PALESTRA");
        tvFormTitle.setText("EDITAR PALESTRA");
        
        // sobe a tela para o formulario usando o scroll
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
        etDescricao.setText("");
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
