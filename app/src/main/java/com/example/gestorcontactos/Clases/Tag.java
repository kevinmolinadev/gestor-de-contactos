package com.example.gestorcontactos.Clases;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "tag")
public class Tag {
        @PrimaryKey(autoGenerate = true)
        private int id;
        private String name;

        public Tag(String name) {
                this.name = name;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        @Override
        public String toString() {
                return "Tag{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        '}';
        }
}
