package com.example.gestorcontactos.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gestorcontactos.Clases.Contact;
import com.example.gestorcontactos.Clases.ContactTag;
import java.util.List;

@Dao
public interface ContactTagDao {
    @Query("SELECT * FROM contact_tag")
    List<ContactTag> loadAll();
    @Insert
    void insertAll(ContactTag... contactTags);
    @Delete
    void deleteContactTag(ContactTag contactTag);
    @Query("SELECT contact.* FROM contact INNER JOIN contact_tag ON contact.id = contact_tag.id_contact WHERE contact_tag.id_tag = :tagId")
    List<Contact> getContactsByTagId(int tagId);
    @Query("SELECT contact_tag.id_tag FROM contact_tag INNER JOIN contact ON contact.id = contact_tag.id_contact WHERE contact.name = :contactName")
    int getTagIdByContactName(String contactName);

    @Query("SELECT contact_tag.id FROM contact_tag INNER JOIN contact ON contact.id = contact_tag.id_contact WHERE contact.name = :contactName AND contact_tag.id_tag = :tagId")
    int getIdByContactName_TagId(String contactName, int tagId);



}
