package com.example.gestorcontactos.Clases;

import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private static Agenda instance;
    private List<Contact> contacts;
    private List<Contact> contactsTag;
    private List<Tag> tags;

    private Agenda() {
        contacts = new ArrayList<>();
        contactsTag=new ArrayList<>();
        tags=new ArrayList<>();
    }

    public static Agenda getInstance() {
        if (instance == null) {
            instance = new Agenda();
        }
        return instance;
    }
    public void addContact(Contact contact) {
        contacts.add(contact);
    }
    public void addTag(Tag tag) {
        tags.add(tag);
    }


    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
    public List<Contact> getContacts() {
        return contacts;
    }
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    public List<Tag> getTags() {
        return tags;
    }
    public void setContactsTag(List<Contact> contact) {
        contactsTag=contact;
    }
    public List<Contact> getContactsFavorite() {
        return contactsTag;
    }



    public void delete(Contact contact){
        contacts.remove(contact);
    }
    public void deleteTag(Tag tag){
        tags.remove(tag);
    }
    public void deleteFavorite(Contact contact){
        contacts.remove(contact);
    }
    public void clearContacts(){
        contacts.clear();
    }
    public void clearTags(){
        tags.clear();
    }
    public void clearContactsTag(){
        contactsTag.clear();
    }
}
