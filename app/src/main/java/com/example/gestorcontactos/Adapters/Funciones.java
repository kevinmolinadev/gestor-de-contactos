package com.example.gestorcontactos.Adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Base64;

import com.example.gestorcontactos.Clases.Agenda;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class Funciones {
    private static Funciones instance;
    public static Funciones getInstance() {
        if (instance == null) {
            instance = new Funciones();
        }
        return instance;
    }
    public Bitmap generateProfileImage(String name) {
        int width = 200;
        int height = 200;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Generar un color aleatorio para el fondo
        Random random = new Random();
        int backgroundColor = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        canvas.drawColor(backgroundColor);

        // Configurar el pincel para dibujar las iniciales
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(80);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Obtener las iniciales del nombre
        String initials;
        if (!TextUtils.isEmpty(name)) {
            String[] nameParts = name.trim().split("\\s+");
            StringBuilder initialsBuilder = new StringBuilder();
            for (String part : nameParts) {
                if (part.length() > 0) {
                    initialsBuilder.append(part.charAt(0));
                }
            }
            initials = initialsBuilder.toString().toUpperCase();
        } else {
            initials = "";
        }

        // Dibujar las iniciales en el centro de la imagen
        int xPos = canvas.getWidth() / 2;
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        canvas.drawText(initials, xPos, yPos, textPaint);

        return bitmap;
    }

    public String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public Bitmap convertStringToBitmap(String imageString) {
        byte[] decodedBytes = Base64.decode(imageString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
