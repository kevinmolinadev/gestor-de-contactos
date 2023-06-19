package com.example.gestorcontactos.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.gestorcontactos.Clases.Contact;
import com.example.gestorcontactos.DAO.ContactoDAO;

@Database(entities = {Contact.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactoDAO contactoDAO();
}
