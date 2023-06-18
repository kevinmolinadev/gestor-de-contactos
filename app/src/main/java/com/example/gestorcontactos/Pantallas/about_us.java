package com.example.gestorcontactos.Pantallas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gestorcontactos.R;
import com.squareup.picasso.Picasso;

public class about_us extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        ImageView imageView = view.findViewById(R.id.profile1);
        Picasso.get().load("https://avatars.githubusercontent.com/u/112907401?v=4").into(imageView);
        ImageView imageView2 = view.findViewById(R.id.profile2);
        Picasso.get().load("https://avatars.githubusercontent.com/u/120534154?v=4").into(imageView2);
        return view;

    }
}