package com.example.gestorcontactos.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gestorcontactos.Clases.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> loadAll();
    @Insert
    void insertAll(Contact... contacts);
    @Query("SELECT * FROM contact WHERE name IN (:contactName)")
    List<Contact> loadAllBySongId(String... contactName);
}
