package com.weektech;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListaAlunosActivity extends AppCompatActivity {

    private RecyclerView rvAlunos;
    private UsuarioAdapter adapter;
    private AppDatabase db;
    private List<Usuario> todosUsuarios = new ArrayList<>();
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        db = AppDatabase.getInstance(this);
        rvAlunos = findViewById(R.id.rvAlunos);
        tabLayout = findViewById(R.id.tabLayoutAlunos);

        rvAlunos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsuarioAdapter();
        rvAlunos.setAdapter(adapter);

        findViewById(R.id.toolbarAlunos).setOnClickListener(v -> finish());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filtrarLista(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        carregarAlunos();
    }

    private void carregarAlunos() {
        AppDatabase.databaseExecutor.execute(() -> {
            todosUsuarios = db.usuarioDao().listarTodos();
            runOnUiThread(() -> filtrarLista(tabLayout.getSelectedTabPosition()));
        });
    }

    private void filtrarLista(int position) {
        List<Usuario> filtrados;
        if (position == 1) { // Coffee Break
            filtrados = todosUsuarios.stream()
                    .filter(u -> u.coffeeBreak)
                    .collect(Collectors.toList());
        } else {
            filtrados = todosUsuarios;
        }
        adapter.setUsuarios(filtrados);
    }
}
