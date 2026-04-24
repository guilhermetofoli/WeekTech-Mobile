package com.weektech;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView rv = findViewById(R.id.rvPalestras);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<Palestra> lista = new ArrayList<>();
        lista.add(new Palestra("React para Iniciantes", "Palestrante 01", "23/04 - 19:30", "Auditório 1", R.drawable.ic_launcher_background));
        lista.add(new Palestra("O Futuro do Desenvolvimento", "Palestrante 02", "24/04 - 20:00", "Lab 02", R.drawable.ic_launcher_background));

        PalestraAdapter adapter = new PalestraAdapter(lista);
        rv.setAdapter(adapter);
    }
}