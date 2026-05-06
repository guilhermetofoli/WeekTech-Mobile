package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.weektech.util.SessionManager;

public class PerfilActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private SessionManager       session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        session = new SessionManager(this);

        TextView tvNome  = findViewById(R.id.tvPerfilNome);
        TextView tvEmail = findViewById(R.id.tvPerfilEmail);
        TextView tvRa    = findViewById(R.id.tvPerfilRa);
        TextView tvCurso = findViewById(R.id.tvPerfilCurso);
        Button   btnSair = findViewById(R.id.btnSair);
        Button   btnFaq  = findViewById(R.id.btnFaq);
        bottomNav        = findViewById(R.id.bottomNav);

        tvNome.setText(session.getNome());
        tvEmail.setText(session.getEmail());
        tvRa.setText("RA: " + session.getRa());
        tvCurso.setText(session.getCurso());

        bottomNav.setSelectedItemId(R.id.nav_perfil);

        btnFaq.setOnClickListener(v -> {
            Intent intent = new Intent(this, FaqActivity.class);
            startActivity(intent);
        });

        btnSair.setOnClickListener(v -> {
            session.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_schedule) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_projetos) {
                startActivity(new Intent(this, ProjetoActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_perfil) {
                return true;
            }
            return false;
        });
    }
}