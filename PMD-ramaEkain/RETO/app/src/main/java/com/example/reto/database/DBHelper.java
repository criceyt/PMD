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

// Clase DBHelper que extiende SQLiteOpenHelper para gestionar la base de datos.
public class DBHelper extends SQLiteOpenHelper {

    // Nombre y versión de la base de datos
    private static final String DATABASE_NAME = "juegos.db";
    private static final int DATABASE_VERSION = 62;

    // Definición de las tablas y columnas para la base de datos
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

    private static final String TABLE_BIBLIOTECA = "biblioteca";
    private static final String COLUMN_BIBLIOTECA_ID = "id";
    private static final String COLUMN_BIBLIOTECA_USUARIO_ID = "usuario_id";
    private static final String COLUMN_BIBLIOTECA_JUEGO_ID = "juego_id";

    // Constructor de la clase DBHelper
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Método llamado cuando se crea la base de datos por primera vez
    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL para crear la tabla de juegos
        String createJuegosTable = "CREATE TABLE " + TABLE_JUEGOS + " ("
                + COLUMN_JUEGO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_JUEGO_NOMBRE + " TEXT NOT NULL, "
                + COLUMN_JUEGO_PRECIO + " TEXT NOT NULL, "
                + COLUMN_JUEGO_DESCRIPCION + " TEXT, "
                + COLUMN_JUEGO_IMAGEN + " TEXT, "
                + COLUMN_JUEGO_TRAILER + " TEXT "
                + ")";
        db.execSQL(createJuegosTable);

        // SQL para crear la tabla de usuarios
        String createUsuariosTable = "CREATE TABLE " + TABLE_USUARIOS + " ("
                + "dni TEXT PRIMARY KEY, "
                + COLUMN_USUARIO_NOMBRE + " TEXT NOT NULL, "
                + COLUMN_USUARIO_EMAIL + " TEXT NOT NULL UNIQUE, "
                + COLUMN_USUARIO_PASSWORD + " TEXT NOT NULL "
                + ")";
        db.execSQL(createUsuariosTable);

        // SQL para crear la tabla de la biblioteca (relaciona usuarios con juegos)
        String createBibliotecaTable = "CREATE TABLE " + TABLE_BIBLIOTECA + " ("
                + COLUMN_BIBLIOTECA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BIBLIOTECA_USUARIO_ID + " TEXT NOT NULL, "
                + COLUMN_BIBLIOTECA_JUEGO_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_BIBLIOTECA_USUARIO_ID + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USUARIO_ID + "), "
                + "FOREIGN KEY(" + COLUMN_BIBLIOTECA_JUEGO_ID + ") REFERENCES " + TABLE_JUEGOS + "(" + COLUMN_JUEGO_ID + ")"
                + ")";
        db.execSQL(createBibliotecaTable);

