package com.weektech;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.weektech.util.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity 
        implements PalestraAdapter.OnPalestraClickListener, ProjetoAdapter.OnProjetoClickListener {

    private TextView tvDataSelecionada, tvSemAtividades;
    private RecyclerView rvPalestras, rvProjetos;
    private PalestraAdapter palestraAdapter;
    private ProjetoAdapter projetoAdapter;
    private BottomNavigationView bottomNav;
    private SessionManager session;
    private PalestraDao palestraDao;
    private ProjetoDao projetoDao;
    private InscricaoDao inscricaoDao;
    
    private String dataAtualFormatada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new SessionManager(this);
        palestraDao = AppDatabase.getInstance(this).palestraDao();
        projetoDao = AppDatabase.getInstance(this).projetoDao();
        inscricaoDao = AppDatabase.getInstance(this).inscricaoDao();

        tvDataSelecionada = findViewById(R.id.tvDataSelecionada);
        tvSemAtividades = findViewById(R.id.tvSemAtividades);
        rvPalestras = findViewById(R.id.rvPalestrasCronograma);
        rvProjetos = findViewById(R.id.rvProjetosCronograma);
        bottomNav = findViewById(R.id.bottomNav);

        rvPalestras.setLayoutManager(new LinearLayoutManager(this));
        rvProjetos.setLayoutManager(new LinearLayoutManager(this));

        palestraAdapter = new PalestraAdapter(this, this);
        palestraAdapter.setAdmin(session.isAdmin());
        rvPalestras.setAdapter(palestraAdapter);

        projetoAdapter = new ProjetoAdapter(this);
        rvProjetos.setAdapter(projetoAdapter);

        // Define a data de hoje como padrão
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dataAtualFormatada = sdf.format(new Date());
        tvDataSelecionada.setText(dataAtualFormatada);

        findViewById(R.id.cardSelecionarData).setOnClickListener(v -> abrirCalendario());

        configurarNavegacao();
        carregarCronograma(dataAtualFormatada);
    }

    private void abrirCalendario() {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date d = sdf.parse(dataAtualFormatada);
            if (d != null) calendar.setTime(d);
        } catch (Exception ignored) {}

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this,
                R.style.CustomDatePickerDialog,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    dataAtualFormatada = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    tvDataSelecionada.setText(dataAtualFormatada);
                    carregarCronograma(dataAtualFormatada);
                }, year, month, day);

        datePicker.show();
    }

    private void carregarCronograma(String data) {
        // Busca Palestras
        palestraDao.listarPorData(data).observe(this, palestras -> {
            if (palestras == null) palestras = new ArrayList<>();
            
            List<Palestra> listaFinal = palestras;
            AppDatabase.databaseExecutor.execute(() -> {
                String ra = session.getRa();
                for (Palestra p : listaFinal) {
                    boolean inscrito = inscricaoDao.verificarDuplicata(ra, p.id) > 0;
                    if (inscrito) {
                        p.statusInscricao = Palestra.StatusInscricao.INSCRITO;
                    } else if (!p.ativa) {
                        p.statusInscricao = Palestra.StatusInscricao.VISUALIZAR;
                    } else {
                        p.statusInscricao = Palestra.StatusInscricao.DISPONIVEL;
                    }
                }
                runOnUiThread(() -> {
                    palestraAdapter.setPalestras(listaFinal);
                    verificarVisibilidadeSemAtividades();
                });
            });
        });

        // Busca Projetos
        AppDatabase.databaseExecutor.execute(() -> {
            List<Projeto> todos = projetoDao.listarAgendados();
            List<Projeto> filtrados = new ArrayList<>();
            for (Projeto p : todos) {
                if (data.equals(p.dataApresentacao)) {
                    filtrados.add(p);
                }
            }
            runOnUiThread(() -> {
                projetoAdapter.setProjetos(filtrados);
                verificarVisibilidadeSemAtividades();
            });
        });
    }

    private void verificarVisibilidadeSemAtividades() {
        boolean temPalestras = palestraAdapter.getItemCount() > 0;
        boolean temProjetos = projetoAdapter.getItemCount() > 0;

        findViewById(R.id.tvTituloCronograma).setVisibility(temPalestras ? View.VISIBLE : View.GONE);
        findViewById(R.id.tvTituloProjetosCronograma).setVisibility(temProjetos ? View.VISIBLE : View.GONE);
        tvSemAtividades.setVisibility(!temPalestras && !temProjetos ? View.VISIBLE : View.GONE);
    }

    private void configurarNavegacao() {
        if (!session.isAdmin()) {
            Menu menu = bottomNav.getMenu();
            MenuItem adminItem = menu.findItem(R.id.nav_admin);
            if (adminItem != null) adminItem.setVisible(false);
        }

        bottomNav.setSelectedItemId(R.id.nav_schedule);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_schedule) return true;
            if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, PerfilActivity.class));
                return true;
            }
            if (id == R.id.nav_projetos) {
                startActivity(new Intent(this, ProjetoActivity.class));
                return true;
            }
            if (id == R.id.nav_admin) {
                if (session.isAdmin()) startActivity(new Intent(this, AdminActivity.class));
                return true;
            }
            return false;
        });
    }

    // Callbacks de Palestra
    @Override
    public void onInscreverClick(Palestra palestra, int position) {
        String ra = session.getRa();
        if (ra.isEmpty()) {
            Toast.makeText(this, "Faça login para se inscrever.", Toast.LENGTH_SHORT).show();
            return;
        }
        AppDatabase.databaseExecutor.execute(() -> {
            if (inscricaoDao.verificarDuplicata(ra, palestra.id) == 0) {
                inscricaoDao.inserir(new Inscricao(ra, palestra.id));
                runOnUiThread(() -> {
                    palestraAdapter.atualizarStatus(position, Palestra.StatusInscricao.INSCRITO);
                    Toast.makeText(this, "Inscrição realizada!", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override public void onInscritoClick(Palestra p, int pos) { onCardClick(p, pos); }
    @Override public void onVisualizarClick(Palestra p, int pos) { onCardClick(p, pos); }
    @Override public void onCardClick(Palestra p, int pos) {
        Intent intent = new Intent(this, CheckInActivity.class);
        intent.putExtra("PALESTRA_ID", p.id);
        intent.putExtra("PALESTRA_TITULO", p.titulo);
        startActivity(intent);
    }
    @Override public void onVisualizarPresencasClick(Palestra p) {
        Intent intent = new Intent(this, PresencasActivity.class);
        intent.putExtra("palestra_id", p.id);
        startActivity(intent);
    }

    // Callback de Projeto
    @Override public void onDeleteClick(Projeto p) {
        // Aluno deletando projeto reprovado
        new AlertDialog.Builder(this)
                .setTitle("Remover Projeto")
                .setMessage("Deseja remover este projeto?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    AppDatabase.databaseExecutor.execute(() -> {
                        projetoDao.deletarPorId(p.id);
                        runOnUiThread(() -> carregarCronograma(dataAtualFormatada));
                    });
                })
                .setNegativeButton("Não", null)
                .show();
    }
}
