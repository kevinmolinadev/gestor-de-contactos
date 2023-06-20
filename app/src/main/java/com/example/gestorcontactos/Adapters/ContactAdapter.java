package com.example.gestorcontactos.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.gestorcontactos.Clases.Agenda;
import com.example.gestorcontactos.Clases.Contact;
import com.example.gestorcontactos.DB.ClientDataBase;
import com.example.gestorcontactos.DB.DataBase;
import com.example.gestorcontactos.MainActivity;
import com.example.gestorcontactos.Pantallas.about_us;
import com.example.gestorcontactos.Pantallas.contactos;
import com.example.gestorcontactos.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>{
    Agenda agenda=Agenda.getInstance();
    Funciones funciones=Funciones.getInstance();
    private Context context;
    private List<Contact> contactList;
    private boolean favorite;
    AlertDialog alertDialog;


    public ContactAdapter(Context context, List<Contact> contactList,boolean favorite ) {
        this.context = context;
        this.contactList = contactList;
        this.favorite=favorite;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (!favorite){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_layout, parent, false);
            return new ContactHolder(itemView);
        }else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_layout_favorite, parent, false);
            return new ContactHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            Contact contact=contactList.get(position);
            String imageString = contact.getImage();
            Bitmap profileBitmap = convertStringToBitmap(imageString);
            holder.image.setImageBitmap(profileBitmap);
            holder.name.setText(contact.getName());
            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    String opcionSeleccionada = adapterView.getItemAtPosition(position).toString();
                    if (opcionSeleccionada.equals("Eliminar")) {
                        delete(contact);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                        //deleteContactInDatabase(contact);
                    } else if (opcionSeleccionada.equals("Actualizar")) {
                        actualizar(contact);
                        String nombreContacto = contact.getName();
                        Toast.makeText(context, "Contacto actulizado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // Maneja el evento cuando no se selecciona ninguna opción
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    View dialogView = inflater.inflate(R.layout.layout_view_contact, null);
                    builder.setView(dialogView);
                    AlertDialog alertDialog;
                    ImageFilterView image = dialogView.findViewById(R.id.image);
                    image.setImageBitmap(profileBitmap);
                    EditText nameTextView = dialogView.findViewById(R.id.Name);
                    nameTextView.setText(contact.getName());
                    nameTextView.setEnabled(false);
                    nameTextView.setFocusable(false);
                    EditText phoneTextView = dialogView.findViewById(R.id.Phone);
                    phoneTextView.setText(contact.getNumber());
                    phoneTextView.setEnabled(false);
                    phoneTextView.setFocusable(false);
                    alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawableResource(R.color.fondo);
                    alertDialog.show();
                    //Toast.makeText(context, "Tocaste el contacto: " + contact.getName(), Toast.LENGTH_SHORT).show();
                }
            });
            holder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!favorite){
                        agenda.setContactsFavorite(contact);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Contacto agregado a favoritos", Toast.LENGTH_SHORT).show();
                    }else{
                        agenda.deleteFavorite(contact);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Contacto eliminado de favoritos", Toast.LENGTH_SHORT).show();
                    }

                }
            });
    }
    @Override
    public int getItemCount() {
        return contactList.size();
    }
    static class ContactHolder extends RecyclerView.ViewHolder{
        private ImageFilterView image;
        private TextView name;
        private ImageButton favorite;
        private Spinner spinner;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.textName);
            favorite = itemView.findViewById(R.id.add_favorite);
            spinner=itemView.findViewById(R.id.spinner);
            // Agrega las opciones "Eliminar" y "Actualizar" al Spinner
            List<String> opciones = new ArrayList<>();
            opciones.add("Opciones");
            opciones.add("Eliminar");
            opciones.add("Actualizar");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, opciones);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
        }
    }
    private void delete(Contact contact){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                DataBase db = ClientDataBase.getAppDatabase(context.getApplicationContext());
                agenda.delete(contact);
                db.contactDao().deleteContact(contact);
            }
        });

    }
    public void actualizar(Contact contact){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.fragment_add_contact, null);
        builder.setView(dialogView);
        EditText name = dialogView.findViewById(R.id.Name);
        EditText phone = dialogView.findViewById(R.id.Phone);
        Button addButton = dialogView.findViewById(R.id.button);
        name.setText(contact.getName());
        phone.setText(contact.getNumber());
        alertDialog = builder.create();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setName = name.getText().toString();
                String setPhone = phone.getText().toString();
                if (setName.isEmpty() || setPhone.isEmpty()) {
                    Toast.makeText(context, "Campos requeridos", Toast.LENGTH_SHORT).show();
                } else {
                    Bitmap profileBitmap = funciones.generateProfileImage(setName);
                    String profileImageString = funciones.convertBitmapToString(profileBitmap);
                    contact.setName(setName);
                    contact.setNumber(setPhone);
                    contact.setImage(profileImageString);
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            DataBase db = ClientDataBase.getAppDatabase(context.getApplicationContext());
                            db.contactDao().update(contact);
                        }
                    });
                    name.setText("");
                    phone.setText("");
                    Toast.makeText(context, "Contacto Agregadp", Toast.LENGTH_SHORT).show();
                    //replaceFragment(new contactos());
                    alertDialog.dismiss();
                }
                notifyDataSetChanged();
            }
        });
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.fondo);
        alertDialog.show();
    }
    private Bitmap convertStringToBitmap(String imageString) {
        byte[] decodedBytes = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
