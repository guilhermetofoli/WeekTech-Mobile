package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.weektech.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProjetoActivity extends AppCompatActivity 
        implements ProjetoAdapterAdmin.OnProjetoStatusChangeListener, ProjetoAdapter.OnProjetoClickListener {

    private TextInputEditText etNomeProjeto, etDescricao;
    private Button btnSalvarProjeto;
    private ProjetoAdapter projetoAdapter;
    private ProjetoAdapterAdmin adminAdapter;
    private ProjetoDao projetoDao;
    private SessionManager session;
    private BottomNavigationView bottomNav;
    private TabLayout tabLayout;
    private List<Projeto> todosProjetos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto);

        session = new SessionManager(this);
        projetoDao = AppDatabase.getInstance(this).projetoDao();

        etNomeProjeto = findViewById(R.id.etNomeProjeto);
        etDescricao = findViewById(R.id.etDescricaoProjeto);
        btnSalvarProjeto = findViewById(R.id.btnSalvarProjeto);
        RecyclerView rvProjetos = findViewById(R.id.rvProjetos);
        bottomNav = findViewById(R.id.bottomNav);
        tabLayout = findViewById(R.id.tabLayoutProjetos);
        LinearLayout layoutCadastro = findViewById(R.id.layoutCadastroProjeto);

        rvProjetos.setLayoutManager(new LinearLayoutManager(this));

        if (session.isAdmin()) {
            if (layoutCadastro != null) layoutCadastro.setVisibility(View.GONE);
            if (tabLayout != null) tabLayout.setVisibility(View.VISIBLE);
            
            adminAdapter = new ProjetoAdapterAdmin(null, projetoDao, this);
            rvProjetos.setAdapter(adminAdapter);
            
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    filtrarProjetos(tab.getPosition());
                }
                @Override public void onTabUnselected(TabLayout.Tab tab) {}
                @Override public void onTabReselected(TabLayout.Tab tab) {}
            });
        } else {
            if (layoutCadastro != null) layoutCadastro.setVisibility(View.VISIBLE);
            if (tabLayout != null) tabLayout.setVisibility(View.GONE);
            
            projetoAdapter = new ProjetoAdapter(this);
            rvProjetos.setAdapter(projetoAdapter);
        }

        configurarNavegacao();
        carregarProjetos();

        if (btnSalvarProjeto != null) {
            btnSalvarProjeto.setOnClickListener(v -> salvarProjeto());
        }
    }

    @Override
    public void onStatusChanged() {
        carregarProjetos();
    }

    @Override
    public void onDeleteClick(Projeto projeto) {
        new AlertDialog.Builder(this)
                .setTitle("Remover Projeto")
                .setMessage("Deseja remover este projeto reprovado para enviar um novo?")
                .setPositiveButton("Sim, Remover", (dialog, which) -> {
                    AppDatabase.databaseExecutor.execute(() -> {
                        projetoDao.deletarPorId(projeto.id);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Projeto removido. Você pode enviar um novo agora.", Toast.LENGTH_SHORT).show();
                            carregarProjetos();
                        });
                    });
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void configurarNavegacao() {
        if (!session.isAdmin()) {
            Menu menu = bottomNav.getMenu();
            MenuItem adminItem = menu.findItem(R.id.nav_admin);
            if (adminItem != null) adminItem.setVisible(false);
        }

        bottomNav.setSelectedItemId(R.id.nav_projetos);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_schedule) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_projetos) {
                return true;
            } else if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, PerfilActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_admin) {
                if (session.isAdmin()) {
                    startActivity(new Intent(this, AdminActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
                return true;
            }
            return false;
        });
    }

    private void salvarProjeto() {
        String nomeProjeto = getText(etNomeProjeto);
        String descricao = getText(etDescricao);
        String ra = session.getRa();
        String nome = session.getNome();

        if (nomeProjeto.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha o nome e descrição!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSalvarProjeto.setEnabled(false);
        AppDatabase.databaseExecutor.execute(() -> {
            Projeto existente = projetoDao.buscarProjetoPorRa(ra);
            if (existente != null) {
                runOnUiThread(() -> {
                    btnSalvarProjeto.setEnabled(true);
                    String msg = "Você já possui um projeto com status: " + existente.status;
                    if ("REPROVADO".equals(existente.status)) {
                        msg += ". Remova-o na lista abaixo para enviar um novo.";
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                });
                return;
            }

            Projeto projeto = new Projeto(nome, ra, nomeProjeto, descricao);
            projeto.dataCriacao = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            projeto.horaCriacao = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            projeto.status = "PENDENTE";

            long id = projetoDao.inserir(projeto);

            runOnUiThread(() -> {
                btnSalvarProjeto.setEnabled(true);
                if (id > 0) {
                    Toast.makeText(this, "Projeto enviado para análise!", Toast.LENGTH_SHORT).show();
                    etNomeProjeto.setText("");
                    etDescricao.setText("");
                    carregarProjetos();
                }
            });
        });
    }

    private void carregarProjetos() {
        AppDatabase.databaseExecutor.execute(() -> {
            if (session.isAdmin()) {
                todosProjetos = projetoDao.listarTodos();
                runOnUiThread(() -> filtrarProjetos(tabLayout.getSelectedTabPosition()));
            } else {
                // Aluno vê os dele (mesmo pendente/reprovado)
                List<Projeto> meusProjetos = new ArrayList<>();
                Projeto p = projetoDao.buscarProjetoPorRa(session.getRa());
                if (p != null) meusProjetos.add(p);
                
                // E também os agendados (aprovados) de outros alunos
                List<Projeto> agendados = projetoDao.listarAgendados();
                for (Projeto a : agendados) {
                    if (!a.ra.equals(session.getRa())) {
                        meusProjetos.add(a);
                    }
                }
                
                runOnUiThread(() -> projetoAdapter.setProjetos(meusProjetos));
            }
        });
    }

    private void filtrarProjetos(int position) {
        String statusFiltro;
        switch (position) {
            case 1: statusFiltro = "APROVADO"; break;
            case 2: statusFiltro = "REPROVADO"; break;
            default: statusFiltro = "PENDENTE"; break;
        }

        List<Projeto> filtrados = todosProjetos.stream()
                .filter(p -> p.status.equals(statusFiltro))
                .collect(Collectors.toList());
        
        if (adminAdapter != null) {
            adminAdapter.setProjetos(filtrados);
        }
    }

    private String getText(TextInputEditText et) {
        return et != null && et.getText() != null ? et.getText().toString().trim() : "";
    }
}
