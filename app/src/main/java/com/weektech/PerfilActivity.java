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

        // pegando as coisas da tela
        TextView tvNome  = findViewById(R.id.tvPerfilNome);
        TextView tvEmail = findViewById(R.id.tvPerfilEmail);
        TextView tvRa    = findViewById(R.id.tvPerfilRa);
        TextView tvCurso = findViewById(R.id.tvPerfilCurso);
        Button   btnSair = findViewById(R.id.btnSair);
        Button   btnFaq  = findViewById(R.id.btnFaq);
        bottomNav        = findViewById(R.id.bottomNav);

        // coloca os dados do usuario logado nos campos
        tvNome.setText(session.getNome());
        tvEmail.setText(session.getEmail());
        tvRa.setText("RA: " + session.getRa());
        tvCurso.setText(session.getCurso());

        // deixa o icone de perfil selecionado no menu
        bottomNav.setSelectedItemId(R.id.nav_perfil);

        // vai pra tela de ajuda
        btnFaq.setOnClickListener(v -> {
            Intent intent = new Intent(this, FaqActivity.class);
            startActivity(intent);
        });

        // desloga o usuario e volta pro login
        btnSair.setOnClickListener(v -> {
            session.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            // limpa a pilha de telas pra nao voltar pra ca no botao back
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // trata a navegacao do menu de baixo
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
                return true; // ja ta aqui
            }
            return false;
        });
    }
}
