package com.example.gestorcontactos.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.gestorcontactos.Clases.Tag;

import java.util.List;
@Dao
public interface TagDao {
    @Query("SELECT * FROM tag")
    List<Tag> loadAll();
    @Insert
    void insertAll(Tag... tags);
    @Delete
    void delete(Tag... tags);
}
