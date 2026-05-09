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
import com.google.android.material.textfield.TextInputEditText;
import com.weektech.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProjetoActivity extends AppCompatActivity {

    // componentes da tela
    private TextInputEditText etNomeProjeto, etDescricao;
    private Button btnSalvarProjeto;
    private ProjetoAdapter projetoAdapter;
    private ProjetoAdapterAdmin adminAdapter;
    private ProjetoDao projetoDao;
    private SessionManager session;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto);

        session = new SessionManager(this);
        projetoDao = AppDatabase.getInstance(this).projetoDao();

        // pega as referencias do xml
        etNomeProjeto = findViewById(R.id.etNomeProjeto);
        etDescricao = findViewById(R.id.etDescricaoProjeto);
        btnSalvarProjeto = findViewById(R.id.btnSalvarProjeto);
        RecyclerView rvProjetos = findViewById(R.id.rvProjetos);
        bottomNav = findViewById(R.id.bottomNav);
        LinearLayout layoutCadastro = findViewById(R.id.layoutCadastroProjeto);

        rvProjetos.setLayoutManager(new LinearLayoutManager(this));

        // se for admin, nao pode cadastrar projeto, so ver a lista
        if (session.isAdmin()) {
            if (layoutCadastro != null) layoutCadastro.setVisibility(View.GONE);
            adminAdapter = new ProjetoAdapterAdmin(null, projetoDao);
            rvProjetos.setAdapter(adminAdapter);
        } else {
            // aluno pode cadastrar e ver os dele
            if (layoutCadastro != null) layoutCadastro.setVisibility(View.VISIBLE);
            projetoAdapter = new ProjetoAdapter();
            rvProjetos.setAdapter(projetoAdapter);
        }

        configurarNavegacao();
        carregarProjetos();

        btnSalvarProjeto.setOnClickListener(v -> salvarProjeto());
    }

    // trata os cliques no menu de baixo
    private void configurarNavegacao() {
        // esconde a aba admin se nao for admin logado
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

    // salva o projeto novo do aluno
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
            // so pode ter um projeto por aluno (RA)
            if (projetoDao.verificarProjetoExistente(ra) > 0) {
                runOnUiThread(() -> {
                    btnSalvarProjeto.setEnabled(true);
                    Toast.makeText(this, "Você já cadastrou um projeto.", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            Projeto projeto = new Projeto(nome, ra, nomeProjeto, descricao);
            
            // coloca a data e hora do servidor local
            projeto.dataCriacao = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            projeto.horaCriacao = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            long id = projetoDao.inserir(projeto);

            runOnUiThread(() -> {
                btnSalvarProjeto.setEnabled(true);
                if (id > 0) {
                    Toast.makeText(this, "Projeto cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    etNomeProjeto.setText("");
                    etDescricao.setText("");
                    carregarProjetos(); // atualiza a lista
                }
            });
        });
    }

    private void carregarProjetos() {
        AppDatabase.databaseExecutor.execute(() -> {
            List<Projeto> lista;
            if (session.isAdmin()) {
                // admin ve tudo
                lista = projetoDao.listarTodos();
                runOnUiThread(() -> adminAdapter.setProjetos(lista));
            } else {
                // aluno so ve os agendados pelo admin
                lista = projetoDao.listarAgendados();
                runOnUiThread(() -> projetoAdapter.setProjetos(lista));
            }
        });
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
