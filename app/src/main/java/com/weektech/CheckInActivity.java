package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.weektech.util.SessionManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckInActivity extends AppCompatActivity {

    private TextView tvNomePalestra, tvStatusLocalizacao;
    private Button   btnConfirmarCheckin;
    private int      palestraId;
    private SessionManager session;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        session = new SessionManager(this);

        // pegando os componentes
        tvNomePalestra      = findViewById(R.id.tvNomePalestra);
        tvStatusLocalizacao = findViewById(R.id.tvStatusLocalizacao);
        btnConfirmarCheckin = findViewById(R.id.btnConfirmarCheckin);
        bottomNav           = findViewById(R.id.bottomNav);

        // configura o menu de baixo
        configurarNavegacao();

        // recupera o que foi passado por intent
        palestraId = getIntent().getIntExtra("PALESTRA_ID", -1);
        String titulo = getIntent().getStringExtra("PALESTRA_TITULO");
        tvNomePalestra.setText(titulo != null ? titulo : "Palestra");

        btnConfirmarCheckin.setOnClickListener(v -> confirmarPresenca());

        // verifica se o cabra já confirmou antes de mostrar o botão liberado
        verificarPresencaExistente();
    }

    private void configurarNavegacao() {
        // se o cara nao for admin, tira o menu de admin do rodape
        if (!session.isAdmin()) {
            Menu menu = bottomNav.getMenu();
            MenuItem adminItem = menu.findItem(R.id.nav_admin);
            if (adminItem != null) adminItem.setVisible(false);
        }

        bottomNav.setSelectedItemId(R.id.nav_schedule); // esta tela faz parte do fluxo de cronograma
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_schedule) {
                finish(); // volta para a Home/Cronograma
                return true;
            } else if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, PerfilActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_projetos) {
                startActivity(new Intent(this, ProjetoActivity.class));
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

    // verifica no banco se ja existe o checkin desse aluno nessa palestra
    private void verificarPresencaExistente() {
        String ra = session.getRa();
        if (ra.isEmpty() || palestraId == -1) return;

        AppDatabase.databaseExecutor.execute(() -> {
            InscricaoDao dao = AppDatabase.getInstance(this).inscricaoDao();
            int jaConfirmou = dao.verificarPresenca(ra, palestraId);

            if (jaConfirmou > 0) {
                runOnUiThread(() -> {
                    btnConfirmarCheckin.setEnabled(false);
                    btnConfirmarCheckin.setText("Presença Já Confirmada");
                    tvStatusLocalizacao.setText("✓ Presença confirmada anteriormente!\nRA: " + ra);
                });
            }
        });
    }

    // faz o checkin do aluno na palestra
    private void confirmarPresenca() {
        String ra = session.getRa();
        if (ra.isEmpty()) {
            Toast.makeText(this, "Você precisa estar logado para confirmar presença.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (palestraId == -1) {
            Toast.makeText(this, "Palestra não identificada.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnConfirmarCheckin.setEnabled(false);
        tvStatusLocalizacao.setText("Confirmando...");

        AppDatabase.databaseExecutor.execute(() -> {
            InscricaoDao dao = AppDatabase.getInstance(this).inscricaoDao();
            
            // pega a data e hora atual do celular
            String dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            
            // tenta marcar presenca no banco
            int rows = dao.confirmarPresenca(ra, palestraId, dataHora);

            runOnUiThread(() -> {
                if (rows > 0) {
                    btnConfirmarCheckin.setEnabled(false);
                    btnConfirmarCheckin.setText("Presença Já Confirmada");
                    tvStatusLocalizacao.setText("✓ Presença confirmada com sucesso!\nRA: " + ra);
                    Toast.makeText(this, "Presença confirmada!", Toast.LENGTH_LONG).show();
                } else {
                    btnConfirmarCheckin.setEnabled(true);
                    // se nao achou a linha é pq o cara nao se inscreveu antes
                    tvStatusLocalizacao.setText("Erro: Você deve se inscrever na palestra antes de confirmar presença.");
                    Toast.makeText(this, "Inscrição não encontrada.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