        // Inserta datos predeterminados en la tabla de juegos
        insertarDatosJuegos(db);
    }

    // Método llamado cuando la base de datos se actualiza (cambio de versión)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Elimina las tablas antiguas si existen
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIBLIOTECA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JUEGOS);
        // Vuelve a crear las tablas con la nueva estructura
        onCreate(db);
    }

    @SuppressLint("Range")
    // Método para obtener un juego por su ID
    public Juego obtenerJuegoPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Realiza una consulta SQL para obtener el juego por ID
        Cursor cursor = db.query("juegos", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        // Si el cursor contiene datos (es decir, se encontró el juego)
        if (cursor != null && cursor.moveToFirst()) {
            // Crea un objeto Juego donde se almacenarán los datos del juego encontrado
            Juego juego = new Juego();

            // Mapea los valores del cursor a los atributos del objeto Juego
            juego.setId(cursor.getInt(cursor.getColumnIndex("id")));              // Asigna el ID
            juego.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));    // Asigna el nombre
            juego.setDescripcion(cursor.getString(cursor.getColumnIndex("descripcion"))); // Asigna la descripción
            juego.setPrecio(cursor.getString(cursor.getColumnIndex("precio")));    // Asigna el precio
            juego.setImagen(cursor.getString(cursor.getColumnIndex("imagen")));    // Asigna la imagen
            juego.setTrailer(cursor.getString(cursor.getColumnIndex("trailer")));  // Asigna el trailer

            cursor.close(); // Cierra el cursor para liberar los recursos
            return juego;   // Retorna el objeto Juego con los datos mapeados
        } else {
            cursor.close(); // Si no se encuentra el juego, se cierra el cursor
            return null;    // Retorna null si no se encontró el juego con el ID proporcionado
        }
    }

    // Método para insertar juegos predeterminados en la base de datos
    private void insertarDatosJuegos(SQLiteDatabase db) {
        // Definición de los juegos a insertar en la tabla
        String[][] juegos = {
                // Cada juego está representado por un arreglo con: nombre, precio, descripción, imagen y trailer
                {"Hollow Knight", "14.99", "Hollow Knight es una aventura de acción clásica en 2D ambientada en un vasto mundo interconectado. Explora cavernas tortuosas, ciudades antiguas y páramos mortales. Combate contra criaturas corrompidas, haz nuevas amistades con extraños insectos y resuelve los antiguos misterios que yacen en el corazón del reino.", "hollow_knight", "hollow_knight" },
                {"League of Legends", "0.00", "League of Legends es un juego de estrategia por equipos en el que dos equipos de cinco campeones se enfrentan para ver quién destruye antes la base del otro. Elige de entre un elenco de 140 campeones para realizar jugadas épicas, asesinar rivales y derribar torretas para alzarte con la victoria.", "league_of_legends", "league_of_legends"},
                {"Skyrim", "37.99", "La historia se centra en los esfuerzos del Dovahkiin (Sangre de dragón) para derrotar a Alduin, un dragón o «dovah» que, según la profecía, está destinado a destruir el mundo. La trama se sitúa doscientos años después de los sucesos de Oblivion y tiene lugar en la provincia ficticia de Skyrim.", "skyrim", "skyrim"},
                {"Diablo 4", "49.99", "Diablo IV está ambientado muchos años tras los sucesos de Diablo III, después de la pérdida de millones de vidas que causaron las acciones de los Altos Cielos y los Infiernos Abrasadores. En ese vacío de poder reaparece un nombre legendario: Lilith, hija de Mefisto, el supuesto progenitor de la humanidad.", "diablo4", "diablo4"},
                {"Ark Survival: Ascended", "59.99", "Reimaginado con Unreal Engine 5, Ark: Survival Ascended es un juego de doma de dragones en un mundo abierto en el que comienzas como un humano débil. La primera tarea es asegurar tu supervivencia fabricando armas y otras herramientas que te ayuden a recolectar materiales para crear las estructuras esenciales del juego.", "ark_survival_ascended", "ark_survival_ascended" }
        };

        // Inserta cada juego en la base de datos
        for (String[] juego : juegos) {
            // Para cada juego, se crea un objeto ContentValues para preparar los datos para la inserción
            ContentValues values = new ContentValues();
            values.put(COLUMN_JUEGO_NOMBRE, juego[0]);          // Nombre del juego
            values.put(COLUMN_JUEGO_PRECIO, juego[1]);          // Precio del juego
            values.put(COLUMN_JUEGO_DESCRIPCION, juego[2]);     // Descripción del juego
            values.put(COLUMN_JUEGO_IMAGEN, juego[3]);         // Nombre de la imagen del juego (referencia)
            values.put(COLUMN_JUEGO_TRAILER, juego[4]);        // Nombre del trailer del juego (referencia)

            // Inserta los valores en la tabla de la base de datos
            db.insert(TABLE_JUEGOS, null, values);              // Inserta en la tabla TABLE_JUEGOS
        }
    }


    @SuppressLint("Range")
    // Método para verificar si el usuario existe en la base de datos
    public Usuario verificarUsuario(String dni, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Realiza la consulta SQL para verificar el usuario por dni y contraseña
        Cursor cursor = db.query(TABLE_USUARIOS, null, "dni=? AND password=?", new String[]{dni, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Usuario usuario = new Usuario();
            usuario.setId(cursor.getString(cursor.getColumnIndex("dni")));
            cursor.close(); // Cierra el cursor después de su uso
            return usuario;
        }
        cursor.close(); // Cierra el cursor si no se encuentra un usuario
        return null; // Retorna null si no se encuentra el usuario
    }

    // Método para agregar un juego a la biblioteca de un usuario
    public boolean agregarJuegoABiblioteca(String usuario_id, int juegoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BIBLIOTECA_USUARIO_ID, usuario_id);  // Establece el ID del usuario
        values.put(COLUMN_BIBLIOTECA_JUEGO_ID, juegoId);      // Establece el ID del juego

        try {
            // Inserta en la tabla de la biblioteca y verifica si fue exitoso
            long result = db.insert(TABLE_BIBLIOTECA, null, values);
            return result != -1; // Retorna true si la inserción fue exitosa
        } catch (Exception e) {
            // Si hay un error, lo registra en los logs
            Log.e("DB_ERROR", "Error al insertar juego en biblioteca: ", e);
            return false;
        } finally {
            db.close(); // Cierra la base de datos
        }
    }

    @SuppressLint("Range")
    public List<Juego> obtenerJuegosDeBiblioteca(String bibliotecaId) {
        // Crea una lista vacía para almacenar los juegos recuperados de la biblioteca
        List<Juego> juegos = new ArrayList<>();

        // Abre la base de datos en modo solo lectura
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL que une las tablas "juegos" y "biblioteca" para obtener todos los juegos que están en la biblioteca de un usuario específico
        String query = "SELECT juegos.* FROM " + TABLE_JUEGOS + " AS juegos " +
                "INNER JOIN " + TABLE_BIBLIOTECA + " AS biblioteca " +
                "ON juegos.id = biblioteca.juego_id " + // Relaciona los juegos con la biblioteca por el ID del juego
                "WHERE biblioteca.usuario_id = ?"; // Filtra los juegos por el ID del usuario en la biblioteca

        // Ejecuta la consulta con el ID de la biblioteca (usuario)
        Cursor cursor = db.rawQuery(query, new String[]{bibliotecaId});

        // Procesa los resultados del cursor, mapeando cada juego encontrado en un objeto "Juego"
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Crea un nuevo objeto Juego para cada registro del cursor
                Juego juego = new Juego();
                // Mapea los valores de las columnas de la tabla "juegos" al objeto Juego
                juego.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_JUEGO_ID))); // Asigna el ID del juego
                juego.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_NOMBRE))); // Asigna el nombre del juego
                juego.setDescripcion(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_DESCRIPCION))); // Asigna la descripción
                juego.setPrecio(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_PRECIO))); // Asigna el precio
                juego.setImagen(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_IMAGEN))); // Asigna la imagen
                juego.setTrailer(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_TRAILER))); // Asigna el tráiler

                // Añade el objeto juego a la lista de juegos
                juegos.add(juego);
            } while (cursor.moveToNext()); // Continúa iterando mientras haya más registros

            cursor.close(); // Cierra el cursor después de procesar los resultados
        }

        // Devuelve la lista de juegos obtenidos
        return juegos;
    }


    @SuppressLint("Range")
    public List<Juego> obtenerJuegosPorBiblioteca(String bibliotecaId) {
        // Crea una lista vacía para almacenar los juegos recuperados
        List<Juego> juegos = new ArrayList<>();

        // Abre la base de datos en modo solo lectura
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL que obtiene solo el nombre e imagen de los juegos en la biblioteca de un usuario específico
        String query = "SELECT j." + COLUMN_JUEGO_NOMBRE + ", j." + COLUMN_JUEGO_IMAGEN +
                " FROM " + TABLE_JUEGOS + " j " +
                "INNER JOIN " + TABLE_BIBLIOTECA + " b ON j." + COLUMN_JUEGO_ID + " = b." + COLUMN_BIBLIOTECA_JUEGO_ID +
                " WHERE b." + COLUMN_BIBLIOTECA_USUARIO_ID + " = ?"; // Se filtra por el ID de la biblioteca (usuario)

        // Ejecuta la consulta con el ID de la biblioteca (usuario)
        Cursor cursor = db.rawQuery(query, new String[]{bibliotecaId});

        // Procesa los resultados del cursor, mapeando solo el nombre y la imagen
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Crea un nuevo objeto Juego para almacenar solo los datos necesarios (nombre e imagen)
                Juego juego = new Juego();
                juego.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_NOMBRE))); // Establece el nombre del juego
                juego.setImagen(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_IMAGEN))); // Establece la imagen del juego

                // Añade el juego a la lista de juegos
                juegos.add(juego);
            } while (cursor.moveToNext()); // Continúa mientras haya más registros en el cursor
            cursor.close(); // Cierra el cursor después de procesar los resultados
        }

        // Cierra la base de datos después de completar la consulta
        db.close();

        // Devuelve la lista de juegos con solo el nombre y la imagen
        return juegos;
    }


    // Método para obtener todos los juegos de la base de datos
    public List<Juego> obtenerJuegos() {
        // Inicializa una lista vacía para almacenar los juegos recuperados
        List<Juego> juegos = new ArrayList<>();

        // Abre la base de datos en modo lectura
        SQLiteDatabase db = this.getReadableDatabase();

        // Realiza la consulta para obtener todos los juegos de la tabla "TABLE_JUEGOS"
        Cursor cursor = db.query(TABLE_JUEGOS,  // Nombre de la tabla a consultar
                null,  // Selecciona todas las columnas de la tabla (null significa todas las columnas)
                null,  // No se aplica filtro de selección, es decir, todos los registros serán seleccionados
                null,  // No se aplican agrupaciones
                null,  // No se aplica filtro de agrupación
                null,  // No se especifica filtro de ordenación
                null); // No se especifica límite de registros a recuperar

        // Si el cursor no es nulo y contiene resultados
        if (cursor != null) {
            // Mueve el cursor al primer registro de los resultados
            if (cursor.moveToFirst()) {
                // Itera sobre todos los registros del cursor
                do {
                    // Mapea los resultados de cada columna a las variables correspondientes del objeto Juego
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_JUEGO_ID)); // Obtiene el ID del juego
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_NOMBRE)); // Obtiene el nombre del juego
                    @SuppressLint("Range") String precio = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_PRECIO)); // Obtiene el precio del juego
                    @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_DESCRIPCION)); // Obtiene la descripción del juego
                    @SuppressLint("Range") String imagen = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_IMAGEN)); // Obtiene la imagen del juego
                    @SuppressLint("Range") String trailer = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_TRAILER)); // Obtiene el enlace al trailer del juego

                    // Crea un objeto Juego con los datos obtenidos y lo agrega a la lista de juegos
                    juegos.add(new Juego(id, nombre, precio, descripcion, imagen, trailer));
                } while (cursor.moveToNext());  // Continúa al siguiente registro mientras haya más juegos
            }
            // Cierra el cursor después de haber terminado de procesar los resultados
            cursor.close();
        }
        // Retorna la lista de juegos obtenida de la base de datos
        return juegos;
    }



    // Método para registrar un nuevo usuario
    public boolean registrarUsuario(String dni, String email, String nombre, String password) {
        // Verifica si el usuario ya existe mediante el método 'usuarioExiste'
        if (usuarioExiste(dni, email)) {
            return false; // Si el usuario ya existe, retorna false para evitar duplicados
        }

        // Abre la base de datos en modo escritura para insertar un nuevo registro
        SQLiteDatabase db = this.getWritableDatabase();

        // Crea un objeto ContentValues para almacenar los valores del nuevo usuario
        ContentValues values = new ContentValues();

        // Inserta los datos del usuario en el objeto ContentValues
        values.put("dni", dni); // Inserta el DNI del usuario
        values.put("email", email); // Inserta el correo electrónico
        values.put("nombre", nombre); // Inserta el nombre completo del usuario
        values.put("password", password); // Inserta la contraseña del usuario

        // Realiza la inserción del nuevo usuario en la tabla "usuarios"
        long result = db.insert("usuarios", null, values); // 'insert' devuelve el ID de la fila insertada o -1 si falla

        // Cierra la base de datos para liberar los recursos
        db.close();

        // Retorna 'true' si la inserción fue exitosa (el valor de 'result' no es -1), de lo contrario, retorna 'false'
        return result != -1; // Si result es -1, la inserción falló, de lo contrario, fue exitosa
    }



    // Método privado para verificar si el usuario ya existe en la base de datos
    private boolean usuarioExiste(String dni, String email) {
        // Abre la base de datos en modo solo lectura para consultar los datos
        SQLiteDatabase db = this.getReadableDatabase();

        // Definición de la consulta SQL que buscará un registro con el mismo DNI o correo electrónico
        String query = "SELECT * FROM usuarios WHERE dni = ? OR email = ?";

        // Ejecuta la consulta con los parámetros pasados (dni y email), evitando inyecciones SQL
        Cursor cursor = db.rawQuery(query, new String[]{dni, email});

        // Verifica si la consulta devuelve alguna fila
        boolean existe = cursor.getCount() > 0;  // Si el contador de filas es mayor que 0, significa que el usuario ya existe

        // Cierra el cursor para liberar los recursos
        cursor.close();

        // Cierra la base de datos para liberar los recursos utilizados
        db.close();

        // Devuelve 'true' si el usuario existe (es decir, si la consulta devolvió algún resultado),
        // de lo contrario, devuelve 'false'
        return existe;
    }


}
