package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.weektech.util.SessionManager;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        SessionManager session = new SessionManager(this);

        TextView tvNome  = findViewById(R.id.tvPerfilNome);
        TextView tvEmail = findViewById(R.id.tvPerfilEmail);
        TextView tvRa    = findViewById(R.id.tvPerfilRa);
        TextView tvCurso = findViewById(R.id.tvPerfilCurso);
        Button   btnSair = findViewById(R.id.btnSair);
        Button   btnFaq  = findViewById(R.id.btnFaq);

        tvNome.setText(session.getNome());
        tvEmail.setText(session.getEmail());
        tvRa.setText("RA: " + session.getRa());
        tvCurso.setText(session.getCurso());

        btnFaq.setOnClickListener(v ->
                startActivity(new Intent(this, FaqActivity.class)));

        btnSair.setOnClickListener(v -> {
            session.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
