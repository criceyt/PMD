package com.example.reto.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.reto.database.modelo.Juego;
import com.example.reto.database.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "juegos.db";
    private static final int DATABASE_VERSION = 47;


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
                + "dni TEXT PRIMARY KEY, "
                + COLUMN_USUARIO_NOMBRE + " TEXT NOT NULL, "
                + COLUMN_USUARIO_EMAIL + " TEXT NOT NULL UNIQUE, "
                + COLUMN_USUARIO_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_USUARIO_IMAGE + " BLOB "
                + ")";
        db.execSQL(createUsuariosTable);



        String createBibliotecaTable = "CREATE TABLE " + TABLE_BIBLIOTECA + " ("
                + COLUMN_BIBLIOTECA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BIBLIOTECA_USUARIO_ID + " TEXT NOT NULL, "
                + COLUMN_BIBLIOTECA_JUEGO_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_BIBLIOTECA_USUARIO_ID + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USUARIO_ID + "), "
                + "FOREIGN KEY(" + COLUMN_BIBLIOTECA_JUEGO_ID + ") REFERENCES " + TABLE_JUEGOS + "(" + COLUMN_JUEGO_ID + ")"
                + ")";
        db.execSQL(createBibliotecaTable);


        insertarDatosJuegos(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIBLIOTECA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JUEGOS);
        onCreate(db);
    }

    @SuppressLint("Range")
    public Juego obtenerJuegoPorId(int id) {
        // Consulta para obtener el juego por ID
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("juegos", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Juego juego = new Juego();
            juego.setId(cursor.getInt(cursor.getColumnIndex("id")));
            juego.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
            juego.setDescripcion(cursor.getString(cursor.getColumnIndex("descripcion")));
            juego.setPrecio(cursor.getString(cursor.getColumnIndex("precio")));
            juego.setImagen(cursor.getString(cursor.getColumnIndex("imagen")));
            juego.setTrailer(cursor.getString(cursor.getColumnIndex("trailer")));
            // Configura los otros atributos del juego si es necesario

            cursor.close();
            return juego;
        } else {
            cursor.close();
            return null; // Si no se encuentra el juego
        }
    }
    private void insertarDatosJuegos(SQLiteDatabase db) {

        String[][] juegos = {
                {"Hollow Knight", "14.99", "Hollow Knight es una aventura de acción clásica en 2D ambientada en un vasto mundo interconectado. Explora cavernas tortuosas, ciudades antiguas y páramos mortales. Combate contra criaturas corrompidas, haz nuevas amistades con extraños insectos y resuelve los antiguos misterios que yacen en el corazón del reino.", "hollow_knight", "hollow_knight" },
                {"League of Legends", "0.00", "League of Legends es un juego de estrategia por equipos en el que dos equipos de cinco campeones se enfrentan para ver quién destruye antes la base del otro. Elige de entre un elenco de 140 campeones para realizar jugadas épicas, asesinar rivales y derribar torretas para alzarte con la victoria.", "league_of_legends", "league_of_legends"},
                {"Skyrim", "37.99", "La historia se centra en los esfuerzos del Dovahkiin (Sangre de dragón) para derrotar a Alduin, un dragón o «dovah» que, según la profecía, está destinado a destruir el mundo. La trama se sitúa doscientos años después de los sucesos de Oblivion y tiene lugar en la provincia ficticia de Skyrim.", "skyrim", "skyrim"},
                {"Diablo 4", "49.99", "Diablo IV está ambientado muchos años tras los sucesos de Diablo III, después de la pérdida de millones de vidas que causaron las acciones de los Altos Cielos y los Infiernos Abrasadores. En ese vacío de poder reaparece un nombre legendario: Lilith, hija de Mefisto, el supuesto progenitor de la humanidad.", "diablo4", "diablo4"},
                {"Ark Survival: Ascended", "59.99", "Reimaginado con Unreal Engine 5, Ark: Survival Ascended es un juego de doma de dragones en un mundo abierto en el que comienzas como un humano débil. La primera tarea es asegurar tu supervivencia fabricando armas y otras herramientas que te ayuden a recolectar materiales para crear las estructuras esenciales del juego .", "ark_survival_ascended", "ark_survival_ascended"}
        };

        for (String[] juego : juegos) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_JUEGO_NOMBRE, juego[0]);
            values.put(COLUMN_JUEGO_PRECIO, juego[1]);
            values.put(COLUMN_JUEGO_DESCRIPCION, juego[2]);
            values.put(COLUMN_JUEGO_IMAGEN, juego[3]);
            values.put(COLUMN_JUEGO_TRAILER, juego[4]);



            db.insert(TABLE_JUEGOS, null, values);
        }
    }
    public Usuario obtenerUsuarioPorId(String userId) {
        // Obtener la base de datos en modo lectura
        SQLiteDatabase db = this.getReadableDatabase();

        // Definir la consulta SQL
        String query = "SELECT * FROM " + TABLE_USUARIOS + " WHERE dni = ?";  // Usamos 'dni' como clave primaria

        // Ejecutar la consulta con el userId (dni)
        Cursor cursor = db.rawQuery(query, new String[]{userId});

        // Si el cursor tiene resultados, obtenemos los datos del usuario
        if (cursor != null && cursor.moveToFirst()) {
            // Obtener los datos del cursor
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_ID));
            @SuppressLint("Range") String dni = cursor.getString(cursor.getColumnIndex("dni"));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_EMAIL));
            @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_NOMBRE));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_PASSWORD));
            @SuppressLint("Range") byte[] imagen = cursor.getBlob(cursor.getColumnIndex(COLUMN_USUARIO_IMAGE));

            // Crear el objeto Usuario con los datos obtenidos
            Usuario usuario = new Usuario(id, nombre, email, password);
            cursor.close();  // Cerrar el cursor después de usarlo
            return usuario;
        } else {
            cursor.close();
            return null;  // Si no se encontró ningún usuario con ese ID
        }
    }

    @SuppressLint("Range")
    public Usuario verificarUsuario(String dni, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USUARIOS, null, "dni=? AND password=?", new String[]{dni, password}, null, null, null);

        // Verifica si el cursor tiene datos y si hay un resultado
        if (cursor != null && cursor.moveToFirst()) {
            Usuario usuario = new Usuario();
            // Asigna el valor del dni al usuario, pero recuerda que el nombre de la columna puede variar dependiendo de tu estructura
            usuario.setId(cursor.getString(cursor.getColumnIndex("dni")));
            cursor.close(); // Cierra el cursor después de su uso
            return usuario;
        }
        cursor.close(); // Cierra el cursor si no se encuentra un usuario
        return null; // Retorna null si no hay usuario que coincida
    }



    public void guardarImagenUsuario(String dni, byte[] imagenBytes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO_IMAGE, imagenBytes); // Guarda la imagen como BLOB

        int rowsAffected = db.update(TABLE_USUARIOS, values, "dni = ?", new String[]{dni});
        Log.d("DBHelper", "Filas afectadas al guardar imagen: " + rowsAffected);
        db.close();
    }



    public boolean agregarJuegoABiblioteca(String usuario_id, int juegoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Usa las constantes de las columnas, no los nombres como cadenas literales
        values.put(COLUMN_BIBLIOTECA_USUARIO_ID, usuario_id);  // Usar la constante COLUMN_BIBLIOTECA_USUARIO_ID
        values.put(COLUMN_BIBLIOTECA_JUEGO_ID, juegoId);      // Usar la constante COLUMN_BIBLIOTECA_JUEGO_ID

        try {
            // Intentar insertar el juego
            long result = db.insert(TABLE_BIBLIOTECA, null, values);  // Usar la constante TABLE_BIBLIOTECA

            // Verificar si la inserción fue exitosa
            return result != -1;
        } catch (Exception e) {
            // En caso de error, imprimir el error en los logs
            Log.e("DB_ERROR", "Error al insertar juego en biblioteca: ", e);
            return false;
        } finally {
            db.close();
        }
    }




    @SuppressLint("Range")
    public List<Juego> obtenerJuegosDeBiblioteca(String bibliotecaId) {
        List<Juego> juegos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Realiza la consulta para obtener los juegos que pertenecen al usuario
        String query = "SELECT juegos.* FROM " + TABLE_JUEGOS + " AS juegos " +
                "INNER JOIN " + TABLE_BIBLIOTECA + " AS biblioteca " +
                "ON juegos.id = biblioteca.juego_id " +
                "WHERE biblioteca.usuario_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{bibliotecaId});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Juego juego = new Juego();
                juego.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_JUEGO_ID)));
                juego.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_NOMBRE)));
                juego.setDescripcion(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_DESCRIPCION)));
                juego.setPrecio(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_PRECIO)));
                juego.setImagen(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_IMAGEN)));
                juego.setTrailer(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_TRAILER)));
                juegos.add(juego);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return juegos;
    }

    @SuppressLint("Range")
    public List<Juego> obtenerJuegosPorBiblioteca(String bibliotecaId) {
        List<Juego> juegos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT j." + COLUMN_JUEGO_NOMBRE + ", j." + COLUMN_JUEGO_IMAGEN +
                " FROM " + TABLE_JUEGOS + " j " +
                "INNER JOIN " + TABLE_BIBLIOTECA + " b ON j." + COLUMN_JUEGO_ID + " = b." + COLUMN_BIBLIOTECA_JUEGO_ID +
                " WHERE b." + COLUMN_BIBLIOTECA_USUARIO_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{bibliotecaId});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Juego juego = new Juego();
                juego.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_NOMBRE)));
                juego.setImagen(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_IMAGEN)));
                juegos.add(juego);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return juegos;
    }


    // En DBHelper
    @SuppressLint("Range")
    public List<Juego> obtenerJuegosPorUsuario(String userId) {
        List<Juego> juegos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM juegos WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Juego juego = new Juego();
                // Aquí deberías mapear los datos del cursor a tu objeto Juego
                juego.setId(cursor.getInt(cursor.getColumnIndex("id")));
                juego.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                juego.setImagen(cursor.getString(cursor.getColumnIndex("imagen")));
                juegos.add(juego);
            }
            cursor.close();
        }

        db.close();
        return juegos;
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
    public Cursor obtenerDatosUsuario(String usuarioId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            // Especificar las columnas necesarias
            String[] columnas = {"dni", "nombre", "direccion", "ciudad", "codigoPostal", "email", "login"};

            // Realizar la consulta utilizando dni en lugar de usuario_id
            cursor = db.query("usuarios", columnas, "dni=?", new String[]{usuarioId}, null, null, null);

            // Verificar si se encontró algún dato
            if (cursor != null && cursor.moveToFirst()) {
                return cursor;
            } else {
                if (cursor != null) {
                    cursor.close();
                }
                Log.d("DBHelper", "No se encontraron datos para el DNI: " + usuarioId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


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