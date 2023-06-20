package com.example.gestorcontactos.Pantallas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gestorcontactos.R;
import com.squareup.picasso.Picasso;

public class isEmtyContact extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_is_emty_contact, container, false);
        ImageView imageView2 = view.findViewById(R.id.image);
        Picasso.get().load("https://static.wikia.nocookie.net/sonicpokemon/images/b/b2/Psyduck_AG_anime.png").into(imageView2);
        return  view;
    }
}