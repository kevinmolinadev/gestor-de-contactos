package com.example.gestorcontactos.Database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private static AppDatabase appDatabase;
    private static final String DATABASE_NAME = "gestor-de-contactos";

    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .build();
        }
        return appDatabase;
    }
}
