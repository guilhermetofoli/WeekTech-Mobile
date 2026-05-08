package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.weektech.util.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUser, etPassword;
    private Button btnLogin, btnGoToRegister;
    private SessionManager session;

    // Login do adm que a gente combinou
    private static final String ADMIN_EMAIL = "admin@techweek.com";
    private static final String ADMIN_SENHA = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);

        // Se ja estiver logado, nem precisa fazer login de novo, vai direto
        if (session.isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        // Pegando as ids do layout
        etUser          = findViewById(R.id.etUser);
        etPassword      = findViewById(R.id.etPassword);
        btnLogin        = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        btnLogin.setOnClickListener(v -> realizarLogin());

        // Botao pra quem nao tem conta
        btnGoToRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void realizarLogin() {
        String email = etUser.getText().toString().trim();
        String senha = etPassword.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha tudo ai!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Primeiro ve se é o admin tentando entrar
        if (email.equals(ADMIN_EMAIL) && senha.equals(ADMIN_SENHA)) {
            session.createLoginSession(true, "admin", "Administrador", ADMIN_EMAIL, "");
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        // Valida se o email ta no formato certo
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Esse e-mail tá estranho, confere aí!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);

        // Busca no banco pra ver se o aluno existe
        AppDatabase.databaseExecutor.execute(() -> {
            Usuario usuario = AppDatabase.getInstance(this)
                    .usuarioDao()
                    .buscarPorEmailESenha(email, senha);

            runOnUiThread(() -> {
                btnLogin.setEnabled(true);
                if (usuario != null) {
                    // Salva a sessao como aluno
                    session.createLoginSession(false, usuario.ra, usuario.nome, usuario.email, usuario.curso);
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "E-mail ou senha errados!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
