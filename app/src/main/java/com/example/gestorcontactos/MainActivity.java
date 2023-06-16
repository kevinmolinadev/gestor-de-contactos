package com.example.gestorcontactos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.gestorcontactos.Pantallas.about_us;
import com.example.gestorcontactos.Pantallas.contactos;
import com.example.gestorcontactos.Pantallas.favorite;
import com.example.gestorcontactos.Pantallas.tag;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    //BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Inicio();
        busqueda();
    }
    public void Inicio() {
        //bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new contactos());
        navigationView.setCheckedItem(R.id.contact);
        //replaceFragment(new contactos());
        /*bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.contact) {
                replaceFragment(new contactos());
            } else if (itemId == R.id.favorite) {
                replaceFragment(new favorite());
            }
            return true;
        });*/
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.contact) {
            replaceFragment(new contactos());
        } else if (itemId == R.id.favorite) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new favorite()).commit();
        } else if (itemId == R.id.about_us) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new about_us()).commit();
        } else if (itemId == R.id.casa) {
            Toast.makeText(this, "Casa", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.universidad) {
            Toast.makeText(this, "Universidad", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.add_tag) {
            getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout, new tag()).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void busqueda() {
        search = findViewById(R.id.search);
        search.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void tag_close(View view) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.drawer_layout);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            replaceFragment(new contactos());
            navigationView.setCheckedItem(R.id.contact);

        }
    }
    public void add_contact(View view){
        Toast.makeText(this, "Agregar contacto", Toast.LENGTH_SHORT).show();
    }
}


