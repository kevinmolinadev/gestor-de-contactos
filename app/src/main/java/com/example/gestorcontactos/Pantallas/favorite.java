package com.example.gestorcontactos.Pantallas;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestorcontactos.Adapters.ContactAdapter;
import com.example.gestorcontactos.Clases.Agenda;
import com.example.gestorcontactos.Clases.Contact;
import com.example.gestorcontactos.R;

public class favorite extends Fragment {
    Agenda agenda = Agenda.getInstance();
    RecyclerView recyclerView;
    ContactAdapter  contactAdapter;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.dataContact);
        initAdapter();
        return view;
    }
    public void initAdapter(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactAdapter = new ContactAdapter(getContext(),agenda.getContactsFavorite(),true);
        recyclerView.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();
    }
}