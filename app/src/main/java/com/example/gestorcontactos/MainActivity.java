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
import androidx.room.Room;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gestorcontactos.Adapters.ContactAdapter;
import com.example.gestorcontactos.Clases.Agenda;
import com.example.gestorcontactos.Clases.Contact;
import com.example.gestorcontactos.DB.DataBase;
import com.example.gestorcontactos.Pantallas.about_us;
import com.example.gestorcontactos.Pantallas.contactos;
import com.example.gestorcontactos.Pantallas.favorite;
import com.example.gestorcontactos.Pantallas.isEmtyContact;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener{
    Agenda agenda=Agenda.getInstance();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SearchView search;
    AlertDialog alertDialog;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(getApplicationContext(), DataBase.class, "database-name").build();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                agenda.setContacts(db.contactDao().loadAll());
            }
        });
        //agenda.setContacts(db.contactDao().loadAll());
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
        } else if (itemId == R.id.add_tag) {
            showAddTagDialog();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!agenda.getContacts().isEmpty()){
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }else{
            fragmentTransaction.replace(R.id.fragment_container, new isEmtyContact());
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.fragment_add_contact, null);
        builder.setView(dialogView);
        EditText name = dialogView.findViewById(R.id.Name);
        EditText phone = dialogView.findViewById(R.id.Phone);
        Button addButton = dialogView.findViewById(R.id.button);
        alertDialog = builder.create();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setName = name.getText().toString();
                String setPhone = phone.getText().toString();
                if (setName.isEmpty() || setPhone.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Campos requeridos", Toast.LENGTH_SHORT).show();
                } else {
                    Bitmap profileBitmap = generateProfileImage(setName);
                    String profileImageString = convertBitmapToString(profileBitmap);
                    Contact contact = new Contact(setName, setPhone);
                    contact.setImage(profileImageString);
                    agenda.addContact(contact);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            db.contactDao().insertAll(contact);
                            List<Contact> c=db.contactDao().loadAll();
                            for (Contact a:c
                                 ) {
                                System.out.println(a.toString());
                            }
                        }
                    });
                    name.setText("");
                    phone.setText("");
                    Toast.makeText(MainActivity.this, "Contacto Agregadp", Toast.LENGTH_SHORT).show();
                    replaceFragment(new contactos());
                    //alertDialog.dismiss();
                }
            }
        });
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.fondo);
        alertDialog.show();
        }
    private Bitmap generateProfileImage(String name) {
        int width = 200;
        int height = 200;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Generar un color aleatorio para el fondo
        Random random = new Random();
        int backgroundColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        canvas.drawColor(backgroundColor);

        // Configurar el pincel para dibujar las iniciales
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(80);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Obtener las iniciales del nombre
        String initials;
        if (!TextUtils.isEmpty(name)) {
            String[] nameParts = name.trim().split("\\s+");
            StringBuilder initialsBuilder = new StringBuilder();
            for (String part : nameParts) {
                if (part.length() > 0) {
                    initialsBuilder.append(part.charAt(0));
                }
            }
            initials = initialsBuilder.toString().toUpperCase();
        } else {
            initials = "";
        }

        // Dibujar las iniciales en el centro de la imagen
        int xPos = canvas.getWidth() / 2;
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        canvas.drawText(initials, xPos, yPos, textPaint);

        return bitmap;
    }

    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}



