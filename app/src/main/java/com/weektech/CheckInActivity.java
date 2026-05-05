package com.weektech;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.weektech.util.SessionManager;

public class CheckInActivity extends AppCompatActivity {

    private TextView tvNomePalestra, tvStatusLocalizacao;
    private Button   btnConfirmarCheckin;
    private int      palestraId;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        session = new SessionManager(this);

        tvNomePalestra      = findViewById(R.id.tvNomePalestra);
        tvStatusLocalizacao = findViewById(R.id.tvStatusLocalizacao);
        btnConfirmarCheckin = findViewById(R.id.btnConfirmarCheckin);

        palestraId = getIntent().getIntExtra("PALESTRA_ID", -1);
        String titulo = getIntent().getStringExtra("PALESTRA_TITULO");
        tvNomePalestra.setText(titulo != null ? titulo : "Palestra");

        btnConfirmarCheckin.setOnClickListener(v -> confirmarPresenca());
    }

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
        tvStatusLocalizacao.setText("Verificando...");

        AppDatabase.databaseExecutor.execute(() -> {
            InscricaoDao dao = AppDatabase.getInstance(this).inscricaoDao();

            // Verifica se já está inscrito antes de confirmar
            boolean jaInscrito = dao.verificarDuplicata(ra, palestraId) > 0;

            runOnUiThread(() -> {
                btnConfirmarCheckin.setEnabled(true);
                if (jaInscrito) {
                    tvStatusLocalizacao.setText("✓ Presença confirmada com sucesso!\nRA: " + ra);
                    Toast.makeText(this, "Presença confirmada!", Toast.LENGTH_LONG).show();
                } else {
                    tvStatusLocalizacao.setText("Você não está inscrito nesta palestra.\nInscreva-se primeiro na tela principal.");
                    Toast.makeText(this, "Inscrição não encontrada.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
