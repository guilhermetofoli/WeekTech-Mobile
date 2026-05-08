package com.weektech;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class FaqActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // botao pra fechar a tela de duvidas
        Button btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // volta se apertar o botao de voltar do celular
    }
}
