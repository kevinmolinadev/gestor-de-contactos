package com.example.gestorcontactos.Pantallas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gestorcontactos.Adapters.ContactAdapter;
import com.example.gestorcontactos.Clases.Agenda;
import com.example.gestorcontactos.Clases.Contact;
import com.example.gestorcontactos.R;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

public class add_contact extends Fragment {
    Agenda agenda = Agenda.getInstance();

    EditText name;
    Contact contact;
    EditText phone;
    ContactAdapter contactAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        //contactAdapter = new  ContactAdapter(getContext(),agenda.getContacts(),false);
        name = view.findViewById(R.id.Name);
        phone = view.findViewById(R.id.Phone);
        Button addButton = view.findViewById(R.id.button);
        agenda.clearContacts();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setName = name.getText().toString();
                String setPhone = phone.getText().toString();
                if (setName.isEmpty() || setPhone.isEmpty()) {
                    Toast.makeText(getContext(), "Campos requeridos", Toast.LENGTH_SHORT).show();
                }else {
                    Bitmap profileBitmap = generateProfileImage(setName);
                    String profileImageString = convertBitmapToString(profileBitmap);
                    Contact contact = new Contact(setName, setPhone);
                    contact.setImage(profileImageString);
                    //insertContactInDatabase(contact);
                    agenda.addContact(contact);
                    name.setText("");
                    phone.setText("");
                    Toast.makeText(getContext(), "Contacto agregado con éxito", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
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