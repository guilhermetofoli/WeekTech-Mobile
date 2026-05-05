package com.weektech;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.weektech.util.SessionManager;

public class ProjetoActivity extends AppCompatActivity {

    private TextInputEditText etNomeProjeto, etDescricao;
    private Button btnSalvarProjeto;
    private RecyclerView rvProjetos;
    private ProjetoAdapter projetoAdapter;
    private ProjetoDao projetoDao;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto);

        session = new SessionManager(this);
        projetoDao = AppDatabase.getInstance(this).projetoDao();

        etNomeProjeto  = findViewById(R.id.etNomeProjeto);
        etDescricao    = findViewById(R.id.etDescricaoProjeto);
        btnSalvarProjeto = findViewById(R.id.btnSalvarProjeto);
        rvProjetos     = findViewById(R.id.rvProjetos);

        rvProjetos.setLayoutManager(new LinearLayoutManager(this));
        projetoAdapter = new ProjetoAdapter();
        rvProjetos.setAdapter(projetoAdapter);

        carregarProjetos();

        btnSalvarProjeto.setOnClickListener(v -> salvarProjeto());
    }

    private void salvarProjeto() {
        String nomeProjeto = getText(etNomeProjeto);
        String descricao   = getText(etDescricao);
        String ra   = session.getRa();
        String nome = session.getNome();

        if (nomeProjeto.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha o nome e descrição do projeto!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ra.isEmpty()) {
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
            java.util.List<Projeto> lista = projetoDao.listarTodos();
            runOnUiThread(() -> projetoAdapter.setProjetos(lista));
        });
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
