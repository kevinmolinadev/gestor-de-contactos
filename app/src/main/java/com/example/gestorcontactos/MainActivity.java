package com.example.gestorcontactos;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.example.gestorcontactos.Pantallas.about_us;
import com.example.gestorcontactos.Pantallas.contactos;
import com.example.gestorcontactos.Pantallas.favorite;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DrawerLayout drawerLayout;
        BottomNavigationView bottomNavigationView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new contactos()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        replaceFragment(new contactos());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            /*switch (item.getItemId()) {
                case R.id.contact:
                    replaceFragment(new contactos());
                    break;
                case R.id.favorite:
                    replaceFragment(new favorite());
                    break;
                case R.id.about_us:
                    replaceFragment(new about_us());
                    break;
            }*/
            int itemId = item.getItemId();
            if (itemId == R.id.contact) {
                replaceFragment(new contactos());
            } else if (itemId == R.id.favorite) {
                replaceFragment(new favorite());
            } else if (itemId == R.id.about_us) {
                replaceFragment(new about_us());
            }
            return true;
        });
    }
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}
