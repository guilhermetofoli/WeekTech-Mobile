package com.weektech;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PresencasActivity extends AppCompatActivity {
    // listas e adapters pra separar quem foi de quem so se inscreveu
    private RecyclerView rvConfirmados, rvInscritos;
    private PresencaAdapter adapterConfirmados, adapterInscritos;
    private AppDatabase db;
    private int palestraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presencas);

        // pega o id da palestra que veio da outra tela
        palestraId = getIntent().getIntExtra("palestra_id", -1);
        db = AppDatabase.getInstance(this);

        rvConfirmados = findViewById(R.id.rvConfirmados);
        rvInscritos = findViewById(R.id.rvInscritos);
        ImageButton btnVoltar = findViewById(R.id.btnVoltarPresencas);

        // configura os recycles
        rvConfirmados.setLayoutManager(new LinearLayoutManager(this));
        rvInscritos.setLayoutManager(new LinearLayoutManager(this));

        adapterConfirmados = new PresencaAdapter(new ArrayList<>());
        adapterInscritos = new PresencaAdapter(new ArrayList<>());

        rvConfirmados.setAdapter(adapterConfirmados);
        rvInscritos.setAdapter(adapterInscritos);

        // clica no X pra fechar
        btnVoltar.setOnClickListener(v -> finish());

        carregarDados();
    }

    private void carregarDados() {
        new Thread(() -> {
            // pega todo mundo que se inscreveu nessa palestra
            List<InscricaoComUsuario> todos = db.inscricaoDao().getInscritosComUsuario(palestraId);
            
            // separa a galera que deu checkin (presente = true)
            List<InscricaoComUsuario> presentes = todos.stream()
                    .filter(i -> i.inscricao.presente)
                    .collect(Collectors.toList());
            
            // separa quem so se inscreveu mas nao apareceu
            List<InscricaoComUsuario> apenasInscritos = todos.stream()
                    .filter(i -> !i.inscricao.presente)
                    .collect(Collectors.toList());

            // joga pra tela
            runOnUiThread(() -> {
                adapterConfirmados.setLista(presentes);
                adapterInscritos.setLista(apenasInscritos);
                
                if (todos.isEmpty()) {
                    Toast.makeText(this, "Ninguém inscrito ainda.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
