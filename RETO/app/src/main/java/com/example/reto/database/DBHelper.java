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

    private static final String DATABASE_NAME = "juegos.db";  // Nombre de la base de datos
    private static final int DATABASE_VERSION = 47;  // Versión de la base de datos


    // Definición de las constantes para la tabla "juegos"
    private static final String TABLE_JUEGOS = "juegos";  // Nombre de la tabla de juegos
    private static final String COLUMN_JUEGO_ID = "id";  // Columna para el ID del juego
    private static final String COLUMN_JUEGO_NOMBRE = "nombre";  // Columna para el nombre del juego
    private static final String COLUMN_JUEGO_PRECIO = "precio";  // Columna para el precio del juego
    private static final String COLUMN_JUEGO_DESCRIPCION = "descripcion";  // Columna para la descripción del juego
    private static final String COLUMN_JUEGO_IMAGEN = "imagen";  // Columna para la imagen del juego
    private static final String COLUMN_JUEGO_TRAILER = "trailer";  // Columna para el trailer del juego



    // Definición de las constantes para la tabla "usuarios"
    private static final String TABLE_USUARIOS = "usuarios";  // Nombre de la tabla de usuarios
    private static final String COLUMN_USUARIO_ID = "id";  // Columna para el ID del usuario
    private static final String COLUMN_USUARIO_NOMBRE = "nombre";  // Columna para el nombre del usuario
    private static final String COLUMN_USUARIO_EMAIL = "email";  // Columna para el email del usuario
    private static final String COLUMN_USUARIO_PASSWORD = "password";  // Columna para la contraseña del usuario
    private static final String COLUMN_USUARIO_IMAGE = "imagen";  // Columna para la imagen del usuario


    // Definición de las constantes para la tabla "biblioteca"
    private static final String TABLE_BIBLIOTECA = "biblioteca";  // Nombre de la tabla de biblioteca
    private static final String COLUMN_BIBLIOTECA_ID = "id";  // Columna para el ID de la biblioteca
    private static final String COLUMN_BIBLIOTECA_USUARIO_ID = "usuario_id";  // Columna para el ID del usuario
    private static final String COLUMN_BIBLIOTECA_JUEGO_ID = "juego_id";  // Columna para el ID del juego


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  // Llama al constructor de la clase base
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Crear la tabla de juegos
        String createJuegosTable = "CREATE TABLE " + TABLE_JUEGOS + " ("
                + COLUMN_JUEGO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_JUEGO_NOMBRE + " TEXT NOT NULL, "
                + COLUMN_JUEGO_PRECIO + " TEXT NOT NULL, "
                + COLUMN_JUEGO_DESCRIPCION + " TEXT, "
                + COLUMN_JUEGO_IMAGEN + " TEXT, "
                + COLUMN_JUEGO_TRAILER + " TEXT "
                + ")";
        db.execSQL(createJuegosTable);  // Ejecuta la consulta para crear la tabla "juegos"



        // Crear la tabla de usuarios
        String createUsuariosTable = "CREATE TABLE " + TABLE_USUARIOS + " ("
                + "dni TEXT PRIMARY KEY, "
                + COLUMN_USUARIO_NOMBRE + " TEXT NOT NULL, "
                + COLUMN_USUARIO_EMAIL + " TEXT NOT NULL UNIQUE, "
                + COLUMN_USUARIO_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_USUARIO_IMAGE + " BLOB "
                + ")";
        db.execSQL(createUsuariosTable);  // Ejecuta la consulta para crear la tabla "usuarios"




        // Crear la tabla de biblioteca
        String createBibliotecaTable = "CREATE TABLE " + TABLE_BIBLIOTECA + " ("
                + COLUMN_BIBLIOTECA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BIBLIOTECA_USUARIO_ID + " TEXT NOT NULL, "
                + COLUMN_BIBLIOTECA_JUEGO_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_BIBLIOTECA_USUARIO_ID + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_USUARIO_ID + "), "
                + "FOREIGN KEY(" + COLUMN_BIBLIOTECA_JUEGO_ID + ") REFERENCES " + TABLE_JUEGOS + "(" + COLUMN_JUEGO_ID + ")"
                + ")";
        db.execSQL(createBibliotecaTable);  // Ejecuta la consulta para crear la tabla "biblioteca"



        insertarDatosJuegos(db);  // Inserta los datos iniciales de juegos en la base de datos
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIBLIOTECA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JUEGOS);
        onCreate(db);  // Vuelve a crear las tablas con la nueva versión de la base de datos
    }


    @SuppressLint("Range")
    public Juego obtenerJuegoPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();  // Obtiene una instancia de la base de datos en modo solo lectura
        Cursor cursor = db.query("juegos", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);  // Ejecuta la consulta

        if (cursor != null && cursor.moveToFirst()) {  // Si la consulta devuelve resultados
            Juego juego = new Juego();  // Crea un objeto Juego
            juego.setId(cursor.getInt(cursor.getColumnIndex("id")));  // Obtiene el ID del juego
            juego.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));  // Obtiene el nombre del juego
            juego.setDescripcion(cursor.getString(cursor.getColumnIndex("descripcion")));  // Obtiene la descripción
            juego.setPrecio(cursor.getString(cursor.getColumnIndex("precio")));  // Obtiene el precio
            juego.setImagen(cursor.getString(cursor.getColumnIndex("imagen")));  // Obtiene la imagen
            juego.setTrailer(cursor.getString(cursor.getColumnIndex("trailer")));  // Obtiene el trailer

            cursor.close();  // Cierra el cursor después de obtener los datos
            return juego;  // Devuelve el objeto Juego con los datos
        } else {
            cursor.close();  // Cierra el cursor si no se encuentra el juego
            return null;  // Devuelve null si no se encontró el juego
        }
    }

    private void insertarDatosJuegos(SQLiteDatabase db) {
        // Inserta los datos de los juegos en la tabla de juegos
        // Se definen los juegos como un arreglo bidimensional de Strings
        String[][] juegos = {
                {"Hollow Knight", "14.99", "Hollow Knight es una aventura de acción clásica en 2D ambientada en un vasto mundo interconectado. Explora cavernas tortuosas, ciudades antiguas y páramos mortales. Combate contra criaturas corrompidas, haz nuevas amistades con extraños insectos y resuelve los antiguos misterios que yacen en el corazón del reino.", "hollow_knight", "hollow_knight" },
                {"League of Legends", "0.00", "League of Legends es un juego de estrategia por equipos en el que dos equipos de cinco campeones se enfrentan para ver quién destruye antes la base del otro. Elige de entre un elenco de 140 campeones para realizar jugadas épicas, asesinar rivales y derribar torretas para alzarte con la victoria.", "league_of_legends", "league_of_legends"},
                {"Skyrim", "37.99", "La historia se centra en los esfuerzos del Dovahkiin (Sangre de dragón) para derrotar a Alduin, un dragón o «dovah» que, según la profecía, está destinado a destruir el mundo. La trama se sitúa doscientos años después de los sucesos de Oblivion y tiene lugar en la provincia ficticia de Skyrim.", "skyrim", "skyrim"},
                {"Diablo 4", "49.99", "Diablo IV está ambientado muchos años tras los sucesos de Diablo III, después de la pérdida de millones de vidas que causaron las acciones de los Altos Cielos y los Infiernos Abrasadores. En ese vacío de poder reaparece un nombre legendario: Lilith, hija de Mefisto, el supuesto progenitor de la humanidad.", "diablo4", "diablo4"},
                {"Ark Survival: Ascended", "59.99", "Reimaginado con Unreal Engine 5, Ark: Survival Ascended es un juego de doma de dragones en un mundo abierto en el que comienzas como un humano débil. La primera tarea es asegurar tu supervivencia fabricando armas y otras herramientas que te ayuden a recolectar materiales para crear las estructuras esenciales del juego .", "ark_survival_ascended", "ark_survival_ascended"}
        };

        // Itera a través del arreglo de juegos
        for (String[] juego : juegos) {
            ContentValues values = new ContentValues(); // Crea un objeto ContentValues para almacenar los datos del juego

            // Agrega cada atributo del juego a ContentValues
            values.put(COLUMN_JUEGO_NOMBRE, juego[0]); // Nombre del juego
            values.put(COLUMN_JUEGO_PRECIO, juego[1]); // Precio del juego
            values.put(COLUMN_JUEGO_DESCRIPCION, juego[2]); // Descripción del juego
            values.put(COLUMN_JUEGO_IMAGEN, juego[3]); // Imagen del juego
            values.put(COLUMN_JUEGO_TRAILER, juego[4]); // Trailer del juego

            // Inserta los valores en la tabla "juegos" en la base de datos
            db.insert(TABLE_JUEGOS, null, values);  // El segundo parámetro es null ya que no usamos un valor predeterminado para las columnas
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
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_ID));  // Obtiene el id del usuario
            @SuppressLint("Range") String dni = cursor.getString(cursor.getColumnIndex("dni"));  // Obtiene el dni del usuario
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_EMAIL));  // Obtiene el email
            @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_NOMBRE));  // Obtiene el nombre
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_USUARIO_PASSWORD));  // Obtiene la contraseña
            @SuppressLint("Range") byte[] imagen = cursor.getBlob(cursor.getColumnIndex(COLUMN_USUARIO_IMAGE));  // Obtiene la imagen en formato BLOB

            // Crear el objeto Usuario con los datos obtenidos
            Usuario usuario = new Usuario(id, nombre, email, password);  // Crea el objeto usuario con los valores obtenidos
            cursor.close();  // Cierra el cursor después de usarlo
            return usuario;  // Devuelve el usuario
        } else {
            cursor.close();  // Cierra el cursor si no se encuentra el usuario
            return null;  // Si no se encontró ningún usuario con ese ID
        }
    }


    @SuppressLint("Range")
    public Usuario verificarUsuario(String dni, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para verificar si existe un usuario con el dni y password proporcionados
        Cursor cursor = db.query(TABLE_USUARIOS, null, "dni=? AND password=?", new String[]{dni, password}, null, null, null);

        // Verifica si el cursor tiene datos y si hay un resultado
        if (cursor != null && cursor.moveToFirst()) {
            Usuario usuario = new Usuario();
            // Asigna el valor del dni al usuario, pero recuerda que el nombre de la columna puede variar dependiendo de tu estructura
            usuario.setId(cursor.getString(cursor.getColumnIndex("dni")));  // Establece el ID del usuario
            cursor.close();  // Cierra el cursor después de su uso
            return usuario;  // Devuelve el usuario
        }

        cursor.close();  // Cierra el cursor si no se encuentra un usuario
        return null;  // Retorna null si no hay usuario que coincida
    }




    public void guardarImagenUsuario(String dni, byte[] imagenBytes) {
        SQLiteDatabase db = this.getWritableDatabase();  // Obtiene la base de datos en modo escritura
        ContentValues values = new ContentValues();  // Crea un objeto ContentValues para almacenar los datos

        values.put(COLUMN_USUARIO_IMAGE, imagenBytes);  // Guarda la imagen como un BLOB en la columna correspondiente

        // Realiza la actualización de la imagen del usuario en la base de datos
        int rowsAffected = db.update(TABLE_USUARIOS, values, "dni = ?", new String[]{dni});

        Log.d("DBHelper", "Filas afectadas al guardar imagen: " + rowsAffected);  // Muestra cuántas filas fueron actualizadas
        db.close();  // Cierra la base de datos
    }




    public boolean agregarJuegoABiblioteca(String usuario_id, int juegoId) {
        // Abre la base de datos en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();  // Crea un objeto ContentValues para insertar datos

        // Asigna los valores correspondientes a la base de datos utilizando las constantes de columna
        values.put(COLUMN_BIBLIOTECA_USUARIO_ID, usuario_id);  // Asigna el id del usuario a la columna correspondiente
        values.put(COLUMN_BIBLIOTECA_JUEGO_ID, juegoId);      // Asigna el id del juego a la columna correspondiente

        try {
            // Intenta insertar el juego en la biblioteca
            long result = db.insert(TABLE_BIBLIOTECA, null, values);  // Inserta los valores en la tabla "biblioteca"

            // Verifica si la inserción fue exitosa (el método insert retorna -1 en caso de error)
            return result != -1;  // Si el resultado es distinto de -1, la inserción fue exitosa
        } catch (Exception e) {
            // Si ocurre un error durante la inserción, se captura y muestra en el log
            Log.e("DB_ERROR", "Error al insertar juego en biblioteca: ", e);
            return false;  // Retorna false si hubo un error
        } finally {
            // Cierra la base de datos para liberar recursos
            db.close();
        }
    }





    @SuppressLint("Range")
    public List<Juego> obtenerJuegosDeBiblioteca(String bibliotecaId) {
        // Lista para almacenar los juegos de la biblioteca
        List<Juego> juegos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();  // Obtiene la base de datos en modo lectura

        // Realiza una consulta para obtener los juegos asociados al usuario con el 'bibliotecaId'
        String query = "SELECT juegos.* FROM " + TABLE_JUEGOS + " AS juegos " +
                "INNER JOIN " + TABLE_BIBLIOTECA + " AS biblioteca " +
                "ON juegos.id = biblioteca.juego_id " +  // Hace la unión con la tabla 'biblioteca' por el id del juego
                "WHERE biblioteca.usuario_id = ?";  // Filtra por el id del usuario

        // Ejecuta la consulta
        Cursor cursor = db.rawQuery(query, new String[]{bibliotecaId});

        // Si el cursor tiene datos, procesa los resultados
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Crea un nuevo objeto Juego y asigna los valores del cursor
                Juego juego = new Juego();
                juego.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_JUEGO_ID)));  // Asigna el id del juego
                juego.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_NOMBRE)));  // Asigna el nombre del juego
                juego.setDescripcion(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_DESCRIPCION)));  // Asigna la descripción
                juego.setPrecio(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_PRECIO)));  // Asigna el precio
                juego.setImagen(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_IMAGEN)));  // Asigna la imagen
                juego.setTrailer(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_TRAILER)));  // Asigna el trailer

                // Agrega el juego a la lista
                juegos.add(juego);
            } while (cursor.moveToNext());  // Continúa con el siguiente juego

            cursor.close();  // Cierra el cursor cuando ya no sea necesario
        }
        return juegos;  // Retorna la lista de juegos
    }


    @SuppressLint("Range")
    public List<Juego> obtenerJuegosPorBiblioteca(String bibliotecaId) {
        // Lista para almacenar los juegos de la biblioteca
        List<Juego> juegos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();  // Obtiene la base de datos en modo lectura

        // Realiza una consulta para obtener el nombre y la imagen de los juegos asociados al 'bibliotecaId'
        String query = "SELECT j." + COLUMN_JUEGO_NOMBRE + ", j." + COLUMN_JUEGO_IMAGEN +
                " FROM " + TABLE_JUEGOS + " j " +  // Selecciona los juegos
                "INNER JOIN " + TABLE_BIBLIOTECA + " b ON j." + COLUMN_JUEGO_ID + " = b." + COLUMN_BIBLIOTECA_JUEGO_ID +  // Hace el JOIN entre juegos y biblioteca
                " WHERE b." + COLUMN_BIBLIOTECA_USUARIO_ID + " = ?";  // Filtra por el id del usuario en la biblioteca

        // Ejecuta la consulta
        Cursor cursor = db.rawQuery(query, new String[]{bibliotecaId});

        // Si el cursor tiene datos, procesa los resultados
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Crea un nuevo objeto Juego con el nombre y la imagen del juego
                Juego juego = new Juego();
                juego.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_NOMBRE)));  // Asigna el nombre del juego
                juego.setImagen(cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_IMAGEN)));  // Asigna la imagen del juego

                // Agrega el juego a la lista
                juegos.add(juego);
            } while (cursor.moveToNext());  // Continúa con el siguiente juego

            cursor.close();  // Cierra el cursor cuando ya no sea necesario
        }

        db.close();  // Cierra la base de datos
        return juegos;  // Retorna la lista de juegos
    }



    @SuppressLint("Range")  // Desactiva la advertencia sobre el uso de índices en el cursor
    public List<Juego> obtenerJuegosPorUsuario(String userId) {
        List<Juego> juegos = new ArrayList<>();  // Crea una lista vacía para almacenar los juegos
        SQLiteDatabase db = this.getReadableDatabase();  // Obtiene una referencia a la base de datos en modo lectura
        String query = "SELECT * FROM juegos WHERE user_id = ?";  // Consulta para obtener juegos por usuario
        Cursor cursor = db.rawQuery(query, new String[]{userId});  // Ejecuta la consulta con el userId como parámetro

        if (cursor != null) {  // Si el cursor no es nulo (es decir, si se encontraron resultados)
            while (cursor.moveToNext()) {  // Itera sobre las filas del cursor
                Juego juego = new Juego();  // Crea un nuevo objeto Juego
                // Mapea los datos del cursor a las propiedades del objeto Juego
                juego.setId(cursor.getInt(cursor.getColumnIndex("id")));  // Asigna el ID del juego
                juego.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));  // Asigna el nombre del juego
                juego.setImagen(cursor.getString(cursor.getColumnIndex("imagen")));  // Asigna la imagen del juego
                juegos.add(juego);  // Añade el objeto Juego a la lista
            }
            cursor.close();  // Cierra el cursor una vez que se ha terminado de usar
        }

        db.close();  // Cierra la base de datos
        return juegos;  // Devuelve la lista de juegos obtenidos
    }

    public List<Juego> obtenerJuegos() {
        List<Juego> juegos = new ArrayList<>();  // Crea una lista vacía para almacenar los juegos
        SQLiteDatabase db = this.getReadableDatabase();  // Obtiene una referencia a la base de datos en modo lectura

        Cursor cursor = db.query(TABLE_JUEGOS,  // Realiza una consulta en la tabla de juegos
                null,  // Selecciona todas las columnas
                null,  // Sin condiciones
                null,  // Sin agrupación
                null,  // Sin filtrado
                null,  // Sin ordenación
                null);  // Sin límite

        if (cursor != null) {  // Si el cursor contiene resultados
            if (cursor.moveToFirst()) {  // Mueve el cursor a la primera fila de resultados
                do {
                    // Obtiene los datos del juego de cada columna del cursor
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_JUEGO_ID));  // Obtiene el ID del juego
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_NOMBRE));  // Obtiene el nombre del juego
                    @SuppressLint("Range") String precio = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_PRECIO));  // Obtiene el precio del juego
                    @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_DESCRIPCION));  // Obtiene la descripción del juego
                    @SuppressLint("Range") String imagen = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_IMAGEN));  // Obtiene la imagen del juego
                    @SuppressLint("Range") String trailer = cursor.getString(cursor.getColumnIndex(COLUMN_JUEGO_TRAILER));  // Obtiene el trailer del juego

                    // Añade el juego a la lista
                    juegos.add(new Juego(id, nombre, precio, descripcion, imagen, trailer));
                } while (cursor.moveToNext());  // Repite mientras haya más juegos en el cursor
            }
            cursor.close();  // Cierra el cursor una vez que se ha terminado de usar
        }
        return juegos;  // Devuelve la lista de juegos obtenidos
    }

    public boolean registrarUsuario(String dni, String email, String nombre, String password) {
        if (usuarioExiste(dni, email)) {  // Verifica si el usuario ya existe con el DNI o el correo
            return false;  // Si ya existe, no lo registra y devuelve false
        }

        SQLiteDatabase db = this.getWritableDatabase();  // Obtiene una referencia a la base de datos en modo escritura
        ContentValues values = new ContentValues();  // Crea un objeto ContentValues para insertar datos
        values.put("dni", dni);  // Asigna el DNI al ContentValues
        values.put("email", email);  // Asigna el email al ContentValues
        values.put("nombre", nombre);  // Asigna el nombre al ContentValues
        values.put("password", password);  // Asigna la contraseña al ContentValues

        long result = db.insert("usuarios", null, values);  // Inserta los valores en la tabla 'usuarios'
        db.close();  // Cierra la base de datos
        return result != -1;  // Si la inserción fue exitosa (no retornó -1), devuelve true, sino false
    }

    private boolean usuarioExiste(String dni, String email) {
        SQLiteDatabase db = this.getReadableDatabase();  // Obtiene una referencia a la base de datos en modo lectura
        String query = "SELECT * FROM usuarios WHERE dni = ? OR email = ?";  // Consulta para verificar si el usuario ya existe
        Cursor cursor = db.rawQuery(query, new String[]{dni, email});  // Ejecuta la consulta con los parámetros DNI y email

        boolean existe = cursor.getCount() > 0;  // Si el cursor tiene más de 0 filas, el usuario ya existe
        cursor.close();  // Cierra el cursor
        db.close();  // Cierra la base de datos
        return existe;  // Devuelve true si el usuario existe, false si no
    }

    public Cursor obtenerDatosUsuario(String usuarioId) {
        SQLiteDatabase db = null;  // Declara una variable para la base de datos
        Cursor cursor = null;  // Declara una variable para el cursor

        try {
            db = this.getReadableDatabase();  // Obtiene una referencia a la base de datos en modo lectura
            // Especifica las columnas necesarias para obtener los datos del usuario
            String[] columnas = {"dni", "nombre", "direccion", "ciudad", "codigoPostal", "email", "login"};

            // Realiza la consulta en la tabla 'usuarios' usando el DNI como parámetro de búsqueda
            cursor = db.query("usuarios", columnas, "dni=?", new String[]{usuarioId}, null, null, null);

            // Si se encuentra el dato, retorna el cursor
            if (cursor != null && cursor.moveToFirst()) {
                return cursor;  // Retorna el cursor con los datos del usuario
            } else {
                if (cursor != null) {
                    cursor.close();  // Cierra el cursor si no se encontraron resultados
                }
                Log.d("DBHelper", "No se encontraron datos para el DNI: " + usuarioId);  // Muestra un mensaje en los logs si no se encuentran datos
            }
        } catch (Exception e) {
            e.printStackTrace();  // Imprime el error si ocurre una excepción
        }

        return null;  // Devuelve null si no se encuentran los datos
    }

    public byte[] recuperarImagenDeUsuario(String dni) {
        SQLiteDatabase db = this.getReadableDatabase();  // Obtiene una referencia a la base de datos en modo lectura
        String[] projection = {COLUMN_USUARIO_IMAGE};  // Especifica que se debe recuperar la columna de imagen
        String selection = "dni = ?";  // Filtro para buscar por DNI
        String[] selectionArgs = {dni};  // Argumento para el filtro de DNI

        // Realiza la consulta en la tabla 'usuarios' para obtener la imagen
        Cursor cursor = db.query(TABLE_USUARIOS, projection, selection, selectionArgs, null, null, null);
        byte[] imagen = null;  // Inicializa la variable de imagen como null por si no se encuentra

        if (cursor != null) {  // Si el cursor tiene resultados
            try {
                if (cursor.moveToFirst()) {  // Si hay al menos una fila
                    int columnIndex = cursor.getColumnIndex(COLUMN_USUARIO_IMAGE);  // Obtiene el índice de la columna de imagen
                    if (columnIndex != -1) {  // Verifica que la columna exista
                        imagen = cursor.getBlob(columnIndex);  // Recupera la imagen almacenada como BLOB
                        Log.d("DBHelper", "Imagen recuperada de la base de datos, tamaño: " + (imagen != null ? imagen.length : "null"));  // Imprime el tamaño de la imagen
                    }
                }
            } finally {
                cursor.close();  // Asegúrate de cerrar el cursor una vez que se ha usado
            }
        }

        db.close();  // Cierra la base de datos
        return imagen;  // Devuelve la imagen o null si no se encontró
    }

}