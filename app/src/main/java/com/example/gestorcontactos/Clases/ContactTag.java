package com.example.gestorcontactos.Clases;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_tag",
        foreignKeys = {
                @ForeignKey(entity = Contact.class,
                        parentColumns = "id",
                        childColumns = "id_contact",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Tag.class,
                        parentColumns = "id",
                        childColumns = "id_tag",
                        onDelete = ForeignKey.CASCADE),
        })
public class ContactTag {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "id_contact")
    private int id_contact;

    @ColumnInfo(name = "id_tag")
    private int id_tag;

    public ContactTag( int id_contact, int id_tag) {
        this.id_contact = id_contact;
        this.id_tag = id_tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_contact() {
        return id_contact;
    }

    public void setId_contact(int id_contact) {
        this.id_contact = id_contact;
    }

    public int getId_tag() {
        return id_tag;
    }

    public void setId_tag(int id_tag) {
        this.id_tag = id_tag;
    }
}
