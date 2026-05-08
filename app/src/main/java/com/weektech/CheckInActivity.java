package com.weektech;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.weektech.util.SessionManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        // pegando os componentes
        tvNomePalestra      = findViewById(R.id.tvNomePalestra);
        tvStatusLocalizacao = findViewById(R.id.tvStatusLocalizacao);
        btnConfirmarCheckin = findViewById(R.id.btnConfirmarCheckin);

        // recupera o que foi passado por intent
        palestraId = getIntent().getIntExtra("PALESTRA_ID", -1);
        String titulo = getIntent().getStringExtra("PALESTRA_TITULO");
        tvNomePalestra.setText(titulo != null ? titulo : "Palestra");

        btnConfirmarCheckin.setOnClickListener(v -> confirmarPresenca());
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
                btnConfirmarCheckin.setEnabled(true);
                if (rows > 0) {
                    tvStatusLocalizacao.setText("✓ Presença confirmada com sucesso!\nRA: " + ra);
                    Toast.makeText(this, "Presença confirmada!", Toast.LENGTH_LONG).show();
                } else {
                    // se nao achou a linha é pq o cara nao se inscreveu antes
                    tvStatusLocalizacao.setText("Erro: Você deve se inscrever na palestra antes de confirmar presença.");
                    Toast.makeText(this, "Inscrição não encontrada.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
