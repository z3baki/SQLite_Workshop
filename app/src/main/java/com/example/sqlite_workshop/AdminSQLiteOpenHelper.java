package com.example.sqlite_workshop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Created by Juan Sebastian Varela and Julian Baquero 10/04/24
 */

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    /*
    Constructor, para gestionar la base de datos, el super nos ayuda
    a administrar el desarrollo de SQL.
     */
    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*
    Metodo para crear tabla con 4 parametros, codigo de la solicitd, descripcion, numero de telefono y placa del vehiculo
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table solicitud(codigo int primary key, descripcion text, numero int, placa text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
