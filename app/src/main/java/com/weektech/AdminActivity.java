package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

        // configura o spinner dos dias
        ArrayAdapter<String> adapterDia = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Dia 1", "Dia 2", "Dia 3"});
        adapterDia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDia.setAdapter(adapterDia);

        // spinner do tempo (40 a 60 min)
        String[] tempos = new String[21];
        for (int i = 0; i <= 20; i++) tempos[i] = (40 + i) + " minutos";
        ArrayAdapter<String> adapterTempo = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tempos);
        adapterTempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTempo.setAdapter(adapterTempo);

        // configura a lista de palestras
        rvAdminPalestras.setLayoutManager(new LinearLayoutManager(this));
        adminAdapter = new AdminPalestraAdapter(this);
        rvAdminPalestras.setAdapter(adminAdapter);

        // observa as palestras do banco
        palestraDao.listarTodas().observe(this, palestras ->
                adminAdapter.setPalestras(palestras));

        btnSalvarPalestra.setOnClickListener(v -> salvarPalestra());

        // abre a tela de gerenciar alunos
        findViewById(R.id.btnGerenciarAlunos).setOnClickListener(v -> {
            startActivity(new Intent(this, ListaAlunosActivity.class));
        });

        configurarNavegacao();
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
        int    tempo      = 40 + spinnerTempo.getSelectedItemPosition();

        if (titulo.isEmpty() || palestrante.isEmpty() || horaInicio.isEmpty() || horaFim.isEmpty()) {
            Toast.makeText(this, "Preencha os campos obrigatórios!", Toast.LENGTH_SHORT).show();
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
        spinnerTempo.setSelection(Math.max(0, palestra.tempo - 40));
        
        btnSalvarPalestra.setText("ATUALIZAR PALESTRA");
        tvFormTitle.setText("EDITAR PALESTRA");
        
        // sobe a tela
        findViewById(R.id.tvAdminFormTitle).getParent().getParent().requestLayout();
        ((View)findViewById(R.id.tvAdminFormTitle).getParent().getParent()).scrollTo(0, 0);
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
