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

    // campos do cadastro
    private TextInputEditText etNome, etEmail, etRA, etCurso, etSerie, etSenha;
    private CheckBox cbCoffeeBreak;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // pegando as coisas do xml pelo id
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
        // pega o que o usuario digitou
        String nome   = get(etNome);
        String email  = get(etEmail);
        String ra     = get(etRA);
        String curso  = get(etCurso);
        String serie  = get(etSerie);
        String senha  = get(etSenha);

        // checa se nao deixou nada vazio
        if (nome.isEmpty() || email.isEmpty() || ra.isEmpty() || curso.isEmpty() || serie.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // valida se o email parece um email mesmo
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        // trava pra nao cadastrar RA curtinho
        if (ra.length() < 5) {
            Toast.makeText(this, "RA inválido! Deve ter no mínimo 5 dígitos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // trava de seguranca da senha
        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnCadastrar.setEnabled(false);

        AppDatabase.databaseExecutor.execute(() -> {
            UsuarioDao dao = AppDatabase.getInstance(this).usuarioDao();

            // ve se ja nao tem alguem com esse email ou RA
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

            // cria o objeto do aluno pra salvar
            Usuario usuario = new Usuario(nome, email, ra, curso, serie, senha, false);
            // se o checkbox tiver marcado, salva como true
            usuario.coffeeBreak = cbCoffeeBreak != null && cbCoffeeBreak.isChecked();

            long id = dao.inserir(usuario);
            runOnUiThread(() -> {
                btnCadastrar.setEnabled(true);
                if (id > 0) {
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    
                    // ja loga o cara pra ele nao ter que digitar tudo de novo
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

    // atalho pra pegar o texto e tirar os espaços sobrando
    private String get(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
