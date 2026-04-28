package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

/**
 * Tela principal do app
 * Responsável por listar as palestras e navegar entre telas
 */

public class HomeActivity extends AppCompatActivity
        implements PalestraAdapter.OnItemClickListener {

    private RecyclerView rvPalestras;
    private PalestraAdapter adapter;
    private PalestraDao palestraDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicializa RecyclerView
        rvPalestras = findViewById(R.id.rvPalestras);
        rvPalestras.setLayoutManager(new LinearLayoutManager(this));

        // Adapter começa com lista vazia
        adapter = new PalestraAdapter(new ArrayList<>(), this);
        rvPalestras.setAdapter(adapter);

        // Conecta com banco de dados (Room)
        palestraDao = AppDatabase.getInstance(this).palestraDao();

        // Observa mudanças na lista de palestras (LiveData)
        palestraDao.listarAtivas().observe(this, new Observer<List<Palestra>>() {
            @Override
            public void onChanged(List<Palestra> palestras) {
                // Atualiza a lista na tela automaticamente
                adapter.setPalestras(palestras);
            }
        });

        // Botão para tela de cadastro de projetos
        Button btnCadastroProjeto = findViewById(R.id.btnCadastroProjeto);
        btnCadastroProjeto.setOnClickListener(v ->
                startActivity(new Intent(this, ProjetoActivity.class))
        );

        // Botão para tela de administração
        Button btnAdmin = findViewById(R.id.btnAdmin);
        btnAdmin.setOnClickListener(v ->
                startActivity(new Intent(this, AdminActivity.class))
        );
    }