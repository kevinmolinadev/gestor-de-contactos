package com.example.gestorcontactos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.gestorcontactos.Pantallas.about_us;
import com.example.gestorcontactos.Pantallas.add_contact;
import com.example.gestorcontactos.Pantallas.contactos;
import com.example.gestorcontactos.Pantallas.favorite;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SearchView search;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Inicio();
        busqueda();
    }
    public void Inicio() {
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
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.contact) {
            replaceFragment(new contactos());
        } else if (itemId == R.id.favorite) {
            replaceFragment(new favorite());
        } else if (itemId == R.id.about_us) {
            getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout, new about_us()).commit();
        } else if (itemId == R.id.casa) {
            Toast.makeText(this, "Casa", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.universidad) {
            Toast.makeText(this, "Universidad", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.add_tag) {
            showAddTagDialog();
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
    private void showAddTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.layout_tag, null);
        builder.setView(dialogView);

        EditText tagNameEditText = dialogView.findViewById(R.id.tag_name);

        Button addButton = dialogView.findViewById(R.id.new_tag);
        alertDialog = builder.create();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setname = tagNameEditText.getText().toString();
                Menu menu = navigationView.getMenu();
                MenuItem item=menu.add(R.id.group_main, Menu.NONE, Menu.NONE, setname);
                item.setIcon(R.drawable.tag);
                Toast.makeText(MainActivity.this, "Sección agregada", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.fondo);
        alertDialog.show();
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

    public void closeTag(View view) {
        alertDialog.dismiss();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.drawer_layout);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            replaceFragment(new contactos());
            navigationView.setCheckedItem(R.id.contact);
        }
    }
    public void close(View view){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.drawer_layout);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            replaceFragment(new contactos());
            navigationView.setCheckedItem(R.id.contact);
        }
    }
    public void new_contact(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout, new add_contact()).commit();
    }

}


