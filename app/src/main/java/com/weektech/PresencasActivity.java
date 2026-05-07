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
    private RecyclerView rvConfirmados, rvInscritos;
    private PresencaAdapter adapterConfirmados, adapterInscritos;
    private AppDatabase db;
    private int palestraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presencas);

        palestraId = getIntent().getIntExtra("palestra_id", -1);
        db = AppDatabase.getInstance(this);

        rvConfirmados = findViewById(R.id.rvConfirmados);
        rvInscritos = findViewById(R.id.rvInscritos);
        ImageButton btnVoltar = findViewById(R.id.btnVoltarPresencas);

        rvConfirmados.setLayoutManager(new LinearLayoutManager(this));
        rvInscritos.setLayoutManager(new LinearLayoutManager(this));

        adapterConfirmados = new PresencaAdapter(new ArrayList<>());
        adapterInscritos = new PresencaAdapter(new ArrayList<>());

        rvConfirmados.setAdapter(adapterConfirmados);
        rvInscritos.setAdapter(adapterInscritos);

        btnVoltar.setOnClickListener(v -> finish());

        carregarDados();
    }

    private void carregarDados() {
        new Thread(() -> {
            // Pegamos todos os inscritos (com info do usuário)
            List<InscricaoComUsuario> todos = db.inscricaoDao().getInscritosComUsuario(palestraId);
            
            // Filtramos quem está presente
            List<InscricaoComUsuario> presentes = todos.stream()
                    .filter(i -> i.inscricao.presente)
                    .collect(Collectors.toList());
            
            // Filtramos quem apenas se inscreveu (não presente)
            List<InscricaoComUsuario> apenasInscritos = todos.stream()
                    .filter(i -> !i.inscricao.presente)
                    .collect(Collectors.toList());

            runOnUiThread(() -> {
                adapterConfirmados.setLista(presentes);
                adapterInscritos.setLista(apenasInscritos);
                
                if (todos.isEmpty()) {
                    Toast.makeText(this, "Nenhuma inscrição encontrada.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
