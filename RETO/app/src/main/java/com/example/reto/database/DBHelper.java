package com.example.reto.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.reto.database.modelo.Juego;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "juegos.db";
    private static final int DATABASE_VERSION = 35;


    private static final String TABLE_JUEGOS = "juegos";
    private static final String COLUMN_JUEGO_ID = "id";
    private static final String COLUMN_JUEGO_NOMBRE = "nombre";
    private static final String COLUMN_JUEGO_PRECIO = "precio";
    private static final String COLUMN_JUEGO_DESCRIPCION = "descripcion";
    private static final String COLUMN_JUEGO_IMAGEN = "imagen";
    private static final String COLUMN_JUEGO_TRAILER = "trailer";


    private static final String TABLE_USUARIOS = "usuarios";
    private static final String COLUMN_USUARIO_ID = "id";
    private static final String COLUMN_USUARIO_NOMBRE = "nombre";
    private static final String COLUMN_USUARIO_EMAIL = "email";
    private static final String COLUMN_USUARIO_PASSWORD = "password";
    private static final String COLUMN_USUARIO_IMAGE = "imagen";


    private static final String TABLE_BIBLIOTECA = "biblioteca";
    private static final String COLUMN_BIBLIOTECA_ID = "id";
    private static final String COLUMN_BIBLIOTECA_USUARIO_ID = "usuario_id";
    private static final String COLUMN_BIBLIOTECA_JUEGO_ID = "juego_id";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createJuegosTable = "CREATE TABLE " + TABLE_JUEGOS + " ("
                + COLUMN_JUEGO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_JUEGO_NOMBRE + " TEXT NOT NULL, "
                + COLUMN_JUEGO_PRECIO + " TEXT NOT NULL, "
                + COLUMN_JUEGO_DESCRIPCION + " TEXT, "
                + COLUMN_JUEGO_IMAGEN + " TEXT, "
                + COLUMN_JUEGO_TRAILER + " TEXT "
                + ")";
        db.execSQL(createJuegosTable);


        String createUsuariosTable = "CREATE TABLE " + TABLE_USUARIOS + " ("
                + "dni TEXT PRIMARY KEY, " // Usa dni como clave primaria
                + COLUMN_USUARIO_NOMBRE + " TEXT NOT NULL, "
                + COLUMN_USUARIO_EMAIL + " TEXT NOT NULL UNIQUE, "
                + COLUMN_USUARIO_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_USUARIO_IMAGE + " BLOB "
                + ")";
        db.execSQL(createUsuariosTable);



        String createBibliotecaTable = "CREATE TABLE " + TABLE_BIBLIOTECA + " ("
                + COLUMN_BIBLIOTECA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BIBLIOTECA_USUARIO_ID + " INTEGER NOT NULL, "
                + COLUMN_BIBLIOTECA_JUEGO_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_BIBLIOTECA_USUARIO_ID + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USUARIO_ID + "), "
                + "FOREIGN KEY(" + COLUMN_BIBLIOTECA_JUEGO_ID + ") REFERENCES " + TABLE_JUEGOS + "(" + COLUMN_JUEGO_ID + ")"
                + ")";
        db.execSQL(createBibliotecaTable);


        insertarDatosJuegos(db);
    }


    private void insertarDatosJuegos(SQLiteDatabase db) {

        String[][] juegos = {
                {"Hollow Knight", "14.99", "Juego de acción y aventura en 2D.", "hollow_knight"},
                {"League of Legends", "0.00", "Juego de estrategia y combate en línea.", "league_of_legends"},
                {"Skyrim", "37.99", "Juego de rol de mundo abierto.", "skyrim"},
                {"Diablo 4", "49.99", "Juego de rol de acción y hack and slash.", "diablo4"},
                {"Ark Survival: Ascended", "59.99", "Juego de supervivencia y aventura en un mundo prehistórico.", "ark_survival_ascended"}
        };

        for (String[] juego : juegos) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_JUEGO_NOMBRE, juego[0]);
            values.put(COLUMN_JUEGO_PRECIO, juego[1]);
            values.put(COLUMN_JUEGO_DESCRIPCION, juego[2]);
            values.put(COLUMN_JUEGO_IMAGEN, juego[3]);


            db.insert(TABLE_JUEGOS, null, values);
        }
    }

    public boolean verificarUsuario(String dni, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("DBHelper", "Verificando usuario con DNI: " + dni + " y contraseña: " + password);

        String query = "SELECT * FROM usuarios WHERE dni = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{dni, password});

        boolean isValidUser = false;

        if (cursor != null && cursor.moveToFirst()) {
            Log.d("DBHelper", "Usuario encontrado en la base de datos");
            isValidUser = true;
        } else {
            Log.d("DBHelper", "Usuario no encontrado en la base de datos");
        }

        cursor.close();
        db.close();
        return isValidUser;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }




    public List<Juego> obtenerJuegos() {
        List<Juego> juegos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_JUEGOS,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_JUEGO_ID));
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_NOMBRE));
                    @SuppressLint("Range") String precio = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_PRECIO));
                    @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_DESCRIPCION));
                    @SuppressLint("Range") String imagen = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_IMAGEN));
                    @SuppressLint("Range") String trailer = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_TRAILER));

                    juegos.add(new Juego(id, nombre, precio, descripcion, imagen, trailer));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return juegos;
    }

    public boolean registrarUsuario(String dni, String email, String nombre, String password) {
        if (usuarioExiste(dni, email)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("dni", dni);
        values.put("email", email);
        values.put("nombre", nombre);
        values.put("password", password);

        long result = db.insert("usuarios", null, values);
        db.close();
        return result != -1;
    }

    private boolean usuarioExiste(String dni, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM usuarios WHERE dni = ? OR email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{dni, email});

        boolean existe = cursor.getCount() > 0; // Devuelve true si existe algún registro
        cursor.close();
        db.close();
        return existe;
    }

    public Cursor obtenerDatosUsuario(String dni) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("usuarios", null, "dni=?", new String[]{dni}, null, null, null);
        return cursor;
    }

    public void guardarImagenUsuario(String dni, byte[] imagenBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_IMAGE, imagenBytes); // Guarda la imagen como BLOB

        int rowsAffected = db.update(TABLE_USUARIOS, values, "dni = ?", new String[]{dni});
        Log.d("DBHelper", "Filas afectadas al guardar imagen: " + rowsAffected);
        db.close();
    }

    // Recuperar la imagen del usuario
    public byte[] recuperarImagenDeUsuario(String dni) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_USUARIO_IMAGE};  // Asegúrate de que 'imagen' está en la lista de columnas
        String selection = "dni = ?";
        String[] selectionArgs = {dni};

        Cursor cursor = db.query(TABLE_USUARIOS, projection, selection, selectionArgs, null, null, null);
        byte[] imagen = null; // Inicializa como null por si no se encuentra imagen

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(COLUMN_USUARIO_IMAGE);
                    if (columnIndex != -1) {  // Verifica si el índice es válido
                        imagen = cursor.getBlob(columnIndex);  // Recupera la imagen
                        Log.d("DBHelper", "Imagen recuperada de la base de datos, tamaño: " + (imagen != null ? imagen.length : "null"));
                    }
                }
            } finally {
                cursor.close();  // Asegúrate de cerrar el cursor
            }
        }

        db.close();
        return imagen;  // Devuelve null si no hay imagen o si no se encontró el usuario
    }





}
