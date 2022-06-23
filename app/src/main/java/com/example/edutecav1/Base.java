package com.example.edutecav1;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class Base extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Menu menu;
    MenuItem itemBR, itemEN, itemES, itemLogin;
    SessionUser sessionUser;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = drawerLayout.findViewById(R.id.frameLayout);
        frameLayout.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menuOpen, R.string.menuClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        menu = navigationView.getMenu();

        itemBR = findViewById(R.id.nav_flag_br);
        itemES = findViewById(R.id.nav_flag_es);
        itemEN = findViewById(R.id.nav_flag_us);
        itemLogin = findViewById(R.id.nav_login);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch ((item.getItemId())) {
            case R.id.nav_flag_br:
                definirIdioma("pt");
                recreate();
                break;
        }

        switch ((item.getItemId())) {
            case R.id.nav_flag_es:
                definirIdioma("es");
                recreate();
                break;
        }

        switch ((item.getItemId())) {
            case R.id.nav_flag_us:
                definirIdioma("en");
                recreate();
                break;
        }


        switch ((item.getItemId())) {
            case R.id.nav_about:
                startActivity(new Intent(this, About.class));
                overridePendingTransition(0, 0);
                break;
        }

        switch ((item.getItemId())) {
            case R.id.nav_perfil:
                startActivity(new Intent(this, Perfil.class));
                //overridePendingTransition(0, 0);
                break;
        }


        switch ((item.getItemId())) {
            case R.id.nav_login:
                startActivity(new Intent(this, Login.class));
                overridePendingTransition(0,0);
                break;
        }

        switch ((item.getItemId())) {
            case R.id.nav_register:

                startActivity(new Intent(this, CreateUser.class));
                overridePendingTransition(0,0);
                break;
        }

        switch ((item.getItemId())) {
            case R.id.nav_logout:
                sessionUser.logout();
                //startActivity(new Intent(this, Main.class));
                //recreate();
                break;
        }

        switch ((item.getItemId())) {
            case R.id.nav_leave:
                finishAffinity();
                break;
        }
        return false;
    }

    protected void alocarTitulo(String tituloActivity) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(tituloActivity);
        }
    }

    public void definirIdioma(String idiomaAtual) {
        Locale localidade = new Locale(idiomaAtual); //Instanciar um novo Locale a partir da propriedade enviada em método.
        Locale.setDefault(localidade); //Definir Default de acordo seleção do idioma.
        Resources resources = this.getResources(); //Passar contexto da classe e capturar recurso strings.xml.
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(localidade);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics()); //Atualizar resources e configuration em base do novo idioma.
        SharedPreferences.Editor editor = getSharedPreferences("chaveLocale", MODE_PRIVATE).edit();
        editor.putString("chaveIdioma", idiomaAtual);
        editor.apply();
    }

    //Método para carregar o último idioma definido, logo, esse método deve ser chamado no início do onCreate da class MainActivity
    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("chaveLocale", MODE_PRIVATE);
        String lingua = preferences.getString("chaveIdioma", "");
        definirIdioma(lingua);
    }
}