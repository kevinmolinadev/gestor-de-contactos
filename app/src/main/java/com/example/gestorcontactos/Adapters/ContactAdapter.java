package com.example.gestorcontactos.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestorcontactos.Clases.Agenda;
import com.example.gestorcontactos.Clases.Contact;
import com.example.gestorcontactos.MainActivity;
import com.example.gestorcontactos.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>{
    Agenda agenda=Agenda.getInstance();
    private Context context;
    private List<Contact> contactList;
    private boolean favorite;

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View dialogView = inflater.inflate(R.layout.fragment_add_contact, null);
                builder.setView(dialogView);
                AlertDialog alertDialog;
                //ImageFilterView image = dialogView.findViewById(R.id.image);
                //image.setImageBitmap(profileBitmap);
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

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.textName);
            favorite = itemView.findViewById(R.id.add_favorite);
        }
    }
    private Bitmap convertStringToBitmap(String imageString) {
        byte[] decodedBytes = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
