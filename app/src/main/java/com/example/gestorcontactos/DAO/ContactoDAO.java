package com.example.gestorcontactos.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gestorcontactos.Clases.Contact;

import java.util.List;

@Dao
public interface ContactoDAO {
    @Query("SELECT * FROM contacto")
    List<Contact> getAll();

    @Insert
    void insertAll(Contact... contacts);

}
