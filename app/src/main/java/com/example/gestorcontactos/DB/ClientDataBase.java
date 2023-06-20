package com.example.gestorcontactos.DB;

import android.content.Context;

import androidx.room.Room;

public class ClientDataBase {
    private static DataBase appDatabase;
    private static final String DATABASE_NAME = "gestor_de_contactos";

    public static DataBase getAppDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), DataBase.class, DATABASE_NAME)
                    .build();
        }
        return appDatabase;
    }
}
