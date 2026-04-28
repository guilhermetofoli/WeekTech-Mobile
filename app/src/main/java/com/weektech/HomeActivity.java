package com.weektech;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

/**
 * Tela principal do app
 * Responsável por listar as palestras e navegar entre telas
 */

public class HomeActivity extends AppCompatActivity
        implements PalestraAdapter.OnItemClickListener {

    private RecyclerView rvPalestras;
    private PalestraAdapter adapter;
    private PalestraDao palestraDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);