package com.example.gestorcontactos.Clases;

import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private static Agenda instance;
    private List<Contact> contacts;
    private List<Contact> contactsFavorite;

    private Agenda() {
        contacts = new ArrayList<>();
        contactsFavorite=new ArrayList<>();
    }

    public static Agenda getInstance() {
        if (instance == null) {
            instance = new Agenda();
        }
        return instance;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public List<Contact> getContactsFavorite() {
        return contactsFavorite;
    }

    public void setContactsFavorite(Contact contact) {
        contactsFavorite.add(contact);
    }
    public void delete(Contact contact){
        contacts.remove(contact);
    }
    public void deleteFavorite(Contact contact){
        contactsFavorite.remove(contact);
    }
    public void clearContacts(){
        contacts.clear();
    }
}
