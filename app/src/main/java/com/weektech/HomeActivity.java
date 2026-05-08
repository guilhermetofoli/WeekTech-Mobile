package com.weektech;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.weektech.util.SessionManager;

public class HomeActivity extends AppCompatActivity {

    private TabLayout            tabLayout;
    private ViewPager2           viewPager;
    private BottomNavigationView bottomNav;
    private SessionManager       session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session   = new SessionManager(this);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        bottomNav = findViewById(R.id.bottomNav);

        // configura o adapter das abas dos dias
        DiasPagerAdapter pagerAdapter = new DiasPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // coloca o texto de cada aba
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->
                tab.setText("Dia " + (position + 1))).attach();

        // se o cara nao for admin, tira o menu de admin do rodape
        if (!session.isAdmin()) {
            Menu menu = bottomNav.getMenu();
            MenuItem adminItem = menu.findItem(R.id.nav_admin);
            if (adminItem != null) adminItem.setVisible(false);
        }

        // trata os cliques no menu de baixo
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_schedule) {
                return true; // ja ta aqui
            } else if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, PerfilActivity.class));
                return true;
            } else if (id == R.id.nav_projetos) {
                startActivity(new Intent(this, ProjetoActivity.class));
                return true;
            } else if (id == R.id.nav_admin) {
                // so entra se for admin mesmo
                if (session.isAdmin()) {
                    startActivity(new Intent(this, AdminActivity.class));
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Acesso Restrito")
                            .setMessage("Área exclusiva para administradores.")
                            .setPositiveButton("OK", null)
                            .show();
                }
                return true;
            }
            return false;
        });
    }
}
