package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.weektech.util.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etNome, etEmail, etRA, etCurso, etSerie, etSenha;
    private CheckBox cbCoffeeBreak;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNome       = findViewById(R.id.etNomeRegister);
        etEmail      = findViewById(R.id.etEmailRegister);
        etRA         = findViewById(R.id.etRARegister);
        etCurso      = findViewById(R.id.etCursoRegister);
        etSerie      = findViewById(R.id.etSerieRegister);
        etSenha      = findViewById(R.id.etPasswordRegister);
        cbCoffeeBreak= findViewById(R.id.cbCoffeeBreak);
        btnCadastrar = findViewById(R.id.btnFinalizeRegister);

        btnCadastrar.setOnClickListener(v -> realizarCadastro());
    }

    private void realizarCadastro() {
        String nome   = get(etNome);
        String email  = get(etEmail);
        String ra     = get(etRA);
        String curso  = get(etCurso);
        String serie  = get(etSerie);
        String senha  = get(etSenha);

        // Validações
        if (nome.isEmpty() || email.isEmpty() || ra.isEmpty() || curso.isEmpty() || serie.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ra.length() < 5) {
            Toast.makeText(this, "RA inválido! Deve ter no mínimo 5 dígitos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnCadastrar.setEnabled(false);

        AppDatabase.databaseExecutor.execute(() -> {
            UsuarioDao dao = AppDatabase.getInstance(this).usuarioDao();

            // Verifica duplicatas
            if (dao.verificarEmailExistente(email) > 0) {
                runOnUiThread(() -> {
                    btnCadastrar.setEnabled(true);
                    Toast.makeText(this, "Este e-mail já está cadastrado!", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            if (dao.verificarRaExistente(ra) > 0) {
                runOnUiThread(() -> {
                    btnCadastrar.setEnabled(true);
                    Toast.makeText(this, "Este RA já está cadastrado!", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Cria e salva o usuário
            Usuario usuario = new Usuario(nome, email, ra, curso, serie, senha, false);
            usuario.coffeeBreak = cbCoffeeBreak != null && cbCoffeeBreak.isChecked();

            long id = dao.inserir(usuario);
            runOnUiThread(() -> {
                btnCadastrar.setEnabled(true);
                if (id > 0) {
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    // Loga automaticamente após cadastro
                    SessionManager session = new SessionManager(this);
                    session.createLoginSession(false, ra, nome, email, curso);
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Erro ao realizar cadastro.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private String get(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
