package com.example.gestorcontactos.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.gestorcontactos.Clases.Contact;

@Database(entities = {Contact.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract ContactDao contactDao();
}
