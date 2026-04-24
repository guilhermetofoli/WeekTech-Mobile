package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // Declaração dos componentes do XML
    private EditText etUser, etPassword;
    private Button btnLogin, btnGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Ligar as variáveis aos IDs que criamos no XML
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        // 2. Ação do Botão ENTRAR
        btnLogin.setOnClickListener(v -> {
            String usuario = etUser.getText().toString();
            String senha = etPassword.getText().toString();

            // Validação simples (depois faremos com o Banco de Dados)
            if (usuario.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else {
                // Abre a HomeActivity
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Fecha o login para não voltar ao apertar o botão 'back'
            }
        });

        // 3. Ação do Botão "Cadastre-se"
        btnGoToRegister.setOnClickListener(v -> {
            // Abre a RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}