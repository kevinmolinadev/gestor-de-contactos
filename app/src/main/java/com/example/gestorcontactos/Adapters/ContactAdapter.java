package com.example.gestorcontactos.Adapters;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.gestorcontactos.Clases.ContactTag;
import com.example.gestorcontactos.Clases.Tag;
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
    AlertDialog alertDialog;
    public boolean tag;

    public ContactAdapter(Context context, List<Contact> contactList, boolean tag) {
        this.context = context;
        this.contactList = contactList;
        this.tag=tag;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(!tag){
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
            Bitmap profileBitmap = funciones.convertStringToBitmap(imageString);
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
                    } else if (opcionSeleccionada.equals("Editar")) {
                        actualizar(contact);
                    }
                    holder.spinner.setSelection(0,false);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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
                }
            });
        holder.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tag){
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    View dialogView = inflater.inflate(R.layout.tag_selection, null);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    RadioGroup radioGroup = dialogView.findViewById(R.id.radio_group);

                    for (Tag opcion : agenda.getTags()) {
                        RadioButton radioButton = new RadioButton(dialogView.getContext());
                        radioButton.setText(opcion.getName());
                        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20); // Establecer tamaño del texto en 20dp
                        radioGroup.addView(radioButton);

                        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    try {
                                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    DataBase db = ClientDataBase.getAppDatabase(context);
                                                    ContactTag contactTag = new ContactTag(contact.getId(), opcion.getId());
                                                    db.CTDao().insertAll(contactTag);
                                                } catch (SQLiteConstraintException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }catch (Exception exception){
                                        System.out.println(exception);
                                    }
                                    Toast.makeText(context,"Contacto agregado a "+opcion.getName(),Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }
                    alertDialog.getWindow().setBackgroundDrawableResource(R.color.fondo);
                    alertDialog.show();
                }else{
                    agenda.clearContactsTag();
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                DataBase db = ClientDataBase.getAppDatabase(context.getApplicationContext());
                                int idtag=db.CTDao().getTagIdByContactName(contact.getName());
                                int idContactTag=db.CTDao().getIdByContactName_TagId(contact.getName(),idtag);
                                ContactTag contactTag = new ContactTag(contact.getId(), idtag);
                                contactTag.setId(idContactTag);
                                db.CTDao().deleteContactTag(contactTag);
                            }catch (SQLiteConstraintException e){
                                e.printStackTrace();
                            }
                        }
                    });
                    notifyDataSetChanged();
                    Toast.makeText(context,"Contacto eliminado",Toast.LENGTH_SHORT).show();
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
        private ImageButton tag;
        private Spinner spinner;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.textName);
            tag = itemView.findViewById(R.id.add_to_tag);
            spinner=itemView.findViewById(R.id.spinner);
            // Agrega las opciones "Eliminar" y "Actualizar" al Spinner
            List<String> opciones = new ArrayList<>();
            opciones.add("Opciones");
            opciones.add("Editar");
            opciones.add("Eliminar");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, opciones);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
        }
    }
    private void delete(Contact contact){
        try {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        DataBase db = ClientDataBase.getAppDatabase(context.getApplicationContext());
                        agenda.delete(contact);
                        db.contactDao().deleteContact(contact);
                        int idtag=db.CTDao().getTagIdByContactName(contact.getName());
                        int idContactTag=db.CTDao().getIdByContactName_TagId(contact.getName(),idtag);
                        ContactTag contactTag = new ContactTag(contact.getId(), idtag);
                        contactTag.setId(idContactTag);
                        db.CTDao().deleteContactTag(contactTag);
                    }catch (SQLiteConstraintException e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception exception){
            System.out.println(exception);
        }

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
                    try {
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    DataBase db = ClientDataBase.getAppDatabase(context.getApplicationContext());
                                    db.contactDao().update(contact);
                                }catch (SQLiteConstraintException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }catch (Exception exception){
                        System.out.println(exception);
                    }
                    name.setText("");
                    phone.setText("");
                    Toast.makeText(context, "Contacto actualizado", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
                notifyDataSetChanged();
            }
        });
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.fondo);
        alertDialog.show();
    }
}
