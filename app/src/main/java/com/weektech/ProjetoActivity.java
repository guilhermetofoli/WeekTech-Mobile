package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.weektech.util.SessionManager;

import java.util.List;

public class ProjetoActivity extends AppCompatActivity {

    private TextInputEditText etNomeProjeto, etDescricao;
    private Button btnSalvarProjeto;
    private RecyclerView rvProjetos;
    private ProjetoAdapter projetoAdapter;
    private ProjetoDao projetoDao;
    private SessionManager session;
    private BottomNavigationView bottomNav; // Novo componente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto);

        // Inicializações
        session = new SessionManager(this);
        projetoDao = AppDatabase.getInstance(this).projetoDao();

        // Referências da UI
        etNomeProjeto = findViewById(R.id.etNomeProjeto);
        etDescricao = findViewById(R.id.etDescricaoProjeto);
        btnSalvarProjeto = findViewById(R.id.btnSalvarProjeto);
        rvProjetos = findViewById(R.id.rvProjetos);
        bottomNav = findViewById(R.id.bottomNav); // Referência do rodapé

        // Configuração do RecyclerView
        rvProjetos.setLayoutManager(new LinearLayoutManager(this));
        projetoAdapter = new ProjetoAdapter();
        rvProjetos.setAdapter(projetoAdapter);

        // Configuração do Rodapé (Navegação)
        configurarNavegacao();

        carregarProjetos();

        btnSalvarProjeto.setOnClickListener(v -> salvarProjeto());
    }

    private void configurarNavegacao() {
        // Marca o ícone de Projetos como selecionado
        bottomNav.setSelectedItemId(R.id.nav_projetos);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_schedule) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_projetos) {
                // Já está aqui
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
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Acesso Restrito")
                            .setMessage("Área exclusiva para administradores.")
                            .setPositiveButton("OK", null)
                            .show();
                    // Força a volta visual para o ícone de projetos
                    bottomNav.postDelayed(() -> bottomNav.setSelectedItemId(R.id.nav_projetos), 100);
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
            Toast.makeText(this, "Preencha o nome e descrição do projeto!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ra == null || ra.isEmpty()) {
            Toast.makeText(this, "Você precisa estar logado para cadastrar um projeto.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSalvarProjeto.setEnabled(false);

        AppDatabase.databaseExecutor.execute(() -> {
            if (projetoDao.verificarProjetoExistente(ra) > 0) {
                runOnUiThread(() -> {
                    btnSalvarProjeto.setEnabled(true);
                    Toast.makeText(this, "Você já cadastrou um projeto.", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            Projeto projeto = new Projeto(nome, ra, nomeProjeto, descricao);
            long id = projetoDao.inserir(projeto);

            runOnUiThread(() -> {
                btnSalvarProjeto.setEnabled(true);
                if (id > 0) {
                    Toast.makeText(this, "Projeto cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    etNomeProjeto.setText("");
                    etDescricao.setText("");
                    carregarProjetos();
                } else {
                    Toast.makeText(this, "Erro ao salvar projeto.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void carregarProjetos() {
        AppDatabase.databaseExecutor.execute(() -> {
            List<Projeto> lista = projetoDao.listarTodos();
            runOnUiThread(() -> projetoAdapter.setProjetos(lista));
        });
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}