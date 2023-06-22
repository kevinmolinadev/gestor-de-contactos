package com.example.gestorcontactos.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.gestorcontactos.Clases.Contact;
import com.example.gestorcontactos.Clases.ContactTag;
import com.example.gestorcontactos.Clases.Tag;

@Database(entities = {Contact.class, Tag.class, ContactTag.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract TagDao tagDao();
    public abstract ContactTagDao CTDao();
}
